package net.morher.house.api.entity.switches;

import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.light.BaseStateHandler;

public class SwitchStateHandler extends BaseStateHandler<Boolean, SwitchOptions, Boolean> {

  public SwitchStateHandler(SwitchEntity entity, EntityStateListener<? super Boolean> delegate) {
    super(entity, delegate, false);
  }

  public SwitchStateHandler(
      SwitchEntity entity, DeviceInfo deviceInfo, EntityStateListener<? super Boolean> delegate) {
    this(entity, delegate);
    this.setDeviceInfo(deviceInfo);
  }

  @Override
  protected Boolean modifyState(Boolean currentState, Boolean command) {
    return command;
  }
}
