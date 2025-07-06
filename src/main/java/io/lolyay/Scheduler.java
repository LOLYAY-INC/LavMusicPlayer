package io.lolyay;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private final ScheduledExecutorService schedulerpool = Executors.newScheduledThreadPool(12);


    public ScheduledFuture<?> startScheduledTask(Runnable task, int period, TimeUnit unit) {
        //  ScheduledTask task = new ta();
        // Schedule the task to run every 5 seconds (fixed rate)
        return schedulerpool.scheduleAtFixedRate(task, 0, period, unit);
    }

    public ScheduledFuture<?> startDelayedTask(Runnable task, long delay, TimeUnit unit) {
        //  ScheduledTask task = new ta();
        // Schedule the task to run once in 5 seconds
        return schedulerpool.schedule(task, delay, unit);
    }

    public void shutdown() {
        schedulerpool.shutdown();
        try {
            if (!schedulerpool.awaitTermination(5, TimeUnit.SECONDS)) {
                schedulerpool.shutdownNow();
            }
        } catch (InterruptedException e) {
            schedulerpool.shutdownNow();
        }
    }
}