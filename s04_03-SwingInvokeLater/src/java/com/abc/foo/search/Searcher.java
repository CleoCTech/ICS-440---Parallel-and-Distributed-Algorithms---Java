package com.abc.foo.search;

import com.abc.thread.*;

public class Searcher {
    private static long nextUniqueId;

    private final long uniqueId;
    private String searchCriteria;
    private ResultCallback resultCallback;

    public Searcher(String searchCriteria, ResultCallback resultCallback) {
        this.searchCriteria = searchCriteria;
        this.resultCallback = resultCallback != null ? resultCallback : ResultCallback.NO_OP;
        uniqueId = getNextUniqueId();

        Thread t = new Thread(() -> runWork(), getClass().getSimpleName() + "-" + uniqueId);
        t.start();
    }

    private static synchronized long getNextUniqueId() {
        return nextUniqueId++;
    }

    private void runWork() {
        try {
            ThreadTools.outln("Searching using criteria: " + searchCriteria);
            Thread.sleep(3000);
            String results = "found that criteria is " + searchCriteria.length() + " char long";
            ThreadTools.outln("Searching complete, results: " + results);
            resultCallback.searchComplete(results);
        } catch ( InterruptedException x ) {
            // ignore
        } finally {
            ThreadTools.outln(Thread.currentThread().getName() + " finished");
        }
    }

    public static interface ResultCallback {
        public static ResultCallback NO_OP = (results) -> {};

        void searchComplete(String results);
    }  // type ResultCallback
}
