package utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ActionTimer implements Runnable{

	class TimedObject<T extends TimerListener>{
		private volatile T object;
		private volatile double time;
		
		TimedObject(T object, double time){
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
	private Lock lock;
	
	public ActionTimer(){
		activeTimers = new HashMap<Integer, TimedObject<?>>();
		isRunning = true;
		lock = new Lock();
	}
	
	@Override
	public void run(){
		while(isRunning){
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
	
	public synchronized <T extends TimerListener> void setTimer(Integer id, 
			T object, double delta){
		try{
			lock.lock();
			activeTimers.put(id, new TimedObject<T>(object, (delta * 10e5) 
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
	
	public synchronized void terminate(){
		isRunning = false;
	}
	
}
