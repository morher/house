package net.morher.house.buttons.action;

import java.util.ArrayList;
import java.util.List;

public class AllConditions implements Condition {
    private final List<Condition> conditions = new ArrayList<>();

    public AllConditions add(Condition condition) {
        if (condition instanceof AllConditions) {
            conditions.addAll(((AllConditions) condition).conditions);
        } else {
            conditions.add(condition);
        }
        return this;
    }

    @Override
    public boolean isMatch() {
        for (Condition condition : conditions) {
            if (!condition.isMatch()) {
                return false;
            }
        }
        return true;
    }

    public Condition optimize() {
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        return this;
    }
}
