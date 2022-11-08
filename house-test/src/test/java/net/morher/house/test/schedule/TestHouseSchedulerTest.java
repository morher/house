package net.morher.house.test.schedule;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.Test;
import org.mockito.Mockito;

public class TestHouseSchedulerTest {

    @Test
    public void testExecute() {
        TestHouseScheduler scheduler = new TestHouseScheduler();

        Runnable command = Mockito.mock(Runnable.class);

        // The command should not be run when added to queue.
        scheduler.execute(command);
        verifyNoInteractions(command);

        // Command should be called when running waiting tasks.
        scheduler.runWaitingTasks();
        verify(command).run();
    }

    @Test
    public void testWaitWithScheduledTask() {
        TestHouseScheduler scheduler = new TestHouseScheduler();

        Runnable command = Mockito.mock(Runnable.class);

        // The command should not be run when added to queue.
        scheduler.schedule(command, 10, SECONDS);
        verifyNoInteractions(command);

        // The command should not be called when running waiting tasks.
        scheduler.runWaitingTasks();
        verifyNoInteractions(command);

        // Should not run command right before scheduled
        scheduler.skipAhead(9, SECONDS).skipAhead(999, MILLISECONDS).runWaitingTasks();
        verifyNoInteractions(command);

        scheduler.skipAhead(1, MILLISECONDS);
        verifyNoInteractions(command);

        // Command should be called when running waiting tasks.
        scheduler.runWaitingTasks();
        verify(command).run();
    }

    @Test
    public void testSkipAheadOfScheduledTask() {
        TestHouseScheduler scheduler = new TestHouseScheduler();

        Runnable command = Mockito.mock(Runnable.class);

        // The command should not be run when added to queue.
        scheduler.schedule(command, 10, SECONDS);
        verifyNoInteractions(command);

        // The command should not be called when running waiting tasks.
        scheduler.runWaitingTasks();
        verifyNoInteractions(command);

        // When skipping ahead of a scheduled task, it should be run automatically.
        scheduler.skipAhead(1, MILLISECONDS).skipAhead(10, SECONDS);
        verify(command).run();
    }

}
