package net.morher.house.api.entity;

public class Entity {

    protected final EntityId id;
    protected final EntityListener entityListener;
    private DeviceInfo deviceInfo;

    public Entity(EntityId id, EntityListener entityListener) {
        this.id = id;
        this.entityListener = entityListener;
    }

    public EntityId getId() {
        return id;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        onEntityUpdated();
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    protected void onEntityUpdated() {
        if (entityListener != null) {
            entityListener.onEntityUpdated(this);
        }
    }
}