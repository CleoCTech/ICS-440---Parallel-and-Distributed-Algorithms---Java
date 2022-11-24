# ICS-440: Parallel and Distributed Algorithms -[JAVA]
***

## S03 - Self-Running Objects and Threads Concurrency
*****************************************************
- Self run objects with internal thread on construction.

```

public class Fruit {
    public final String name;
    public Fruit(String name) {
        this.name = name;

        Thread t = new Thread(() -> runwork()));
        t.start();
    }

    private void runwork() {
        for(int i = 0; i <20; i++){
            System.println(name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

```
- Note that we are setting the method to `runwork()` private so that it cannot be accessed by other threads/objects. Ideally the method is supposed to be inside `new Thread()`.
- In other words, we are using Inner Class to Hide `run()`.
- Inside `runwork()` method, we are setting a thread to sleep 100milliseconds. But it requires one to catch InterruptedException. This is to say, you a can get interruption while sleeping. Why?
- Basically, when a thread is sleeping and interrupted by other thread, before it finishes, it will come back early and therefore will throw an InterruptedException.

**Self Running More**
- We are doing another example of self-running objects but a bit more sophisticated. 

- Be sure to check project `s03_01-SelfRunningMore`. 
- We can have more than on thread running in the same object, and sometimes we want to take control of how those threads will run. 
- Java has `Lock` mechanism but before we get there, we can use a volatile `boolean` value which will control as a swicther for a thread to run. 

```
import com.abc.thread.*;

public class Greeter {
    private String fruitName;
    private Thread internalThread;
    private volatile boolean keepGoing;
    
    //contructor
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
```
- Method `stopRequest()` flips that boolean `keepGoing` to `false` and interrups the internal thread. 

- Note that we have synchronized `ThreadTools` class,  the println message. This is to ensure only thread is inside the synchronized block at a time. So that the difference will be in the expected order. We understand that without synchronization, threads sometimes compete with other threads and late come thread can be write/excute before the early thread.

```
public static void outln(String fmt, Object... args) {
    String msg = String.format(fmt, args);
    synchronized (ThreadTools.class) {
        double secondsElapsed = (System.nanoTime() - nsStartTime) / 1e9;
        System.out.printf("%10.5f|%-12.12s|%s%n", secondsElapsed, Thread.currentThread().getName(), msg);
    }
}
```
- When we construct a new thread, we can set it to a daemon thread. 
```
internalThread =  new Thread(() ->runwork());
internalThread.setDaemon(true);
internalThread.start();
```
- This means, it will only run when all other non-daemon threads are running. If the only process/threads left are the daemon threads, then the process will exit.
- Daemon threads can be useful to keep virtual machines from running. 

**Some of the methods on :Thread;**
- `start()` - asynchronously requests that VM to spawn a new thread. 
- `run()` - is called by the newly spawned thread "sometime after `start()` is called". 
- `stop()` -causes the thread to cease execution. 
This method has been deprecated as of 1.2.
It's prone to leaving objects in an inconsitent state by suddenly releasing any locks that might be held (locks acquired via synchronized).
- `suspend()` - causes the thread to pause execution.
It is prone to deadlocks as is keeps holding any locks it has while suspended.

- `resume()` - causes the target thread to continue execution after being paused. Also it's deprecated.
- `interrupt()` - signals the target thread that it should take notice. -thread should clean up and die gracefully.
- `isAlive()` - checks the current state of a thread.
- `getName()` - returns the name of the thread (does not have to be unique).
- `setName(name)` - sets the name of the thread. Thread names can also be specified by passing a string to some of the constructors of a string. 

