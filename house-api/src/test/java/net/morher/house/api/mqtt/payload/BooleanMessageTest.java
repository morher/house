package net.morher.house.api.mqtt.payload;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class BooleanMessageTest {

    @Test
    public void testOnOff() {
        BooleanMessage converter = BooleanMessage.onOff();

        assertThat(converter.serialize(true), is(equalTo("ON".getBytes())));
        assertThat(converter.serialize(false), is(equalTo("OFF".getBytes())));

        assertThat(converter.deserialize("ON".getBytes()), is(true));
        assertThat(converter.deserialize("OFF".getBytes()), is(false));

        assertThat(converter.deserialize("true".getBytes()), is(true));
        assertThat(converter.deserialize("false".getBytes()), is(false));

        assertThat(converter.deserialize("TrUe".getBytes()), is(true));
        assertThat(converter.deserialize("FaLsE".getBytes()), is(false));

        assertThat(converter.deserialize("1".getBytes()), is(true));
        assertThat(converter.deserialize("0".getBytes()), is(false));
    }
}
