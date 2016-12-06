package utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ActionTimer implements Runnable{

	class TimedObject<T extends TimerListener>{
		private volatile T object;
		private volatile long time;
		
		TimedObject(T object, long time){
			this.object = object;
			this.time = time;
		}
		
		T getTimedObject(){
			return object;
		}
		
		double getTime(){
			return time;
		}
	}
	
	private volatile HashMap<Integer, TimedObject<?>> activeTimers;
	private volatile boolean isRunning;
	private volatile boolean paused;
	private Lock lock;
	
	public ActionTimer(){
		activeTimers = new HashMap<Integer, TimedObject<?>>();
		isRunning = true;
		paused = false;
		lock = new Lock();
	}
	
	@Override
	public void run(){
		while(isRunning){
			if(!paused){
				try{
					lock.lock();
					
					Iterator<Entry<Integer, TimedObject<?>>> it = 
							activeTimers.entrySet().iterator();
					Map.Entry<Integer, TimedObject<?>> pair;
				    while(it.hasNext()){
				        pair = (Map.Entry<Integer, TimedObject<?>>)it.next();
				        if(pair.getValue().time <= System.nanoTime()){
				        	pair.getValue().object.receiveNotification(pair.getKey());
				        	it.remove();
				        }
				    }
				}catch(InterruptedException e){
				}finally{
					lock.unlock();
				}
			}
		}
	}
	
	public synchronized <T extends TimerListener> void setTimer(Integer id, 
			T object, long delta){
		try{
			lock.lock();
			activeTimers.put(id, new TimedObject<T>(object, (long)(delta * 10e5) 
					+ System.nanoTime()));
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	public synchronized void removeTimer(Integer id){
		try{
			lock.lock();
			activeTimers.remove(id);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	public synchronized long timeLeft(Integer id){
		long timeLeft = -1;
		TimedObject<?> obj;
		
		try{
			lock.lock();
			obj = activeTimers.get(id);
			timeLeft = (long) (obj.getTime() - System.nanoTime());
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		
		return timeLeft;
	}
	
	public synchronized void terminate(){
		isRunning = false;
	}
	
	public synchronized void pause(){
		paused = true;
	}
	
	public synchronized void resume(){
		paused = false;
	}
	
}
