package com.abc.fruit;

public class Fruit extends Thread {

	private final String name;

    public Fruit(String name) {
        this.name = name;
    }

    @Override
    public void run() {
    	for (int i = 0; i <= 20; i++) {
            try {
                System.out.println(name);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
