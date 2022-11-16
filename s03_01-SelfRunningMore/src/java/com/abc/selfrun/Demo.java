package com.abc.selfrun;

import com.abc.thread.*;

public class Demo {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        try {
            ThreadTools.outln("entering main method");

            new Greeter("apple");
            Greeter bananaGreeter = new Greeter("banana");
            new Greeter("cherry");

            long msSleepTime = 1_200;
            ThreadTools.outln("sleeping for %.1f seconds...", msSleepTime / 1_000.0);
            Thread.sleep(msSleepTime);
            ThreadTools.outln("doing bananaGreeter.stopRequest()...");
            bananaGreeter.stopRequest();
            ThreadTools.outln("back from bananaGreeter.stopRequest()");
            bananaGreeter.waitUntilStopped();
            ThreadTools.outln("bananaGreeter has stopped");
        } catch (InterruptedException x) {
            ThreadTools.outln("interrupted while sleeping");
        } finally {
            ThreadTools.outln("leaving main method");
        }
    }
}
