package com.abc.speaker;

public class Speaker {
    private static long nextUniqueId = 10_000L;

    private final long uniqueId;
    private final String[] wordsToSay;

    public Speaker(String[] wordsToSay) {
        this.wordsToSay = wordsToSay;
        uniqueId = getUniqueId();

        Thread t = new Thread(() -> runWork(), getClass().getSimpleName());
        t.start();
    }

    private static synchronized long getUniqueId() {
        long uniqueId = nextUniqueId;
        nextUniqueId++;
        return uniqueId;
    }

    private void runWork() {
        try {
            for (int i = 0; i < 10; i++) {
                String word = wordsToSay[(int) (Math.random() * wordsToSay.length)];
                System.out.println("uniqueId=" + uniqueId + ": " + word);
                Thread.sleep(200);
            }
        } catch ( InterruptedException x ) {
            // ignore, and let thread die
        }
    }
}
