package net.morher.house.api.utils.color;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class ColorTest {

  @Test
  public void testConstructor() {
    Color color = Color.fromHex("#0080FF");
    assertThat(color.getRed(), is(equalTo(0)));
    assertThat(color.getGreen(), is(equalTo(0x80)));
    assertThat(color.getBlue(), is(equalTo(0xFF)));
  }

  @Test
  public void testSerialize() throws JsonProcessingException {
    assertThat(
        new ObjectMapper().writeValueAsString(Color.fromHex("#FFFFFF")),
        is(equalTo("\"#ffffff\"")));
    assertThat(
        new ObjectMapper().writeValueAsString(Color.fromHex("#FFF")), is(equalTo("\"#ffffff\"")));
  }

  @Test
  public void testDeserialize() throws JsonProcessingException {
    Color color = new ObjectMapper().readValue("\"#102030\"", Color.class);
    assertThat(color.getRed(), is(equalTo(0x10)));
    assertThat(color.getGreen(), is(equalTo(0x20)));
    assertThat(color.getBlue(), is(equalTo(0x30)));
  }
}
