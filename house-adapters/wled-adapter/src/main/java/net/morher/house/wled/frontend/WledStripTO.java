package net.morher.house.wled.frontend;

import net.morher.house.api.entity.DeviceId;
import net.morher.house.wled.WledLedStrip;

public class WledStripTO {
    private String id;
    private String room;
    private String device;

    public WledStripTO(String id, String room, String device) {
        this.id = id;
        this.room = room;
        this.device = device;
    }

    public static WledStripTO from(WledLedStrip strip) {
        DeviceId deviceId = strip.getDeviceId();
        return new WledStripTO(strip.getId(), deviceId.getRoomName(), deviceId.getDeviceName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
