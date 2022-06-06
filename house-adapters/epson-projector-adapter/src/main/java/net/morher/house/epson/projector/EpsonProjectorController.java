package net.morher.house.epson.projector;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledExecutorService;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.api.entity.DeviceManager;
import net.morher.house.api.schedule.HouseScheduler;
import net.morher.house.epson.api.EscVpController;
import net.morher.house.epson.api.EscVpNetController;
import net.morher.house.epson.projector.config.EpsonConfig;
import net.morher.house.epson.projector.config.EpsonConfig.EpsonProjectorConfig;

public class EpsonProjectorController {
    private final ScheduledExecutorService scheduler = HouseScheduler.get("Epson Projector Operations");

    private final DeviceManager displays;

    public EpsonProjectorController(DeviceManager displays) {
        this.displays = displays;
    }

    public void configure(EpsonConfig config) {
        for (EpsonProjectorConfig projector : config.getProjectors()) {
            try {
                DeviceId deviceId = projector.getDevice().toDeviceId();
                EpsonProjectorDevice device = new EpsonProjectorDevice(displays.device(deviceId));
                EscVpController escVpController = new EscVpNetController(InetAddress.getByName(projector.getIp()));

                ProjectorManager projectorManager = new ProjectorManager(device, escVpController, scheduler);
                device.registerCommandListener(projectorManager);

            } catch (UnknownHostException e) {
                System.out.println("Failed to create projector manager for " + projector.getIp());
            }
        }
    }
}
