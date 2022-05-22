package net.morher.house.api.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.morher.house.api.entity.common.CommandableEntity;
import net.morher.house.api.entity.common.EntityCommandListener;
import net.morher.house.api.entity.common.EntityOptions;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.api.mqtt.payload.RawMessage;

public class CommandableEntityTest {
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    @Test
    public void testUseNamespace() {
        MqttNamespace namespace = mock(MqttNamespace.class);
        doReturn("the/command/topic").when(namespace).entityCommandTopic(any(EntityId.class));

        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();

        TestCommandableEntity testEntity = testEntity(client);

        ArgumentCaptor<EntityId> entityCaptor = ArgumentCaptor.forClass(EntityId.class);
        verify(namespace, times(1)).entityCommandTopic(entityCaptor.capture());

        assertThat(entityCaptor.getValue(), is(equalTo(ENTITY_ID)));
        assertThat(testEntity.getCommandTopic(), is(equalTo("the/command/topic")));
    }

    @Test
    public void testSendCommand() {
        MqttNamespace namespace = MqttNamespace.defaultNamespace();
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();

        testEntity(client).sendCommand("Test command");

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Boolean> retain = ArgumentCaptor.forClass(boolean.class);

        verify(client, times(1))
                .publish(topic.capture(), payload.capture(), retain.capture());

        assertThat(topic.getValue(), is(equalTo("house/room/device/entity/command")));
        assertThat(payload.getValue(), is(equalTo("Test command".getBytes())));
        assertThat(retain.getValue(), is(false));
    }

    @Test
    public void testSubscribe() {
        MqttNamespace namespace = MqttNamespace.defaultNamespace();
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();

        @SuppressWarnings("unchecked")
        EntityCommandListener<String> listenerMock = mock(EntityCommandListener.class);

        TestCommandableEntity testEntity = testEntity(client);
        testEntity.command().subscribe(listenerMock);

        ArgumentCaptor<MqttMessageListener> listenerCaptor = ArgumentCaptor.forClass(MqttMessageListener.class);
        verify(client, times(1)).subscribe(eq("house/room/device/entity/command"), listenerCaptor.capture());

        MqttMessageListener listener = listenerCaptor.getValue();
        listener.onMessage("house/room/device/entity", "Test command".getBytes(), 0, true);

        verify(listenerMock, times(1)).onCommand(eq("Test command"));
    }

    protected TestCommandableEntity testEntity(HouseMqttClient client) {
        return new TestCommandableEntity(client, ENTITY_ID, null);
    }

    public static class TestCommandableEntity extends CommandableEntity<String, EntityOptions, String> {

        public TestCommandableEntity(HouseMqttClient client, EntityId entityId, EntityListener entityListener) {
            super(client, entityId, entityListener, RawMessage.toStr(), RawMessage.toStr());
        }

    }
}
