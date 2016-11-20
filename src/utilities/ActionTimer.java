package utilities;

import java.util.HashMap;

public class ActionTimer implements Runnable{

	private volatile HashMap<Integer, ?> activeTimers;
	
	class TimedObject<T extends TimerListener>{
		private T object;
		private Integer time;
		
		TimedObject(T object, Integer time){
			this.object = object;
			this.time = time;
		}
		
		T getTimedObject(){
			return object;
		}
		
		Integer getTime(){
			return time;
		}
	}
	
	@Override
	public void run(){
		//iterate over activeTimers.
		//check if current time >= TimedObject.time
		//		if true: notify object.
	}
	
	public synchronized <T extends TimerListener> void setTimer(Integer id, 
			T object, Integer delta){
		//set time: current system time + delta
		//create TimedObject with object and time.
		//insert into hashmap with id.
	}
	
	public synchronized void removeTimer(Integer id){
		//removes TimedObject by id.
	}
	
}
