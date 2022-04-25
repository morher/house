package net.morher.house.api.mqtt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.schedule.HouseScheduler;
import net.morher.house.api.subscription.Subscription;

@Slf4j
public class HouseMqttClientImpl implements HouseMqttClient {
    protected final ScheduledExecutorService scheduler = HouseScheduler.get("mqtt-client");
    private final Queue<Message> outgoingQueue = new ConcurrentLinkedQueue<Message>();
    private final Map<String, TopicSubscription> topicSubscriptions = new HashMap<>();
    private final MqttConnection mqttConnection;
    private final MqttNamespace namespace;
    private final MqttAvailabilityPolicy availabilityPolicy;
    private boolean publishScheduled;

    public HouseMqttClientImpl(MqttOptions options, MqttNamespace namespace) {
        this(options, namespace, new DefaultClientFactory());
    }

    public HouseMqttClientImpl(MqttOptions options, MqttNamespace namespace, MqttClientFactory clientFactory) {
        this.namespace = namespace;
        this.availabilityPolicy = new MqttAvailabilityPolicy(namespace.clientAvailabilityTopic(options.getClientId()));

        mqttConnection = new MqttConnection(scheduler, options, availabilityPolicy, clientFactory);
    }

    @Override
    public MqttNamespace getNamespace() {
        return namespace;
    }

    private void dispatchMessage(String topic, MqttMessage message) {
        for (TopicSubscription sub : topicSubscriptions.values()) {
            if (sub.isMatch(topic)) {
                sub.dispatchMessage(topic, message);
            }
        }
    }

    @Override
    public String getAvailabilityTopic() {
        return availabilityPolicy.getTopic();
    }

    @Override
    public void publish(String topic, byte[] payload, boolean retain) {
        outgoingQueue.add(new Message(topic, payload, 0, retain));
        if (!publishScheduled) {
            publishScheduled = true;
            scheduler.execute(this::publishMessages);
        }
    }

    public void publishMessages() {
        publishScheduled = false;
        if (!mqttConnection.isConnected()) {
            return;
        }
        outgoingQueue.size();
        try {
            while (!outgoingQueue.isEmpty()) {
                Message message = outgoingQueue.peek();
                if (message == null) {
                    return;
                }
                mqttConnection.client
                        .publish(
                                message.getTopic(),
                                message.getPayload(),
                                message.getQos(),
                                message.isRetain())
                        .waitForCompletion();
                outgoingQueue.poll();
            }
        } catch (MqttException e) {
            log.error("Failed to send message: {}", e.getMessage(), e);
        }
    }

    @Override
    public Subscription subscribe(String topic, MqttMessageListener listener) {
        TopicSubscription sub = registerSubscription(topic, listener);
        scheduler.execute(() -> syncSubscription(sub));
        return () -> this.unsubscribe(sub, listener);
    }

    private TopicSubscription registerSubscription(String topic, MqttMessageListener listener) {
        TopicSubscription sub;
        synchronized (topicSubscriptions) {
            sub = topicSubscriptions.get(topic);
            if (sub == null) {
                sub = new TopicSubscription(topic);
                topicSubscriptions.put(topic, sub);
            }
            sub.registerListener(listener);
        }
        return sub;
    }

    private void unsubscribe(TopicSubscription sub, MqttMessageListener listener) {
        sub.removeListener(listener);
        scheduler.execute(() -> syncSubscription(sub));
    }

    private void syncSubscriptions() {
        for (TopicSubscription sub : topicSubscriptions.values()) {
            syncSubscription(sub);
        }
    }

    private void syncSubscription(TopicSubscription sub) {
        if (!mqttConnection.isConnected()) {
            return;
        }

        try {
            if (sub.isInUse()) {
                if (!sub.isRegistered()) {
                    log.debug("Subscribing to {}", sub.getTopic());
                    mqttConnection.client.subscribe(sub.getTopic(), 0);
                    sub.setRegistered(true);
                }
            } else {
                if (sub.isRegistered()) {
                    log.debug("Unsubscribing from {}", sub.getTopic());
                    mqttConnection.client.unsubscribe(sub.getTopic());
                }
            }
        } catch (MqttException e) {
            log.error("Error during subscription sync for topic {}", sub.getTopic(), e);
        }
    }

    private class MqttConnection implements MqttCallback {
        private final ScheduledExecutorService scheduler;
        private final MqttOptions options;
        private final MqttAvailabilityPolicy availabilityPolicy;
        private final MqttClientFactory clientFactory;
        private IMqttAsyncClient client;

        public MqttConnection(
                ScheduledExecutorService scheduler,
                MqttOptions options,
                MqttAvailabilityPolicy availabilityPolicy,
                MqttClientFactory clientFactory) {

            this.scheduler = scheduler;
            this.options = options;
            this.availabilityPolicy = availabilityPolicy;
            this.clientFactory = clientFactory;
            scheduler.execute(this::connect);
        }

        public boolean isConnected() {
            return client != null
                    && client.isConnected();
        }

        private void reconnect() {
            try {
                disconnect();
                connect();

            } catch (Exception e) {
                scheduler.schedule(this::reconnect, 3, TimeUnit.SECONDS);
            }
        }

        private void connect() {
            try {
                if (client != null && client.isConnected()) {
                    return;
                }
                MqttOptions options = this.options;
                client = clientFactory.connect(options, availabilityPolicy, this);

                log.info("Connceted to MQTT-server {} as {}", client.getServerURI(), options.getUsername());

                scheduler.execute(HouseMqttClientImpl.this::syncSubscriptions);
                scheduler.execute(HouseMqttClientImpl.this::publishMessages);
            } catch (Exception e) {
                scheduler.schedule(this::reconnect, options.getReconnectInterval(), TimeUnit.SECONDS);
            }
        }

        private void disconnect() {
            if (client != null && client.isConnected()) {
                try {
                    client
                            .disconnect()
                            .waitForCompletion();

                } catch (MqttException e) {
                    log.warn("Failed to disconnect.", e);
                }
            }

            client = null;
            for (TopicSubscription sub : topicSubscriptions.values()) {
                sub.setRegistered(false);
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            scheduler.execute(() -> dispatchMessage(topic, message));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // Do nothing
        }

        @Override
        public void connectionLost(Throwable cause) {
            log.warn("Disconnected from MQTT server.");
            scheduler.execute(this::reconnect);
        }
    }

    private class TopicSubscription {
        private final String topicFilter;
        private final List<MqttMessageListener> listeners = new ArrayList<>();
        private boolean registered;

        public TopicSubscription(String topic) {
            this.topicFilter = topic;
        }

        public void dispatchMessage(String topic, MqttMessage message) {
            for (MqttMessageListener listener : listeners) {
                try {
                    listener.onMessage(topic, message.getPayload(), message.getQos(), message.isRetained());

                } catch (Exception e) {
                    log.error("Exception in message listener: {}", e.getMessage(), e);
                }
            }
        }

        public void registerListener(MqttMessageListener listener) {
            listeners.add(listener);
        }

        public void removeListener(MqttMessageListener listener) {
            listeners.remove(listener);
        }

        public String getTopic() {
            return topicFilter;
        }

        public boolean isMatch(String topic) {
            return MqttTopic.isMatched(topicFilter, topic);
        }

        public boolean isInUse() {
            return !listeners.isEmpty();
        }

        public boolean isRegistered() {
            return registered;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }

    }

}
