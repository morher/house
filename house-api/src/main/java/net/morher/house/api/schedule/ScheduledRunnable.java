package net.morher.house.api.schedule;

public interface ScheduledRunnable {
    void runScheduled() throws Reschedule;
}
