package net.morher.house.api.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HouseScheduler {

    public static ScheduledExecutorService get(String name) {
        return new ExceptionHandlingSchedulerWrapper(
                Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(name)),
                HouseScheduler::handleException);
    }

    private static void handleException(Throwable e, Object task) {
        log.error("Execption while performing task '{}':", task, e.getMessage(), e);
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)
                    ? s.getThreadGroup()
                    : Thread.currentThread().getThreadGroup();
            this.namePrefix = name + "-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}
