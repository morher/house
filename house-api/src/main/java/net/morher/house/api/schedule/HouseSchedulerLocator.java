package net.morher.house.api.schedule;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HouseSchedulerLocator {
    private static String SCHEDULER_TEST_IMPLEMENTATION = "net.morher.house.test.schedule.TestHouseScheduler";
    private static String SCHEDULER_IMPLEMENTATION = "net.morher.house.api.schedule.DefaultHouseScheduler";
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
        scheduler = loadImplementation(SCHEDULER_TEST_IMPLEMENTATION);
        if (scheduler == null) {
            scheduler = loadImplementation(SCHEDULER_IMPLEMENTATION);
        }
        if (scheduler == null) {
            throw new IllegalStateException("No implementation of HouseScheduler found");
        }
    }

    private static HouseScheduler loadImplementation(String implementationClass) {
        try {
            return (HouseScheduler) Class.forName(implementationClass).getConstructor().newInstance();

        } catch (ClassNotFoundException e) {
            log.trace("HouseScheduler implementation {} not found", implementationClass);
            return null;

        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate the HouseScheduler implementation " + implementationClass, e);
        }

    }
}
