package net.morher.house.api.mqtt.client;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.test.schedule.TestHouseScheduler;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class HouseMqttClientTest {
  private final TestHouseScheduler scheduler = TestHouseScheduler.get();

  @Test
  public void testConstructorScheduleConnect() throws Exception {
    IMqttAsyncClient clientMock = mock(IMqttAsyncClient.class);
    doReturn(mock(IMqttToken.class)).when(clientMock).connect(any(MqttConnectOptions.class));
    doReturn(mock(IMqttDeliveryToken.class))
        .when(clientMock)
        .publish(anyString(), any(byte[].class), anyInt(), anyBoolean());
    TestClientFactory clientFactory = new TestClientFactory(clientMock);

    MqttOptions options = options();

    new HouseMqttClientImpl(options, MqttNamespace.defaultNamespace(), clientFactory);

    scheduler.skipAheadToLastRegisteredTaskAndRunAllWaiting();

    assertThat(clientFactory.getTimesCalled(), is(1));
    assertThat(clientFactory.getLastOptions(), is(equalTo(options)));

    ArgumentCaptor<MqttConnectOptions> connectOptionsCaptor =
        ArgumentCaptor.forClass(MqttConnectOptions.class);
    verify(clientMock, times(1)).connect(connectOptionsCaptor.capture());

    MqttConnectOptions connectOptions = connectOptionsCaptor.getValue();
    assertThat(connectOptions.getUserName(), is(equalTo("username")));
    assertThat(connectOptions.getPassword(), is(equalTo("password".toCharArray())));
    assertThat(connectOptions.isAutomaticReconnect(), is(false));
    assertThat(connectOptions.isCleanSession(), is(true));
  }

  @Test
  public void testSyncSubscriptionsOnConnect() throws Exception {
    IMqttAsyncClient clientMock = mock(IMqttAsyncClient.class);
    TestClientFactory clientFactory = new TestClientFactory(clientMock);

    doReturn(mock(IMqttToken.class)).when(clientMock).connect(any(MqttConnectOptions.class));
    doReturn(mock(IMqttDeliveryToken.class))
        .when(clientMock)
        .publish(anyString(), any(byte[].class), anyInt(), anyBoolean());
    doReturn(true).when(clientMock).isConnected();

    doThrow(new MqttException(0)).when(clientMock).connect(any(MqttConnectOptions.class));

    HouseMqttClient mqtt =
        new HouseMqttClientImpl(options(), MqttNamespace.defaultNamespace(), clientFactory);

    // Wait for failed attempt at connecting...
    scheduler.skipAheadToLastRegisteredTaskAndRunAllWaiting();

    MqttMessageListener listener = mock(MqttMessageListener.class);
    mqtt.subscribe("some/topic", listener);

    // Wait for connection...
    scheduler.runWaitingTasks();

    verify(clientMock, never()).subscribe(anyString(), anyInt());
    doReturn(mock(IMqttToken.class)).when(clientMock).connect(any(MqttConnectOptions.class));

    // Wait for subscriptionSync...
    scheduler.skipAhead(3, SECONDS).runWaitingTasks();
    verify(clientMock, times(1)).subscribe(eq("some/topic"), anyInt());
  }

  @Test
  public void testPublishOnConnect() throws Exception {
    IMqttAsyncClient clientMock = mock(IMqttAsyncClient.class);
    TestClientFactory clientFactory = new TestClientFactory(clientMock);

    doReturn(mock(IMqttToken.class)).when(clientMock).connect(any(MqttConnectOptions.class));
    doReturn(mock(IMqttDeliveryToken.class))
        .when(clientMock)
        .publish(anyString(), any(byte[].class), anyInt(), anyBoolean());
    doReturn(true).when(clientMock).isConnected();

    doThrow(new MqttException(0)).when(clientMock).connect(any(MqttConnectOptions.class));

    HouseMqttClient mqtt =
        new HouseMqttClientImpl(options(), MqttNamespace.defaultNamespace(), clientFactory);

    // Wait for failed attempt at connecting...
    scheduler.skipAheadToLastRegisteredTaskAndRunAllWaiting();

    mqtt.publish("some/topic", "Test message".getBytes(), false);

    verify(clientMock, never()).publish(anyString(), any(byte[].class), anyInt(), anyBoolean());
    doReturn(mock(IMqttToken.class)).when(clientMock).connect(any(MqttConnectOptions.class));

    // Wait for connection...
    scheduler.skipAheadToLastRegisteredTaskAndRunAllWaiting();

    // Wait for publishMessages
    scheduler.skipAheadToLastRegisteredTaskAndRunAllWaiting();
    verify(clientMock, times(1))
        .publish(eq("service/test-adapter/available"), eq("online".getBytes()), anyInt(), eq(true));
    verify(clientMock, times(1))
        .publish(eq("some/topic"), eq("Test message".getBytes()), anyInt(), eq(false));
  }

  protected MqttOptions options() {
    MqttOptions options = new MqttOptions();
    options.setClientId("test-adapter");
    options.setServerUrl("tcp://localhost:1883");
    options.setUsername("username");
    options.setPassword("password");
    options.setReconnectInterval(3);
    return options;
  }

  public static class TestClientFactory extends DefaultClientFactory {
    private final IMqttAsyncClient clientMock;
    private MqttOptions lastOptions;
    private int timesCalled;

    public TestClientFactory(IMqttAsyncClient clientMock) {
      this.clientMock = clientMock;
      reset();
    }

    public void reset() {
      this.lastOptions = null;
      this.timesCalled = 0;
    }

    public MqttOptions getLastOptions() {
      return lastOptions;
    }

    public int getTimesCalled() {
      return timesCalled;
    }

    @Override
    public IMqttAsyncClient connect(
        MqttOptions options, MqttAvailabilityPolicy availability, MqttCallback mqttCallback)
        throws MqttException {
      timesCalled++;
      return super.connect(options, availability, mqttCallback);
    }

    @Override
    protected IMqttAsyncClient createClient(MqttOptions options) throws MqttException {
      lastOptions = options;
      return clientMock;
    }
  }
}
