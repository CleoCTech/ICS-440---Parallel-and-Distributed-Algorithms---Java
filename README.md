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