## Concurrent Access to objects and variables
- Things get tricky when more than one thread can interact with an object.
- Especially tricky when two threads might be concurrently inside the methods of the object.
- Care must be taken to be sure that one thread sees the other's changes.
- Care must be taken to be sure that one threads do not collide with another and leave the object in a dirty state (Invalid state).
- To solve this, we `synchronize` methods that can access common properties/variables. 
- We also have member variable modifier: `Volatile`.   A member variable can be declared as volatile to allow for multiple threads to see changes of it's value when `synchronization` isn't used.
- 
## S04 - Java Threads and Collections, Swing, Interface Implementation in synchronization
*****************************************************
- We start by doing assignment `HW03-CountdownTimer` you will find the answers in `HW03-CountdownTimerAnswer` directory.
- This was assignment instructions.
- Countdown Timer
  1. This app counts down from 10 seconds to zero, one second at a time.
  2. Only make changes to CountdownWorker.java
  3. On MinuteSecondDisplay, use this method to display the number of seconds left:
         public void setSeconds(int seconds)
  4. Sleep between calls to setSeconds, but be careful to adjust your sleep time
     to keep from drifting off.
  5. Use System.currentTimeMillis() to find out the currnet time in ms. Calculate
     the actual elapsed time and compare it to the expected elapsed time and
     adjust the next sleep time accordingly.
  6. After reaching 0, print the actual total elapsed time using 
     System.out.println(). It should be between 9960 ms and 10040 ms. If you get
     something more like 10500 ms, that is too far off.
        
