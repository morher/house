package net.morher.house.buttons.action;

import java.util.ArrayList;
import java.util.List;

public class AnyCondition implements Condition {
    private final List<Condition> conditions = new ArrayList<>();

    public AnyCondition add(Condition condition) {
        if (condition instanceof AnyCondition) {
            conditions.addAll(((AnyCondition) condition).conditions);
        } else {
            conditions.add(condition);
        }
        return this;
    }

    @Override
    public boolean isMatch() {
        for (Condition condition : conditions) {
            if (condition.isMatch()) {
                return true;
            }
        }
        return false;
    }

    public Condition optimize() {
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        return this;
    }
}
