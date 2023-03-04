package net.morher.house.api.mqtt.payload;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class RawMessageTest {

  @Test
  public void testStringMessage() {
    PayloadFormat<String> format = RawMessage.toStr();
    assertThat(format.serialize("Some value"), is(equalTo("Some value".getBytes())));
    assertThat(format.deserialize("Another value".getBytes()), is(equalTo("Another value")));
  }
}
