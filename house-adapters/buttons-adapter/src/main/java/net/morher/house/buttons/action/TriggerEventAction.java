package net.morher.house.buttons.action;

import lombok.RequiredArgsConstructor;
import net.morher.house.api.entity.trigger.TriggerEntity;

@RequiredArgsConstructor
public class TriggerEventAction implements Action {
    private final TriggerEntity trigger;
    private final String event;

    @Override
    public void perform() {
        trigger.publishEvent(event);
    }

    @Override
    public void storePreEventState() {
        // Nothing to store...
    }

}
