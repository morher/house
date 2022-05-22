package net.morher.house.api.hass;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.common.ConfigurableEntity;
import net.morher.house.api.entity.common.EntityOptions;
import net.morher.house.api.mqtt.client.HouseMqttClient;

public class BaseEntityAnnouncerTest {
    private static final EntityId ENTITY_ID = new EntityId(new DeviceId("room", "device"), "entity");

    @Test
    public void testDefaultValues() {
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn("client/availability/topic").when(client).getAvailabilityTopic();
        TestEntity entity = new TestEntity(ENTITY_ID);

        DeviceInfo deviceInfo = new DeviceInfo();
        entity.setDeviceInfo(deviceInfo);

        EntityOptions options = new EntityOptions();
        entity.setOptions(options);

        new TestAnnouncer(client).announce(entity);

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Boolean> retain = ArgumentCaptor.forClass(boolean.class);

        verify(client, times(1))
                .publish(topic.capture(), payload.capture(), retain.capture());

        assertThat(topic.getValue(), is(equalTo("homeassistant/test/room-device-entity/config")));

        TestEntityConfig config = parseConfig(payload.getValue());
        DeviceConfig device = config.getDevice();
        assertThat(device.getName(), is(equalTo("room - device")));
        assertThat(device.getManufacturer(), is(equalTo("House")));
        assertThat(device.getModel(), is(equalTo("MQTT Adapter")));
        assertThat(device.getIdentifiers(), hasItem("room - device"));
        assertThat(device.getArea(), is(equalTo("room")));

        assertThat(config.getName(), is(equalTo("room - device - entity")));
        assertThat(config.getUniqueId(), is(equalTo("room-device-entity")));
        assertThat(config.getAvailabilityTopic(), is(equalTo("client/availability/topic")));
    }

    @Test
    public void testSpecifiedValues() {
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn("client/availability/topic").when(client).getAvailabilityTopic();
        TestEntity entity = new TestEntity(ENTITY_ID);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setName("MyDeviceName");
        deviceInfo.setManufacturer("MyManufacturer");
        deviceInfo.setModel("MyDeviceModel");
        deviceInfo.setSuggestedArea("MyRoom");
        entity.setDeviceInfo(deviceInfo);

        EntityOptions options = new EntityOptions();
        options.setIcon("power");
        entity.setOptions(options);

        new TestAnnouncer(client).announce(entity);

        ArgumentCaptor<String> topic = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> payload = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Boolean> retain = ArgumentCaptor.forClass(boolean.class);

        verify(client, times(1))
                .publish(topic.capture(), payload.capture(), retain.capture());

        assertThat(topic.getValue(), is(equalTo("homeassistant/test/room-device-entity/config")));

        TestEntityConfig config = parseConfig(payload.getValue());
        DeviceConfig device = config.getDevice();
        assertThat(device.getName(), is(equalTo("MyDeviceName")));
        assertThat(device.getManufacturer(), is(equalTo("MyManufacturer")));
        assertThat(device.getModel(), is(equalTo("MyDeviceModel")));
        assertThat(device.getIdentifiers(), hasItem("MyDeviceName"));
        assertThat(device.getArea(), is(equalTo("MyRoom")));

        assertThat(config.getName(), is(equalTo("room - device - entity")));
        assertThat(config.getIcon(), is(equalTo("hass:power")));
        assertThat(config.getUniqueId(), is(equalTo("room-device-entity")));
        assertThat(config.getAvailabilityTopic(), is(equalTo("client/availability/topic")));
    }

    protected TestEntityConfig parseConfig(byte[] value) {
        try {
            return new ObjectMapper().readValue(value, TestEntityConfig.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestEntity extends ConfigurableEntity<EntityOptions> {
        public TestEntity(EntityId id) {
            super(id, null);
        }
    }

    private static class TestEntityConfig extends BaseEntityConfig {
        @Override
        public String getEntityClass() {
            return "test";
        }
    }

    private static class TestAnnouncer extends BaseEntityAnnouncer<TestEntity> {
        public TestAnnouncer(HouseMqttClient mqtt) {
            super(mqtt, TestEntity.class);
        }

        @Override
        protected void announceEntity(TestEntity entity) {
            TestEntityConfig entityConfig = new TestEntityConfig();
            fillDefaults(entity, entityConfig);
            announceEntity(entityConfig);
        }
    }
}
