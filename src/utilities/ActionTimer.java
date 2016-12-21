package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The ActionTimer is used to time specific event.
 * An object may set a new timer for an object (preferably
 * itself) whom implements the TimerListener interface.
 * When the timer for a certain object has ended the
 * ActionTimer calls that object's implemented receiveNotification
 * method with the id it was set with.<br>
 * When choosing an id upon setting a new timer it is strongly
 * recommended using the getNewUniqueId method in ActionTimer.
 * 
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class ActionTimer implements Runnable{
	
	/**
	 * A class used to hold the object with
	 * the corresponding time used to set a timer.
	 * 
	 * @author Alexander Beliaev
	 */
	class TimedObject<T extends TimerListener>{
		private volatile T object;
		private volatile long time, timeC;
		
		TimedObject(T object, long time){
			this.object = object;
			this.time = time;
			timeC = System.nanoTime();
		}
		
		T getTimedObject(){
			return object;
		}
		
		long getTime(){
			return time;
		}
		
		long getTimeC(){
			return timeC;
		}
	}
	
	private volatile IdCounter idCounter;
	private HashMap<Long, TimedObject<?>> activeTimers;
	private volatile boolean isRunning;
	private volatile boolean paused;
	private volatile long timeCreated, timeAtPaused, timeLost;
	private Lock lock;
	
	/**
	 * Creates a new ActionTimer instance.
	 */
	public ActionTimer(){
		idCounter = new IdCounter();
		activeTimers = new HashMap<Long, TimedObject<?>>();
		isRunning = true;
		paused = false;
		lock = new Lock();
		timeCreated = timeAtPaused = timeLost = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		timeCreated = System.nanoTime();
		HashMap<Long, TimedObject<?>> activeTimersCopy;
		ArrayList<Long> expired;
		
		while(isRunning){
			if(!paused){
				activeTimersCopy = null;
				expired = new ArrayList<Long>();
				try{
					lock.lock();
					activeTimersCopy =
							new HashMap<Long, TimedObject<?>>(activeTimers);
				}catch(InterruptedException e){
				}finally{
					lock.unlock();
				}
				if(activeTimersCopy != null){
					Iterator<Entry<Long, TimedObject<?>>> it = 
						activeTimersCopy.entrySet().iterator();
					Map.Entry<Long, TimedObject<?>> pair;
				    while(it.hasNext()){
				        pair = (Map.Entry<Long, TimedObject<?>>)it.next();
				        if(pair.getValue().getTime() <= getCurrentTime()){
				        	pair.getValue().getTimedObject()
				        			.receiveNotification(pair.getKey());
				        	expired.add(pair.getKey());
				        }
				    }
				}
				try{
					lock.lock();
					for(Long id : expired){
						activeTimers.remove(id);
					}
				}catch(InterruptedException e){
				}finally{
					lock.unlock();
				}
			}
		}
	}
	
	/**
	 * Sets a new timer for the specified object. The object must
	 * implement the TimerListener interface.
	 * @param id an id
	 * @param object the object
	 * @param delta time in milliseconds
	 */
	public synchronized <T extends TimerListener> void setTimer(long id, 
			T object, long delta){
		try{
			lock.lock();
			activeTimers.put(id, new TimedObject<T>(object, (long)(delta * 10e5) 
					+ getCurrentTime()));
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	
	/**
	 * Removes the a timer with the specified id.
	 * 
	 * @param id the id.
	 */
	public synchronized void removeTimer(Long id){
		try{
			lock.lock();
			activeTimers.remove(id);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Returns the time left of a timer with the 
	 * specified id.
	 * 
	 * @param id the id.
	 * @return the time left for a specific timer.
	 */
	public synchronized long timeLeft(Long id){
		long timeLeft = -1;
		TimedObject<?> obj = null;
		
		try{
			lock.lock();
			obj = activeTimers.get(id);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		
		if(obj != null){
			if(obj.getTime() - getCurrentTime() < 0){
				timeLeft = 0;
			}else{
				timeLeft = (long) (obj.getTime() - getCurrentTime());
			}
		}
		
		return timeLeft;
	}
	
	
	/**
	 * Returns the time elapsed since the start call to
	 * the thread of this ActionTimer.
	 * 
	 * @return the time elapsed.
	 */
	public synchronized long timeElapsed(){
		return paused ? timeAtPaused - timeCreated : 
			getCurrentTime() - timeCreated;
	}
	
	/**
	 * Returns the time elapsed for a specific timer.
	 * 
	 * @param id the id.
	 * @return the elapsed time.
	 */
	public synchronized long timeElapsed(Long id){
		long timeElapsed = -1;
		TimedObject<?> obj = null;
		
		try{
			lock.lock();
			obj = activeTimers.get(id);
			
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		
		if(obj != null){
			if(obj.getTime() - getCurrentTime() < 0){
				timeElapsed = getCurrentTime() - obj.getTimeC();
			}else{
				timeElapsed = (long) (obj.getTime() - obj.getTimeC());
			}
		}
		
		return timeElapsed;
	}
	
	/**
	 * Returns a new unique id which could be used
	 * to set a new timer in this instance of ActionTimer
	 * without running the risk of writing over an already
	 * existing timer for the same object.
	 * 
	 * @return a new unique id.
	 */
	public synchronized long getNewUniqueId(){
		long id = 0;
		try{
			lock.lock();
			id = idCounter.getId();
			idCounter.increment();
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		idCounter.increment();
		return id;
	}
	
	/**
	 * Terminates the ActionTimer loop.
	 */
	public synchronized void terminate(){
		isRunning = false;
	}
	
	/**
	 * Pauses the ActionTimer.
	 */
	public synchronized void pause(){
		if(!paused)
			timeAtPaused = System.nanoTime();
		paused = true;
	}
	
	/**
	 * Resumes the ActionTimer.
	 */
	public synchronized void resume(){
		if(paused)
			timeLost += (System.nanoTime() - timeAtPaused);
		paused = false;
	}
	
	//Translates the current system time to the actiontimer time.
	private long getCurrentTime(){
		return System.nanoTime() - timeLost;
	}
}