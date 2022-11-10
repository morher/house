package net.morher.house.api.entity.light;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.test.client.TestHouseMqttClient;
import net.morher.house.test.subscription.TestSubscriber;

@SuppressWarnings("unchecked")
public class LightStateHandlerTest {
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    private final TestHouseMqttClient client = TestHouseMqttClient.loopback();
    private final EntityManager entityManager = new EntityManager(client);
    private final LightEntity lightEntity = entityManager.lightEntity(ENTITY_ID);

    @Test
    public void testHandleCommand() {
        TestSubscriber<LightState> lightStateSubscriber = new TestSubscriber<>(lightEntity.state());
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);
        lightEntity.command().publish(state);

        verify(listener, times(1)).onStateUpdated(eq(state));

        assertThat(lightStateSubscriber.items(), hasItems(equalTo(state)));

        // Additional updates even if state is unchanged after command:
        lightStateSubscriber.reset();
        lightEntity.command().publish(new LightState().withState(PowerState.ON));

        verify(listener, times(2)).onStateUpdated(eq(state));
        assertThat(lightStateSubscriber.items(), hasItems(equalTo(state)));
    }

    @Test
    public void testUpdateLightStateLocally() {
        TestSubscriber<LightState> lightStateSubscriber = new TestSubscriber<>(lightEntity.state());
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        LightStateHandler handler = new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);

        // Do not notify listener when state is updated locally
        handler.updateState(state);

        verify(listener, never()).onStateUpdated(eq(state));
        assertThat(lightStateSubscriber.items(), hasItems(equalTo(state)));
        lightStateSubscriber.reset();

        // Do not publish state again if unchanged
        handler.updateState(state);
        verify(listener, never()).onStateUpdated(eq(state));

        assertThat(lightStateSubscriber.items(), is(empty()));
    }

    @Test
    public void testHandleStateChanges() {
        TestSubscriber<LightState> lightStateSubscriber = new TestSubscriber<>(lightEntity.state());
        LightEntity entity = new LightEntity(client, ENTITY_ID, null);

        EntityStateListener<LightState> listener = mock(EntityStateListener.class);
        new LightStateHandler(entity, new DeviceInfo(), listener);

        LightState state = new LightState(PowerState.ON, 127, null);

        // Notify listener
        lightEntity.state().publish(state);

        verify(listener, times(1)).onStateUpdated(eq(state));
        assertThat(lightStateSubscriber.items(), hasItems(equalTo(state)));

        // Do not notify listener when state is unmodified
        lightEntity.state().publish(state);
        verify(listener, times(1)).onStateUpdated(eq(state));
        assertThat(lightStateSubscriber.items(), hasItems(equalTo(state), equalTo(state)));

    }

}
