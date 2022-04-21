package net.morher.house.api.mqtt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import net.morher.house.api.device.DeviceId;
import net.morher.house.api.entity.EntityId;

public class DefaultMqttNamespaceTest {

    @Test
    public void testClientAvailabilityTopic() {
        assertThat(
                namespace().clientAvailabilityTopic("test-adapter"),
                is(equalTo("service/test-adapter/available")));
    }

    @Test
    public void testDeviceBaseTopic() {
        assertThat(
                namespace().getDeviceBaseTopic(new DeviceId("room", "device")),
                is(equalTo("house/room/device")));
    }

    @Test
    public void testEntityBaseTopic() {
        assertThat(
                namespace().entityBaseTopic(new EntityId(new DeviceId("room", "device"), null)),
                is(equalTo("house/room/device")));

        assertThat(
                namespace().entityBaseTopic(new EntityId(new DeviceId("room", "device"), "entity")),
                is(equalTo("house/room/device/entity")));
    }

    @Test
    public void testEntityStateTopic() {
        assertThat(
                namespace().entityStateTopic(new EntityId(new DeviceId("room", "device"), null)),
                is(equalTo("house/room/device")));

        assertThat(
                namespace().entityStateTopic(new EntityId(new DeviceId("room", "device"), "entity")),
                is(equalTo("house/room/device/entity")));
    }

    @Test
    public void testEntityCommandTopic() {
        assertThat(
                namespace().entityCommandTopic(new EntityId(new DeviceId("room", "device"), null)),
                is(equalTo("house/room/device/command")));

        assertThat(
                namespace().entityCommandTopic(new EntityId(new DeviceId("room", "device"), "entity")),
                is(equalTo("house/room/device/entity/command")));
    }

    protected DefaultMqttNamespace namespace() {
        return new DefaultMqttNamespace();
    }

}
