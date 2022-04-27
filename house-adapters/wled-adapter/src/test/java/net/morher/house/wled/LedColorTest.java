package net.morher.house.wled;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LedColorTest {

    @Test
    public void testConstructor() {
        LedColor ledColor = new LedColor("#0080FF");
        assertThat(ledColor.getRed(), is(equalTo(0)));
        assertThat(ledColor.getGreen(), is(equalTo(0x80)));
        assertThat(ledColor.getBlue(), is(equalTo(0xFF)));
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        assertThat(new ObjectMapper().writeValueAsString(new LedColor("#FFFFFF")), is(equalTo("\"#ffffff\"")));
        assertThat(new ObjectMapper().writeValueAsString(new LedColor("#FFF")), is(equalTo("\"#fff\"")));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        LedColor ledColor = new ObjectMapper().readValue("\"#102030\"", LedColor.class);
        assertThat(ledColor.getRed(), is(equalTo(0x10)));
        assertThat(ledColor.getGreen(), is(equalTo(0x20)));
        assertThat(ledColor.getBlue(), is(equalTo(0x30)));
    }
}
