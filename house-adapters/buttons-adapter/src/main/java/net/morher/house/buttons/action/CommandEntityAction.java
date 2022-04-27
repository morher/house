package net.morher.house.buttons.action;

import lombok.AllArgsConstructor;
import net.morher.house.api.entity.CommandableEntity;

@AllArgsConstructor
public class CommandEntityAction<C> implements Action {
    private final CommandableEntity<?, ?, C> entity;
    private final C modifiedState;

    @Override
    public void perform() {
        entity.sendCommand(modifiedState);
    }

}
