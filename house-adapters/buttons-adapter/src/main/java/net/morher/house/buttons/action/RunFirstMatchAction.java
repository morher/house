package net.morher.house.buttons.action;

import java.util.ArrayList;
import java.util.List;

public class RunFirstMatchAction implements Action {
    private final List<ConditionalAction> alternatives = new ArrayList<>();

    @Override
    public void perform() {
        for (ConditionalAction alternative : alternatives) {
            Condition condition = alternative.getCondition();
            if (condition == null || condition.isMatch()) {
                alternative.getAction().perform();
                break;
            }
        }
    }

    public void addAlternative(Action action, Condition condition) {
        alternatives.add(new ConditionalAction(condition, action));
    }
}
