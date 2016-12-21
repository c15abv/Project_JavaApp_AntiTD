package utilities;

/**
 * A lock class.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class Lock{

	private boolean isLocked = false;
	
	public synchronized void lock() throws InterruptedException{
		while(isLocked){
			wait();
		}
		isLocked = true;
	}

	public synchronized void unlock(){
		isLocked = false;
		notify();
	}
}
