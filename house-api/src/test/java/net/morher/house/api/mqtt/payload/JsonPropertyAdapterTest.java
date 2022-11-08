package net.morher.house.api.mqtt.payload;

import static net.morher.house.api.mqtt.payload.RawMessage.toStr;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

public class JsonPropertyAdapterTest {

    @Test
    public void serializationIsUnsupported() {
        @SuppressWarnings("unchecked")
        PayloadFormat<String> format = Mockito.mock(PayloadFormat.class);

        JsonPropertyAdapter<String> adapter = new JsonPropertyAdapter<>(format, "Switch1");

        try {
            adapter.serialize("Test");
            fail("Should throw UnsupportedOperationException");

        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), is(equalTo("Cannot serialize into a JSON property")));
        }

        verify(format, never()).serialize(anyString());
    }

    @Test
    public void testPropertyFound() {
        JsonPropertyAdapter<String> adapter = new JsonPropertyAdapter<>(toStr(), "Switch1.Action");

        String value = adapter.deserialize("""
                {
                  "Switch1": {
                    "Action": "ON"
                  },
                  "Switch2": {
                    "Action": "OFF"
                  }
                }
                """.getBytes());

        assertThat(value, is(equalTo("ON")));
    }

    @Test
    public void testMessageIsNotJsonObject() {
        JsonPropertyAdapter<String> adapter = new JsonPropertyAdapter<>(toStr(), "Switch2.Action");

        String value = adapter.deserialize("true".getBytes());

        assertThat(value, is(nullValue()));
    }

    @Test
    public void testPropertyNotFound() {
        JsonPropertyAdapter<String> adapter = new JsonPropertyAdapter<>(toStr(), "Switch3.Action");

        String value = adapter.deserialize("""
                {
                  "Switch1": {
                    "Action": "ON"
                  },
                  "Switch2": {
                    "Action": "OFF"
                  }
                }
                """.getBytes());

        assertThat(value, is(nullValue()));
    }

}