- Let's have a look inside `CountdownWorker.java`
  ```
    package com.abc.countdown;


    public class CountdownWorker {
        private static int nextId = 1;
        private final int totalSeconds;
        private final MinuteSecondDisplay display;

        public CountdownWorker(int totalSeconds, MinuteSecondDisplay display) {
            this.totalSeconds = totalSeconds;
            this.display = display;

            // do the self-run pattern to spawn a thread to do the counting down...=>Thread runworker()
            // ...
            
        }
        
        // TODO - add methods to do the counting down and time correction....
        //        call: display.setSeconds(...

    }

  ```
  - We can start by printin out our `totalSeconds` once the start button is pressed to count.
  - We will bring in our `ThreadTools` package that we created earlier to help us print out the formated string.
  - When count down is initialized from button click :
  - We call the ` new CountdownWorker(10, display)` and pass the parameters required.
  ```
     public CountdownTimer() {
        display = new MinuteSecondDisplay();

        JButton startB = new JButton("Start");
        startB.addActionListener(new ActionListener() {
            @SuppressWarnings("unused")
            @Override
            public void actionPerformed(ActionEvent e) {
            	ThreadTools.outln("Start button pressed..");
                new CountdownWorker(10, display);
            }
        });

        setLayout(new FlowLayout());
        add(startB);
        add(display);
    }
  ```
  - Inside `CountdownWorker.java`, we can print out the total seconds using our thread tools method.
  ```
    public CountdownWorker(int totalSeconds, MinuteSecondDisplay display) {
        this.totalSeconds = totalSeconds;
        this.display = display;

        ThreadTools.outln("Worker Receives Total seconds  : " +totalSeconds);
        // do the self-run pattern to spawn a thread to do the counting down...=>Thread runworker()
        // ...
        
    }
  ```

  - Now we can create a self-run pattern to spawn a thread to do the counting down.
  - Below is the NestedSelfRun pattern example: 
  ```
    
    public static NestedSelfRun {

        public NestedSelfRun(){
            Thread t = new Thread( () -> runwork(), getClass().getSimpleName());
            t.start();
            
        }
        
        private void runwork(){
            try {
                System.out.println(Thread.currentThread().getName() +"starting");
                Thread.sleep(1_000L);
                System.out.println(Thread.currentThread().getName() +"from 1s sleeping");
            } catch (InterruptedException x) {
                // ignore
            } finally {
                System.out.println(Thread.currentThread().getName() +"finished");
            }
        }

    }

  ```
 - We need to implement this in our `CountdownWorker.java` 

    ```
    package com.abc.countdown;

    import com.abc.thread.ThreadTools;

    public class CountdownWorker {
        private static int nextId = 1;
        private final int totalSeconds;
        private final MinuteSecondDisplay display;

        public CountdownWorker(int totalSeconds, MinuteSecondDisplay display) {
            this.totalSeconds = totalSeconds;
            this.display = display;

             ThreadTools.outln("Worker Receives Total seconds  : " +totalSeconds);
            // do the self-run pattern to spawn a thread to do the counting down..=>Thread runworker()
            // ...
           
            String threadName = String.format("%s-%02d", getClass().getSimpleName(), getNextId());
            Thread t = new Thread( () -> runwork(), threadName);
            t.start();
            
        }
        private static int getNextId() {
            return nextId++;
        }
        private void runwork(){
            try {
                display.setSeconds(totalSeconds);
                Thread.sleep(1_000L);
                display.setSeconds(totalSeconds -1);
                Thread.sleep(1_000L);
                display.setSeconds(totalSeconds -2);
                Thread.sleep(1_000L);
                display.setSeconds(totalSeconds -3);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() +" finished");
            }
        }

        
        // TODO - add methods to do the counting down and time correction....
        //        call: display.setSeconds(...

    }

    ```
 - We have added a mechanism of naming our threads since we will be calling `CountdownWorker` more than once, to be specific thrice. We will need to differentiate threads by naming them with an incremental number concatination. 
 - You do understand by having more than one thread, we will have a thread race where every thread will be checking what is the current status of `nextId` value - which is our incremental value. It takes the value, increment it by 1 : `nextId++` and then leaves, another thread comes in. 
 - The `getNextId()` method will return the incremental value of `nextId`.  But this is a dangerous code. Why? Because thwo threads could be getting in there at the same time and get the same result which we don't want. 
 - What we need to do is to make it `static` and `synchronized`.  
  ```
    private static synchronized int getNextId() {
    	return nextId++;
    }
  ```
 - Note we are using `static`;
 - `static` methods require that the calling thread gain exclusive access to the 
    class-level lock before entering the method:
    ```
    public class ClassA //...
    //...
    public static synchronized void setValue(int value)
    //...
    }
    ```
 - for example, the thread executing this code:
    ```
    ClassA.setValue(5);
    ```
    must get exclusive access to the single class-level lock before the Java VM lets the 
    thread into setValue().

 - By making **static and synchronized** we are gurantees that we are not going to have any problems.

 - The next step is to add methods to do the **counting down** and **time correction**
 - We want to pass in a number of counts to `CountdownWorker` and inside runwork(), we will be counting down to zero 0, starting from the parameter passed. 
 - for example if 5 is passed, we will loop over the display 5 times as we subtract the loop count from the `totalSeconds` or number passed. 
 - In other words, we will use `for loop iteration` to count down. 
  ```
    private void runwork(){
    	try {
    		for (int secondsElapsed = 0; secondsElapsed < totalSeconds; secondsElapsed++) {
    			display.setSeconds(totalSeconds - secondsElapsed);
    			Thread.sleep(1_000L);
			}
    		display.setSeconds(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ThreadTools.outln("finished");
		}
	}
  ```
  **Output:** 
  ```
    0.00920|AWT-EventQueue-0  |Start button pressed..
    0.01650|AWT-EventQueue-0  |Worker Receives Total seconds  : 10
    0.01878|CountdownWorker-01|inside setSeconds(10)
    1.07968|CountdownWorker-01|inside setSeconds(9)
    2.17026|CountdownWorker-01|inside setSeconds(8)
    3.24156|CountdownWorker-01|inside setSeconds(7)
    4.30177|CountdownWorker-01|inside setSeconds(6)
    5.31471|CountdownWorker-01|inside setSeconds(5)
    6.34369|CountdownWorker-01|inside setSeconds(4)
    7.35479|CountdownWorker-01|inside setSeconds(3)
    8.38200|CountdownWorker-01|inside setSeconds(2)
    9.48683|CountdownWorker-01|inside setSeconds(1)
    10.58903|CountdownWorker-01|inside setSeconds(0)
    10.59060|CountdownWorker-01|finished

  ```
- The final timestamp of countdown is `10.59060` which is not accurate. That is 10 seconds to be exact. Maybe because of the JVM machine eats some time. 
- That why we need to do **time correction** 
- We start by printing out the actual elapsed time the worker takes to finish countdown by using system timestamp. 
  ```
   private void runwork(){
    	long msStartTime = System.currentTimeMillis();
    	try {
    		for (int secondsElapsed = 0; secondsElapsed < totalSeconds; secondsElapsed++) {
    			display.setSeconds(totalSeconds - secondsElapsed);
    			Thread.sleep(1_000L);
			}
    		display.setSeconds(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ThreadTools.outln("finished in %d ms", System.currentTimeMillis() - msStartTime);
		}
	}
  ```
