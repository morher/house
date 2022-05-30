package net.morher.house.miele.consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import net.morher.house.miele.consumer.auth.MieleTokenManager;
import net.morher.house.miele.domain.MieleDeviceInfo;

public class MieleConsumerImplTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule();

    @Test
    public void getDevices() throws Exception {
        byte[] responseBytes = getClass().getClassLoader().getResourceAsStream("sample-response.json").readAllBytes();

        stubFor(get("/devices")
                .willReturn(ok()
                        .withBody(responseBytes)));

        Map<String, MieleDeviceInfo> devices = consumer()
                .getDevices();

        assertThat(devices, is(not(nullValue())));
        assertThat(devices.size(), is(0));
    }

    private MieleConsumerImpl consumer() {
        return new MieleConsumerImpl("http://localhost:" + wireMock.port(), mock(MieleTokenManager.class));
    }
}
