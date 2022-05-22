package net.morher.house.api.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import net.morher.house.api.entity.DeviceId;

public class DeviceNameTest {

    @Test
    public void testToDeviceId() {
        DeviceId deviceId = new DeviceName("room", "device").toDeviceId();

        assertThat(deviceId.getRoomName(), is(equalTo("room")));
        assertThat(deviceId.getDeviceName(), is(equalTo("device")));
    }
}