- **Output**
  ```
   0.00877|AWT-EventQueue-0  |Start button pressed..
   0.01501|AWT-EventQueue-0  |Worker Receives Total seconds  : 10
   0.01709|CountdownWorker-01|inside setSeconds(10)
   1.07756|CountdownWorker-01|inside setSeconds(9)
   2.14963|CountdownWorker-01|inside setSeconds(8)
   3.19104|CountdownWorker-01|inside setSeconds(7)
   4.23518|CountdownWorker-01|inside setSeconds(6)
   5.27693|CountdownWorker-01|inside setSeconds(5)
   6.37103|CountdownWorker-01|inside setSeconds(4)
   7.43750|CountdownWorker-01|inside setSeconds(3)
   8.49555|CountdownWorker-01|inside setSeconds(2)
   9.55204|CountdownWorker-01|inside setSeconds(1)
  10.65315|CountdownWorker-01|inside setSeconds(0)
  10.65498|CountdownWorker-01|finished in 10638 ms
  ```
- It finished at `10638` ms. Which is not an expected outcome from our instructions. 
  - *After reaching 0, print the actual total elapsed time using 
   System.out.println(). It should be between 9960 ms and 10040 ms. If you get
   something more like 10500 ms, that is too far off.* 
- We can tell that we are too far off indeed. Therefore we need to find a way to adjust the time we take to loop one count to the other.
- After a couple tries of trying to adjust the elapsed time, this was the output.
  ```
   0.01038|AWT-EventQueue-0  |Start button pressed..
   0.02065|AWT-EventQueue-0  |Worker Receives Total seconds  : 10
   0.05724|CountdownWorker-01|inside setSeconds(10)
   0.07967|CountdownWorker-01|msElapsedIdeal=  0, msElapsedActual= 22, msBehind= 22, msNormalSleepTime=978
   1.05897|CountdownWorker-01|inside setSeconds(9)
   1.07714|CountdownWorker-01|msElapsedIdeal=1000, msElapsedActual=1020, msBehind= 20, msNormalSleepTime=980
   2.06906|CountdownWorker-01|inside setSeconds(8)
   2.12228|CountdownWorker-01|msElapsedIdeal=2000, msElapsedActual=2065, msBehind= 65, msNormalSleepTime=935
   3.05826|CountdownWorker-01|inside setSeconds(7)
   3.08255|CountdownWorker-01|msElapsedIdeal=3000, msElapsedActual=3025, msBehind= 25, msNormalSleepTime=975
   4.05919|CountdownWorker-01|inside setSeconds(6)
   4.12878|CountdownWorker-01|msElapsedIdeal=4000, msElapsedActual=4072, msBehind= 72, msNormalSleepTime=928
   5.05759|CountdownWorker-01|inside setSeconds(5)
   5.11586|CountdownWorker-01|msElapsedIdeal=5000, msElapsedActual=5059, msBehind= 59, msNormalSleepTime=941
   6.05845|CountdownWorker-01|inside setSeconds(4)
   6.10527|CountdownWorker-01|msElapsedIdeal=6000, msElapsedActual=6048, msBehind= 48, msNormalSleepTime=952
   7.05894|CountdownWorker-01|inside setSeconds(3)
   7.15840|CountdownWorker-01|msElapsedIdeal=7000, msElapsedActual=7101, msBehind=101, msNormalSleepTime=899
   8.05763|CountdownWorker-01|inside setSeconds(2)
   8.09270|CountdownWorker-01|msElapsedIdeal=8000, msElapsedActual=8036, msBehind= 36, msNormalSleepTime=964
   9.06448|CountdownWorker-01|inside setSeconds(1)
   9.11006|CountdownWorker-01|msElapsedIdeal=9000, msElapsedActual=9053, msBehind= 53, msNormalSleepTime=947
  10.05870|CountdownWorker-01|inside setSeconds(0)
  10.05972|CountdownWorker-01|finished in 10003 ms
  16.65644|AWT-EventQueue-0  |Start button pressed..
  16.65890|AWT-EventQueue-0  |Worker Receives Total seconds  : 10
  16.66027|CountdownWorker-02|inside setSeconds(10)
  16.69255|CountdownWorker-02|msElapsedIdeal=  0, msElapsedActual= 32, msBehind= 32, msNormalSleepTime=968
  17.66114|CountdownWorker-02|inside setSeconds(9)
  17.69728|CountdownWorker-02|msElapsedIdeal=1000, msElapsedActual=1037, msBehind= 37, msNormalSleepTime=963
  18.66063|CountdownWorker-02|inside setSeconds(8)
  18.68950|CountdownWorker-02|msElapsedIdeal=2000, msElapsedActual=2028, msBehind= 28, msNormalSleepTime=972
  19.66304|CountdownWorker-02|inside setSeconds(7)
  19.74733|CountdownWorker-02|msElapsedIdeal=3000, msElapsedActual=3087, msBehind= 87, msNormalSleepTime=913
  20.66130|CountdownWorker-02|inside setSeconds(6)
  20.72636|CountdownWorker-02|msElapsedIdeal=4000, msElapsedActual=4066, msBehind= 66, msNormalSleepTime=934
  21.66133|CountdownWorker-02|inside setSeconds(5)
  21.68483|CountdownWorker-02|msElapsedIdeal=5000, msElapsedActual=5024, msBehind= 24, msNormalSleepTime=976
  22.66231|CountdownWorker-02|inside setSeconds(4)
  22.67129|CountdownWorker-02|msElapsedIdeal=6000, msElapsedActual=6011, msBehind= 11, msNormalSleepTime=989
  23.66038|CountdownWorker-02|inside setSeconds(3)
  23.68986|CountdownWorker-02|msElapsedIdeal=7000, msElapsedActual=7030, msBehind= 30, msNormalSleepTime=970
  24.66194|CountdownWorker-02|inside setSeconds(2)
  24.71504|CountdownWorker-02|msElapsedIdeal=8000, msElapsedActual=8055, msBehind= 55, msNormalSleepTime=945
  25.66122|CountdownWorker-02|inside setSeconds(1)
  25.69368|CountdownWorker-02|msElapsedIdeal=9000, msElapsedActual=9033, msBehind= 33, msNormalSleepTime=967
  26.66154|CountdownWorker-02|inside setSeconds(0)
  26.66235|CountdownWorker-02|finished in 10001 ms
  ```

  - Well now we can try to jump into Swing Utilities and try to `disable` a button right after we press start, and we get to set a callback to in `CounterdownWorker` to tell UI when the counting is done we `Enable` the button again. 
  - Inside `CounterdownWorker`, let's create a [`nested static class`]() ->`CompletioncallBack` , which will tell us once the counting is done. Works API callbacks. 
    - *A static inner class is a nested class which is a static member of the outer class. It can be accessed without instantiating the outer class, using other static members*
    - *A callback is an asynchronous API request that originates from the API server and is sent to the client in response to an earlier request sent by that client. APIs can use callbacks to signal an event of interest and share data related to that event.*
  - This call will be a type `interface` with one method for now, which will accept one argument. 
    ```
    public static interface CompletioncallBack {
        void countdownComoleted(long msActualElapsedTime);
    }
    ```
  - So, the `CompletioncallBack` is going to be called by this `CounterdownWorker` thread and tell it the countdown is done and this was the actual elapsed time incase you care.
  - The next is to create a second constructor of `CounterdownWorker`, which accepts two arguments and the other accepts all three arguments.
  - When we create an object of this class `CounterdownWorker`, if we pass two arguments, it will go to a second constructor which accepts two arguments. And if we pass three agruments the our object, it will instatiate the constructor which accepts 3 argments only. 
  
    ```
        //onstructor which accepts all 3 arguments
        public CountdownWorker(int totalSeconds, MinuteSecondDisplay display,CompletioncallBack completioncallBack) {

        }
        //constructor which accepts 2 arguments
        public CountdownWorker(int totalSeconds, MinuteSecondDisplay display) {
            this(totalSeconds, display, (ms) -> {});
        }

    ```
 - In our start button, where we call `CountdownWorker`, we can do this:
  
  ```
    JButton startB = new JButton("Start");
    startB.addActionListener(new ActionListener() {
        @SuppressWarnings("unused")
        @Override
        public void actionPerformed(ActionEvent e) {
            ThreadTools.outln("Start button pressed..");
            startB.setEnabled(false);
            new CountdownWorker(10, display, (ms) -> {
                ThreadTools.outln("Countdown Complete");
            });
        }
    });
  ```
  - `CountdownWorker` does not know anything about `Countdowntimer`, but we are giving it a callback : `CompletioncallBack` so that it can callback on us[CountdownWorker].
  - It's like saying, whoever i may work for, i don't know you, mine is just to work and leave...but if you want to tell me something, here is a specific gate, a number that you can callback before we close contract.
  - From the code block above, we are utilizing callback `ms` to send a message `Countdown Complete` to `CountdownWorker`
  - c
  - When doing adjustment in UI, one is required to invoke a different thread for SwingUtilities which will be responsible in updating the UI when all AWT is ready. 
  ```
    private void kickOffCountdown()
    {
    	ThreadTools.outln("entering kickOffCountdown");
    	setStartButtonEnabled(false);
        new CountdownWorker(10, display, (msActual) -> {
        	ThreadTools.outln("Countdown Complete, %d ms", msActual);
        	setStartButtonEnabled(true);
        });
        ThreadTools.outln("leaving kickOffCountdown");
    }
    
    private void setStartButtonEnabled(boolean enabled) {
    	ThreadTools.outln("setStartButtonEnabled (%b)", enabled);
    	
    	if (!SwingUtilities.isEventDispatchThread()) {
    		SwingUtilities.invokeLater(() ->setStartButtonEnabled(enabled));
    		return;
		} 
    	ThreadTools.outln("about to call startB.setEnabled (%b)", enabled);
    	startB.setEnabled(enabled);
    }
  ```
  - This introduces us to a new related topic ...
  **Threads and Swing**
  - See Chapter 9 – "Threads and Swing" of "Java Thread Programming".
    - Swing was designed for speed and Swing components do not safely support access 
    by any thread other than the "event handling" thread after they have been rendered. 
    - This helped to keep Swing simpler and speeds runtime execution.
    - Swing does provide a mechanism for sending work units to the event thread for safe 
    component modification.
    - These two `static` methods on `SwingUtilities` allow for work units to be handed off
    to the event thread:
        ```
        public static void invokeAndWait(Runnable synchronousTask)
        throws InterruptedExcption, InvocationTargetException
        public static void invokeLater(Runnable asynchronousTask)
        ```
    - the `SwingUtilities.invokeLater()` method is generally preferred, `invokeAndWait` can cause deadlock.

 **Holding Lock Bewtween Method Calls**
 - Sometimes we need to sequentially invoke two synchronized methods on a object and want to be sure that another thread can't sneak in between those calls
 - Example:
  ```
  public class ClassB extends Object {
    private double value;
    public synchronized double getValue() {
        return value;
    }
    public synchronized void setValue(double x) {
        value = x;
    }
  }
  ```
  and we have a thread executing this code:
  ```
    ClassB b = //..
    if ( b.getValue() < 10.0 ) {
        b.setValue(50.0);
    }

  ```
 - Then there is a small chance that after we return from `getValue()` but before we call `setValue()`, another thread will sneak in and alter the value!.
 - To eliminate this race condition, we need to use a synchronized block so that the calling thread holds the lock the whole time that it is inside the block—not just while inside the methods:
  ```
  ClassB b = //..
    synchronized ( b ) {
    if ( b.getValue() < 10.0 ) {
    b.setValue(50.0);
    }
  }

  ```
 **Threads and Collections**
 - See material starting on page 155 of "Java Thread Programming".
 - The Collections API is not inherently thread-safe.
 - For speed, the default implementations do not use synchronized methods to control concurrent access. For example:
    ```
    List list = new ArrayList(); // not multithread-safe
    ```
    - If multiple threads interact with list, there are dangerous race conditions. If only one thread interacts, then we're perfectly safe and benefit by not having the overhead of invoking synchronized methods.
  
 - If a collection is going to be accessed/modified by multiple threads, it needs to be wrapped in synchronization:
    ```
    public class Collections //...
        public static Collection synchronizedCollection(Collection raw) //...
        public static List synchronizedList(List raw) //...
        public static Map synchronizedMap(Map raw) //...
        public static Set synchronizedSet(Set raw) //...
        public static SortedMap synchronizedSortedMap(SortedMap raw) //...
        public static SortedSet synchronizedSortedSet(SortedSet raw) //...
        //...
    }
    ```
 - From the above example/code block. We can treat the class `Collection` as a tool, just like when we made our `ThreadTools` for printout formated string.
 - This `CollectionTool` has only static methods, if you give it a collection, it synchronizes the collection and gives you back a synchronized collection.
 - If you give it a `List`, it synchronizes and gives you back a synchronized list.
 - How? It wraps that `list` with it's decorated designed pattern. Which wraps all three calls unsynchronized.
 - For example:
    ```
    List list = Collections.synchronizedList(new ArrayList()); // safe
    ```
    allows list to be safely accessed by multiple threads. A new instance of a class 
