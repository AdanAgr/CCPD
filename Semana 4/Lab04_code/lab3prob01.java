class TickTock {
    String state = "toed";
    synchronized void tick (boolean running){
	if (!running) {   //stop clock
	    state="ticked";
	    notifyAll(); // notify any waiting threads
	    return;
	}

	try {
	    while(!state.equals("toed")){
		wait(); // wait for tock to complete
		}
	} catch (InterruptedException exc) {
	    System.out.println("interrupted");
	}
	System.out.print("Tick... ");
	state="ticked";
	notifyAll(); 
    }
    
    synchronized void tock (boolean running){
	if (!running) { //stop clock
	    state="tocked";
	    notifyAll(); // notify any waiting threads
	    return;
	}

	try {
	    while(!state.equals("ticked")){
		wait(); // wait for tock to complete
		}
	} catch (InterruptedException exc) {
	    System.out.println("interrupted");
	}
	System.out.print("Tock ");
	state="tocked";
	notifyAll(); 
    }
	synchronized void toe (boolean running){
	if (!running) { //stop clock
	    state="toed";
	    notifyAll(); // notify any waiting threads
	    return;
	}

	try {
	    while(!state.equals("tocked")){
		wait(); // wait for tock to complete
		}
	} catch (InterruptedException exc) {
	    System.out.println("interrupted");
	}
	System.out.println("Toe ");
	state="toed";
	notifyAll();
	}
}
class MyThread implements Runnable{
    Thread thrd;
    TickTock ttOb;
    
    // constructor of new thread
    MyThread(String name, TickTock tt){
	thrd = new Thread(this, name);
	ttOb = tt;
    }

    public void run() {
	if(thrd.getName().compareTo("Tick") == 0){
	    for (int i=0; i<=5; i++) ttOb.tick(true);
	    ttOb.tick(false);
	}else { if (thrd.getName().compareTo("Tock") == 0){
	    for (int i=0; i<=5; i++) ttOb.tock(true);
	    ttOb.tock(false);		
	} else {
	    for (int i=0; i<=5; i++) ttOb.toe(true);
	    ttOb.toe(false);
    }}
	}
public class lab3prob01{
    public static void main(String agrs[]){
	TickTock tt = new TickTock();
	
	MyThread mt1 = new MyThread("Tick", tt);
	MyThread mt2 = new MyThread("Tock", tt);	 
	MyThread mt3 = new MyThread("Toed", tt);
	
	mt1.thrd.start();
	mt2.thrd.start();
	mt3.thrd.start();
	
	try {
	    mt1.thrd.join();
	    mt2.thrd.join();
		mt3.thrd.join();
	} catch (InterruptedException exc){
	    System.out.println("main thread interrupted");
	}
    }
}}
