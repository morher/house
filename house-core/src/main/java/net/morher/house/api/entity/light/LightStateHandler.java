package net.morher.house.api.entity.light;

import net.morher.house.api.entity.DeviceInfo;
import net.morher.house.api.entity.common.EntityStateListener;

public class LightStateHandler extends BaseStateHandler<LightState, LightOptions, LightState> {
  public LightStateHandler(LightEntity lightEntity, EntityStateListener<LightState> delegate) {
    super(lightEntity, delegate, LightState.defaultState());
  }

  public LightStateHandler(
      LightEntity lightEntity, DeviceInfo deviceInfo, EntityStateListener<LightState> delegate) {
    this(lightEntity, delegate);
    this.setDeviceInfo(deviceInfo);
  }

  @Override
  protected LightState modifyState(LightState currentState, LightState command) {
    return currentState.modify(command);
  }
}