that implements the List interface with all synchronized methods is returned from 
this method. Internally, the synchronized method turns and calls the unsynchronized 
method on the backing List—in this case the underlying ArrayList. You should not 
allow direct access to the underlying ArrayList.

- You can check directory `s04_02-FakeCollections` to see how an `interface` is implemented in two different ways to modify the `collection` .
  ```
  public interface StringList {
    int count();
    String getAtIndex(int index);
    void append(String s);
  }

  ```
- Let's have a look inside `SyncStringList.java`
  ```
    // Decorator .... SyncStringList IS-A StringList and HAS-A StringList
    public class SyncStringList implements StringList {
        private final StringList rawList;

        public SyncStringList(StringList rawList) {
            this.rawList = rawList;
        }

        @Override
        public synchronized int count() {
            return rawList.count();
        }

        @Override
        public synchronized String getAtIndex(int index) {
            return rawList.getAtIndex(index);
        }

        @Override
        public synchronized void append(String s) {
            rawList.append(s);
        }
    }

  ```
**Java's Wait-Notify Mechanism**
- One thread efficiently signals another (or even several others).
  - One thread waits (using virtually zero CPU)
  - Another thread notifies the waiting thread
- Waiting is similar to the sleeping discussed earlier except that a lock is released 
(more on this later) and a notification results is a shortened "sleep" time.
- Wait-notify is supported by methods on the class Object:
```
public final void wait() throws InterruptedException,
 IllegalMonitorStateException

public final void notify() throws IllegalMonitorStateException
```
- If a thread is interrupted while waiting, it will throw an InterruptedException (much 
like Thread.sleep() does).
- In order to call wait() or notify() on an object, the calling thread must be holding 
the object-level lock on that object.

 ```
 Object obj = //...
obj.wait();
throws an IllegalMonitorStateException because the lock is not held.

obj.notify();
throws an IllegalMonitorStateException because the lock is not held.

 ```
 - We rarely bother to catch IllegalMonitorStateException because it's a runtime 
exception and is only thrown when there is a bug in the code.
- The requirement that the lock is held before calling these methods ensures that no 
race conditions can occur. This now works:

    ```
    Object obj = //...
    synchronized ( obj ) {
        obj.wait();
    }
    //...
    synchronized ( obj ) {
        obj.notify();
    }

    ```
- It turns out that wait() actually releases the object-level lock while the calling thread 
is inside the wait() method.
- This release is necessary to allow the thread calling notify() to be able to acquire 
the lock while the other waiting thread is blocked inside wait().
- Calls to notify() do not release the lock.
- Also, unlike wait() calls to Thread.sleep() do not release the lock.