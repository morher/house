package net.morher.house.modes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;

import net.morher.house.api.config.DeviceName;
import net.morher.house.api.device.DeviceId;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.EntityManager;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.api.mqtt.MqttNamespace;
import net.morher.house.api.mqtt.client.HouseMqttClient;
import net.morher.house.api.mqtt.client.MqttMessageListener;
import net.morher.house.modes.ModesAdapterConfiguration.ModeDeviceConfiguration;
import net.morher.house.modes.ModesAdapterConfiguration.ModeEntityConfiguration;
import net.morher.house.modes.ModesAdapterConfiguration.ModesConfiguration;

public class ModesControllerTest {

    private static final DeviceId DEVICE_ID = new DeviceId("room", "device");
    private static final EntityId ENTITY_ID_MAIN = new EntityId(DEVICE_ID, null);

    @Test
    public void testCreateDeviceWithSwitchAsMain() {
        EntityManager entityManager = mock(EntityManager.class);
        HouseMqttClient client = mock(HouseMqttClient.class);
        doReturn(MqttNamespace.defaultNamespace()).when(client).getNamespace();

        SwitchEntity switchEntity = new SwitchEntity(client, ENTITY_ID_MAIN, null);

        doReturn(switchEntity).when(entityManager).switchEntity(eq(ENTITY_ID_MAIN));

        ModesController controller = new ModesController(entityManager);
        ModesConfiguration config = new ModesConfiguration();
        ModeDeviceConfiguration devConfig = new ModeDeviceConfiguration();
        devConfig.setDevice(new DeviceName("room", "device"));
        ModeEntityConfiguration entityConfig = new ModeEntityConfiguration();
        entityConfig.setType("switch");
        devConfig.setMainEntity(entityConfig);
        config.getDevices().add(devConfig);

        controller.configure(config);

        verify(entityManager).switchEntity(eq(ENTITY_ID_MAIN));
        verify(client, atLeastOnce()).getNamespace();
        verify(client).subscribe(eq("house/room/device"), any(MqttMessageListener.class));
        verify(client, times(1)).subscribe(eq("house/room/device/command"), any(MqttMessageListener.class));
        verifyNoMoreInteractions(client);
    }
}
