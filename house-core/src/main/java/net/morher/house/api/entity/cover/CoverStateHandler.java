package net.morher.house.api.entity.cover;

import net.morher.house.api.entity.common.EntityStateListener;
import net.morher.house.api.entity.light.BaseStateHandler;

public class CoverStateHandler extends BaseStateHandler<CoverState, CoverOptions, CoverCommand> {

  public CoverStateHandler(CoverEntity entity, EntityStateListener<? super CoverState> delegate) {
    super(entity, delegate, CoverState.STOPPED);
  }

  @Override
  protected CoverState modifyState(CoverState currentState, CoverCommand command) {
    if (CoverCommand.CLOSE.equals(command)) {
      return CoverState.CLOSED.equals(currentState) ? CoverState.CLOSED : CoverState.CLOSING;
    }
    if (CoverCommand.OPEN.equals(command)) {
      return CoverState.OPEN.equals(currentState) ? CoverState.OPEN : CoverState.OPENING;
    }
    return CoverState.STOPPED;
  }
}
