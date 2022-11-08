package net.morher.house.api.hass;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.entity.switches.SwitchOptions;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class HomeAssistantAutoDiscoveryControllerTest {
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    @Test
    public void testOnEntityUpdated() {
        MqttNamespace namespace = MqttNamespace.defaultNamespace();
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(namespace).when(client).getNamespace();
        doCallRealMethod().when(client).topic(any(), any(), anyBoolean());

        SwitchEntity entity = new SwitchEntity(client, ENTITY_ID, null);
        entity.setDeviceInfo(new DeviceInfo());
        entity.setOptions(new SwitchOptions());

        new HomeAssistantAutoDiscoveryController(client)
                .onEntityUpdated(entity);

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Boolean> retain = ArgumentCaptor.forClass(boolean.class);

        verify(client, times(1))
                .publish(topic.capture(), payload.capture(), retain.capture());

        assertThat(topic.getValue(), is(equalTo("homeassistant/switch/room-device-entity/config")));
        assertThat(payload.getValue(), is(not(nullValue())));
        assertThat(retain.getValue(), is(true));

    }
}
