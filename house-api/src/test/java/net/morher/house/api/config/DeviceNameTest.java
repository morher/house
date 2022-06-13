package net.morher.house.api.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.EntityId;

public class DeviceNameTest {

    @Test
    public void testToDeviceId() {
        DeviceId deviceId = new DeviceName("room", "device").toDeviceId();

        assertThat(deviceId.getRoomName(), is(equalTo("room")));
        assertThat(deviceId.getDeviceName(), is(equalTo("device")));
    }

    @Test
    public void testToEntityIdDefaultName() {
        EntityId entityId = new DeviceName("room", "device").toEntityId("Power");
        DeviceId deviceId = entityId.getDevice();

        assertThat(deviceId.getRoomName(), is(equalTo("room")));
        assertThat(deviceId.getDeviceName(), is(equalTo("device")));
        assertThat(entityId.getEntity(), is(equalTo("Power")));
    }

    @Test
    public void testToEntityIdSpecifiedName() {
        EntityId entityId = new DeviceName("room", "device", "Entity").toEntityId("Power");
        DeviceId deviceId = entityId.getDevice();

        assertThat(deviceId.getRoomName(), is(equalTo("room")));
        assertThat(deviceId.getDeviceName(), is(equalTo("device")));
        assertThat(entityId.getEntity(), is(equalTo("Entity")));
    }
}
