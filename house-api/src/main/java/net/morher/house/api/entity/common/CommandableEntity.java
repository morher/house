package net.morher.house.api.entity.common;

import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityListener;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.subscription.SubscriptionRegistry;
import net.morher.house.api.subscription.SubscriptionTopic;

public abstract class CommandableEntity<S, O extends EntityOptions, C> extends StatefullEntity<S, O> {
    private final SubscriptionRegistry<EntityCommandListener<C>> commandSubscriptions;
    private final MqttTopicManager<C> commandTopic;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public CommandableEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener, PayloadFormat<S> stateSerializer, PayloadFormat<C> commandSerializer) {
        super(client, entityId, entityListener, stateSerializer);

        commandSubscriptions = new SubscriptionRegistry(EntityCommandListener.class);
        commandSubscriptions.addRegistryChangedListener(this::syncCommandSubscriptions);

        this.commandTopic = new MqttTopicManager<>(
                client,
                client.getNamespace().entityCommandTopic(entityId),
                commandSerializer,
                this::onCommand);

    }

    public String getCommandTopic() {
        return this.commandTopic.getTopic();
    }

    private void onCommand(String topic, C command, int qos, boolean retained) {
        commandSubscriptions.getDispatcher().onCommand(command);
    }

    public void sendCommand(C command) {
        commandTopic.publish(command, false);
    }

    public final SubscriptionTopic<EntityCommandListener<C>> command() {
        return commandSubscriptions;
    }

    protected final void syncCommandSubscriptions() {
        syncStateSubscriptions();
        commandTopic.activateSubscription(!commandSubscriptions.isEmpty());
    }

    @Override
    protected boolean needStateSubscription() {
        return !commandSubscriptions.isEmpty();
    }
}