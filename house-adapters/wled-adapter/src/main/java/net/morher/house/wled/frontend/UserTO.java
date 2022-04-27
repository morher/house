package net.morher.house.wled.frontend;

import java.util.List;

public class UserTO {
    private String userId;
    private String name;
    private List<WledStripTO> ledStrips;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WledStripTO> getLedStrips() {
        return ledStrips;
    }

    public void setLedStrips(List<WledStripTO> ledStrips) {
        this.ledStrips = ledStrips;
    }
}
