package com.abc.thread;

public class ThreadTools {
    private static final long nsStartTime = System.nanoTime();

    // no instances
    private ThreadTools() {
    }

    public static synchronized void outln(String fmt, Object... args) {
        double elapsedSeconds = (System.nanoTime() - nsStartTime) / 1e9;
        String msg = String.format(fmt, args);
        System.out.printf("%10.5f|%-15.15s|%s%n", elapsedSeconds, Thread.currentThread().getName(), msg);
    }
}
