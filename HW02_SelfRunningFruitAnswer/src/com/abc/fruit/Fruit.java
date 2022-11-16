package com.abc.fruit;

public class Fruit  {

	private final String name;
	private  Fruit(String name) {
		this.name = name;
		Thread t = new Thread(() -> runWork());
		t.start();
	}
	
	public static Fruit createAndLaunch(String name) 
	{
		return new Fruit(name);
	}
	private void runWork()
	{
		try {
			for (int i = 0; i < 20; i++) {
				System.out.println(name);
				Thread.sleep(200);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
