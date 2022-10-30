package net.morher.house.api.schedule;

import lombok.Synchronized;

public class HouseSchedulerLocator {
    private static HouseScheduler scheduler;

    public static HouseScheduler get() {
        if (scheduler == null) {
            loadScheduler();
        }
        return scheduler;
    }

    @Synchronized
    private static void loadScheduler() {
        if (scheduler != null) {
            return;
        }

        // TODO: Look for test scope scheduler...

        scheduler = new DefaultHouseScheduler();
    }
}
