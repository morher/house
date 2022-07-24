package net.morher.house.shelly.controller;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttTopicManager;
import net.morher.house.api.mqtt.payload.EnumMessage;
import net.morher.house.api.mqtt.payload.NumberMessage;
import net.morher.house.api.subscription.Subscription;
import net.morher.house.shelly.controller.ShellyCover.ShellyCoverState;

@Slf4j
public class ShellyCoverTopics {
    private final MqttTopicManager<ShellyCoverState> stateTopic;
    private final MqttTopicManager<ShellyCoverState> commandTopic;
    private final Subscription powerSubscription;

    public ShellyCoverTopics(HouseMqttClient mqtt, String nodeName, Consumer<Double> powerListener) {
        stateTopic = mqtt.topic("shellies/" + nodeName + "/roller/0", EnumMessage.lowercase(ShellyCoverState.class));

        commandTopic = stateTopic
                .subTopic("/command", EnumMessage.lowercase(ShellyCoverState.class));

        powerSubscription = stateTopic
                .subTopic("/power", NumberMessage.decimal())
                .subscribe(powerListener);
    }

    public void command(ShellyCoverState command) {
        log.debug("Send command to {}: {}", commandTopic.getTopic(), command);
        commandTopic.publish(command, false);
    }

    public void disconnect() {
        powerSubscription.unsubscribe();
    }
}
