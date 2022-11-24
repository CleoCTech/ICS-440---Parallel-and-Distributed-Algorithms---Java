package com.abc.countdown;

import com.abc.thread.ThreadTools;

public class CountdownWorker {
	private static int nextId = 1;
    private final int totalSeconds;
    private final MinuteSecondDisplay display;
    private final CompletioncallBack completioncallBack;
    
    public CountdownWorker(int totalSeconds, 
    		MinuteSecondDisplay display, CompletioncallBack completioncallBack) {
        this.totalSeconds = totalSeconds;
        this.display = display;
        this.completioncallBack = completioncallBack != null ? completioncallBack : completioncallBack.DO_NOTHING;
        ThreadTools.outln("Worker Receives Total seconds  : " +totalSeconds);
        // do the self-run pattern to spawn a thread to do the counting down..=>Thread runworker()
        // ...
        String threadName = String.format("%s-%02d", getClass().getSimpleName(), getNextId());
        Thread t = new Thread( () -> runwork(), threadName);
		t.start();
        
    }
    public CountdownWorker(int totalSeconds, MinuteSecondDisplay display) {
//    	this(totalSeconds, display, (ms) -> {});
    	this(totalSeconds, display, null);
    }
    private static synchronized int getNextId() {
    	return nextId++;
    }
    
    // TODO - add methods to do the counting down and time correction....
    //        call: display.setSeconds(...
    private void runwork(){
    	long msStartTime = System.currentTimeMillis();
    	try {
    		long msNormalSleepTime = 1_000L;
    		for (int secondsElapsed = 0; secondsElapsed < totalSeconds; secondsElapsed++) {
    			display.setSeconds(totalSeconds - secondsElapsed);
    			
    			long msElapsedIdeal = secondsElapsed * 1_000L;
    			long msElapsedActual =  System.currentTimeMillis() - msStartTime;
    			long msBehind =  msElapsedActual - msElapsedIdeal;
    			long msNextSleepinTime =  msNormalSleepTime - msBehind;
    			ThreadTools.outln("msElapsedIdeal=%3d, msElapsedActual=%3d, msBehind=%3d, msNormalSleepTime=%3d", msElapsedIdeal, msElapsedActual, 
    					msBehind, msNextSleepinTime);
  			
    			if (msNextSleepinTime > 0) Thread.sleep(msNextSleepinTime);
    			
			}
    		display.setSeconds(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			long msElapsed = System.currentTimeMillis() - msStartTime;
			completioncallBack.countdownCompleted(msElapsed);
			ThreadTools.outln("finished in %d ms", msElapsed);
		}
	}
    
    public static interface CompletioncallBack {
    	public static final CompletioncallBack DO_NOTHING = new CompletioncallBack() {
			@Override
			public void countdownCompleted(long msActualElapsedTime) {
				//do nothing
			}
		};
    	void countdownCompleted(long msActualElapsedTime);
    }

}
