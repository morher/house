package net.morher.house.buttons.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.morher.house.api.mqtt.client.MqttMessageListener.ParsedMqttMessageListener;

public class PropertyFilterTest {

    private final StringMqttMessageListener listener = mock(StringMqttMessageListener.class);

    @Test
    public void testPropertyFound() {
        new PropertyFilter("Switch2.Action", listener)
                .onMessage(
                        "some/topic",
                        jsonNode("""
                                {
                                  "Switch1": {
                                    "Action": "ON"
                                  },
                                  "Switch2": {
                                    "Action": "OFF"
                                  }
                                }
                                """),
                        0,
                        false);
        verify(listener, times(1))
                .onMessage(eq("some/topic"), eq("OFF"), eq(0), eq(false));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testMessageIsNotJsonObject() {
        new PropertyFilter("Switch2.Action", listener)
                .onMessage(
                        "some/topic",
                        jsonNode("""
                                true
                                """),
                        0,
                        false);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testPropertyNotFound() {
        new PropertyFilter("Switch3.Action", listener)
                .onMessage(
                        "some/topic",
                        jsonNode("""
                                {
                                  "Switch1": {
                                    "Action": "ON"
                                  },
                                  "Switch2": {
                                    "Action": "OFF"
                                  }
                                }
                                """),
                        0,
                        false);
        verifyNoMoreInteractions(listener);
    }

    private JsonNode jsonNode(String json) {
        try {
            return new ObjectMapper()
                    .readValue(json, JsonNode.class);

        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot deserialize json: " + json, e);
        }
    }

    private interface StringMqttMessageListener extends ParsedMqttMessageListener<String> {

    }
}
