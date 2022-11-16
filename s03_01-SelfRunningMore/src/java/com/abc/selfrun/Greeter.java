package com.abc.selfrun;

import com.abc.thread.*;

public class Greeter {
    private String fruitName;
    private Thread internalThread;
    private volatile boolean keepGoing;

    public Greeter(String fruitName) {
        this.fruitName = fruitName;

        keepGoing = true;
        internalThread = new Thread(() -> runWork());
        internalThread.start();
    }

    private void runWork() {
        ThreadTools.outln("starting...");
        try {
            for (int i = 0; i < 5 && keepGoing; i++) {
                ThreadTools.outln("hello from %s, i=%d", fruitName, i);
                //Thread.sleep(500);
                //ThreadTools.busyStall(500);
                ThreadTools.interruptableBusyStall(500);
            }
            ThreadTools.outln("after the loop, keepGoing=%s", keepGoing);
        } catch (InterruptedException x) {
            ThreadTools.outln("interrupted while looping");
            ThreadTools.busyStall(400);
        } finally {
            ThreadTools.outln("finished");
        }
    }

    public void stopRequest() {
        keepGoing = false;
        internalThread.interrupt();
    }

    public void waitUntilStopped() throws InterruptedException {
        internalThread.join();
    }
}
