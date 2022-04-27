package net.morher.house.buttons.action;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotCondition implements Condition {
    private final Condition delegate;

    @Override
    public boolean isMatch() {
        return !delegate.isMatch();
    }
}
