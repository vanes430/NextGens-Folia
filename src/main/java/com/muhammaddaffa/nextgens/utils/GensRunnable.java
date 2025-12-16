package com.muhammaddaffa.nextgens.utils;

import org.jetbrains.annotations.NotNull;

public abstract class GensRunnable implements Runnable {

    private Object handleTask;

    public synchronized void cancel() {
        this.checkHandleTask();
        FoliaHelper.cancel(this.handleTask);
    }

    private void checkHandleTask() {
        if (handleTask == null) {
            throw new IllegalStateException("Task not started");
        }
    }

    public synchronized void runTaskTimerAsynchronously(@NotNull Object plugin, long delay, long period) {
        this.isRunning();
        this.handleTask = FoliaHelper.runAsyncTimer(this, delay, period);
    }

    public synchronized void runTaskTimer(@NotNull Object plugin, long delay, long period) {
        this.isRunning();
        this.handleTask = FoliaHelper.runSyncTimer(this, delay, period);
    }

    public synchronized void isRunning() {
        if (handleTask != null) {
            throw new IllegalStateException("Task already running");
        }
    }

}
