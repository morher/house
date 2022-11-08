package net.morher.house.api.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.morher.house.api.entity.common.EntityOptions;
import net.morher.house.api.entity.common.StatefullEntity;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.mqtt.payload.RawMessage;

public class StatefullEntityTest {
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    @Test
    public void testUseNamespace() {
        MqttNamespace namespace = mock(MqttNamespace.class);
        doReturn("the/state/topic").when(namespace).entityStateTopic(any(EntityId.class));

        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();
        doCallRealMethod().when(client).topic(any(), any(), anyBoolean());

        TestStatefullEntity testEntity = testEntity(client);

        ArgumentCaptor<EntityId> entityCaptor = ArgumentCaptor.forClass(EntityId.class);
        verify(namespace, times(1)).entityStateTopic(entityCaptor.capture());

        assertThat(entityCaptor.getValue(), is(equalTo(ENTITY_ID)));
        assertThat(testEntity.state().getTopic(), is(equalTo("the/state/topic")));
    }

    @Test
    public void testPublishState() {
        MqttNamespace namespace = MqttNamespace.defaultNamespace();
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();
        doCallRealMethod().when(client).topic(any(), any(), anyBoolean());

        testEntity(client).state().publish("Test state!");

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Boolean> retain = ArgumentCaptor.forClass(boolean.class);

        verify(client, times(1))
                .publish(topic.capture(), payload.capture(), retain.capture());

        assertThat(topic.getValue(), is(equalTo("house/room/device/entity")));
        assertThat(payload.getValue(), is(equalTo("Test state!".getBytes())));
        assertThat(retain.getValue(), is(true));
    }

    @Test
    public void testSubscribe() {
        MqttNamespace namespace = MqttNamespace.defaultNamespace();
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();
        doCallRealMethod().when(client).topic(any(), any(), anyBoolean());

        @SuppressWarnings("unchecked")
        Consumer<String> listenerMock = mock(Consumer.class);

        TestStatefullEntity testEntity = testEntity(client);
        testEntity.state().subscribe(listenerMock);

        ArgumentCaptor<MqttMessageListener> listenerCaptor = ArgumentCaptor.forClass(MqttMessageListener.class);
        verify(client, times(1)).subscribe(eq("house/room/device/entity"), listenerCaptor.capture());

        MqttMessageListener listener = listenerCaptor.getValue();
        listener.onMessage("house/room/device/entity", "Test state".getBytes(), 0, true);

        verify(listenerMock, times(1)).accept(eq("Test state"));
    }

    protected TestStatefullEntity testEntity(HouseMqttClient client) {
        return new TestStatefullEntity(client, ENTITY_ID, null);
    }

    public static class TestStatefullEntity extends StatefullEntity<String, EntityOptions> {
        public TestStatefullEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
            super(client, entityId, entityListener, RawMessage.toStr());
        }
    }
}
