package net.morher.house.buttons.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.mqtt.payload.RawMessage;
import net.morher.house.buttons.action.Action;
import net.morher.house.buttons.action.ActionBuilder;
import net.morher.house.buttons.config.ActionConfig;
import net.morher.house.buttons.config.ButtonsConfig;
import net.morher.house.buttons.config.ButtonsConfig.InputConfig;
import net.morher.house.buttons.config.ButtonsConfig.TemplateConfig;
import net.morher.house.buttons.input.Button;
import net.morher.house.buttons.pattern.ButtonEvent;
import net.morher.house.buttons.pattern.ButtonListener;
import net.morher.house.buttons.pattern.ButtonManager;

@Slf4j
public class ButtonsController {
    private final HouseMqttClient client;
    private final ButtonManager buttonManager;
    private final DeviceManager deviceManager;
    private final List<ButtonInput> inputs = new ArrayList<>();

    public ButtonsController(HouseMqttClient client, ButtonManager buttonManager, DeviceManager deviceManager) {
        this.client = client;
        this.buttonManager = buttonManager;
        this.deviceManager = deviceManager;
    }

    public void configure(ButtonsConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("No buttons configuration");
        }
        Map<String, TemplateConfig> templates = config.getTemplates();

        configureInputs(config.getInputs(), templates);
    }

    private void configureInputs(List<InputConfig> inputs, Map<String, TemplateConfig> templates) {
        for (InputConfig input : inputs) {
            configureInput(input, templates);
        }
    }

    private void configureInput(InputConfig config, Map<String, TemplateConfig> templates) {
        ButtonInput input = new ButtonInput(config.isInverted());

        Map<String, List<ActionConfig>> events = new HashMap<>();
        addEventsFromTemplates(events, config.getTemplates(), templates);
        events.putAll(config.getEvents());

        ActionBuilder context = new ActionBuilder(deviceManager, config);

        for (Map.Entry<String, List<ActionConfig>> event : events.entrySet()) {
            String eventType = event.getKey();
            Action action = context.buildAction(event.getValue());
            input.putEvent(eventType, action);
        }

        client.subscribe(config.getTopic(), inputListener(input, config));
        inputs.add(input);
    }

    private MqttMessageListener inputListener(ButtonInput input, InputConfig config) {
        if (config.getProperty() != null) {
            return MqttMessageListener
                    .map(JsonMessage.toJsonNode())
                    .thenNotify(new PropertyFilter(config.getProperty(), input));
        }
        return MqttMessageListener
                .map(RawMessage.toStr())
                .thenNotify(input);
    }

    private void addEventsFromTemplates(
            Map<String, List<ActionConfig>> events,
            List<String> templateNames,
            Map<String, TemplateConfig> templates) {

        for (String templateName : templateNames) {
            TemplateConfig template = templates.get(templateName);
            if (template != null) {
                events.putAll(template.getEvents());

            } else {
                throw new IllegalArgumentException("Switch template not found: " + templateName);
            }
        }

    }

    private class ButtonInput implements ButtonListener, ParsedMqttMessageListener<String> {
        private Map<String, Action> eventAction = new HashMap<>();
        private final Button button;
        private final List<String> pressedPayloads = new ArrayList<>();

        public ButtonInput(boolean inverted) {
            pressedPayloads.add("on");
            pressedPayloads.add("1");
            Button button = buttonManager.createButton(this);
            if (inverted) {
                button = button.inverted();
            }
            this.button = button;
        }

        public void putEvent(String eventType, Action action) {
            eventAction.put(eventType, action);
        }

        @Override
        public void onMessage(String topic, String data, int qos, boolean retained) {
            button.reportState(pressedPayloads.contains(data.toLowerCase()));
        }

        @Override
        public void onButtonEvent(ButtonEvent event) {
            if (event.getPrecedingEvent() == null) {
                log.debug("New event, store pre event state");
                storePreEventContext();
            }
            String eventType = event.toString();
            log.trace("Received event: {}", eventType);
            Action action = eventAction.get(eventType);
            if (action != null) {
                log.debug("Perform action:\n{}", action);
                action.perform();
            }
        }

        private void storePreEventContext() {
            for (Action action : eventAction.values()) {
                action.storePreEventState();
            }
        }
    }

}
