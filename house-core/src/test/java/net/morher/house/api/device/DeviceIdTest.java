package net.morher.house.api.device;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import net.morher.house.api.entity.DeviceId;
import org.junit.Test;

public class DeviceIdTest {

  @Test
  public void testConstructor() {
    DeviceId id = new DeviceId("room", "device");
    assertThat(id, is(not(nullValue())));
    assertThat(id.getRoomName(), is(equalTo("room")));
    assertThat(id.getDeviceName(), is(equalTo("device")));
  }

  @Test
  public void testConstructorRoomNull() {
    DeviceId id = new DeviceId(null, "device");
    assertThat(id, is(not(nullValue())));
    assertThat(id.getRoomName(), is(nullValue()));
    assertThat(id.getDeviceName(), is(equalTo("device")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorRoomBlank() {
    new DeviceId("", "device");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDeviceNull() {
    DeviceId id = new DeviceId("room", null);
    assertThat(id, is(not(nullValue())));
    assertThat(id.getRoomName(), is(nullValue()));
    assertThat(id.getDeviceName(), is(equalTo("device")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDeviceBlank() {
    new DeviceId("room", "");
  }
}
