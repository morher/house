package net.morher.house.api.entity.light;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.api.mqtt.payload.JsonMessage;
import net.morher.house.api.mqtt.payload.PayloadFormat;
import net.morher.house.api.test.TestHouseMqttClient;
import net.morher.house.api.test.TestHouseMqttClient.TestMessage;

public class LightStateHandlerTest {
    private static final PayloadFormat<LightState> LIGHT_FORMAT = JsonMessage.toType(LightState.class);
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    @Test
    public void testHandleCommand() {
        TestHouseMqttClient client = new TestHouseMqttClient();
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        @SuppressWarnings("unchecked")
        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);
        client.simulateReceivedMessage("house/room/device/entity/command", state, LIGHT_FORMAT);

        verify(listener, times(1)).onStateUpdated(eq(state));
        assertThat(client.getMessages().size(), is(1));
        TestMessage message = client.getMessages().get(0);
        assertThat(message.getPayload(LIGHT_FORMAT), is(equalTo(state)));

        // Additional updates even if state is unchanged after command:
        client.simulateReceivedMessage("house/room/device/entity/command", new LightState().withState(PowerState.ON), LIGHT_FORMAT);

        verify(listener, times(2)).onStateUpdated(eq(state));
        assertThat(client.getMessages().size(), is(2));
    }

    @Test
    public void testUpdateLightStateLocally() {
        TestHouseMqttClient client = new TestHouseMqttClient();
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        @SuppressWarnings("unchecked")
        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        BaseStateHandler handler = new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);

        // Do not notify listener when state is updated locally
        handler.updateState(state);

        verify(listener, never()).onStateUpdated(eq(state));
        assertThat(client.getMessages().size(), is(1));
        TestMessage message = client.getMessages().get(0);
        assertThat(message.getPayload(LIGHT_FORMAT), is(equalTo(state)));

        // Do not publish state again if unchanged
        handler.updateState(state);
        verify(listener, never()).onStateUpdated(eq(state));
        assertThat("Should not publish unchanged state",
                client.getMessages().size(), is(1));
    }

    @Test
    public void testHandleStateChanges() {
        TestHouseMqttClient client = new TestHouseMqttClient();
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        @SuppressWarnings("unchecked")
        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);

        // Notify listener
        client.simulateReceivedMessage("house/room/device/entity", state, LIGHT_FORMAT);

        verify(listener, times(1)).onStateUpdated(eq(state));
        assertThat(client.getMessages().size(), is(0));

        // Do not notify listener when state is unmodified
        client.simulateReceivedMessage("house/room/device/entity", state, LIGHT_FORMAT);
        verify(listener, times(1)).onStateUpdated(eq(state));
        assertThat(client.getMessages().size(), is(0));

    }

}
