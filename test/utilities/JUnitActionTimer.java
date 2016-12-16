package utilities;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnitActionTimer.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class JUnitActionTimer{

	class TestClass implements TimerListener{
		
		private boolean receivedNotification = false;
		private long id = -1;
		
		@Override
		public void receiveNotification(Long id){
			receivedNotification = true;
			this.id = id;
		}
		
		public boolean hasReceivedNotification(){
			return receivedNotification;
		}
		
		public long getIdReturned(){
			return id;
		}
		
	}
	
	@Test
	public void testActionTimerIdCounter(){
		ActionTimer timer = new ActionTimer();
		long id1, id2;
		
		id1 = timer.getNewUniqueId();
		id2 = timer.getNewUniqueId();
		
		assertTrue(id1 == 0);
		assertTrue(id2 == 1);
	}
	
	@Test
	public void testOneTimedObject(){
		TestClass test = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		timer.setTimer(0, test, 1000);
		
		try{
			Thread.sleep(1001);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timer.terminate();
		assertTrue(test.hasReceivedNotification());
	}
	
	@Test
	public void testOneTimedObjectCorrectId(){
		TestClass test = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		timer.setTimer(0, test, 1000);
		
		try{
			Thread.sleep(1001);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timer.terminate();
		assertTrue(test.getIdReturned() == 0);
	}

	@Test
	public void testTwoTimedObjects(){
		TestClass test1 = new TestClass();
		TestClass test2 = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		timer.setTimer(0, test1, 1000);
		timer.setTimer(1, test2, 1000);
		
		try{
			Thread.sleep(1001);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timer.terminate();
		assertTrue(test1.hasReceivedNotification());
		assertTrue(test2.hasReceivedNotification());
	}
	
	@Test
	public void testTwoTimedObjectsRemoveOne(){
		long id1, id2;
		TestClass test1 = new TestClass();
		TestClass test2 = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		id1 = timer.getNewUniqueId();
		id2 = timer.getNewUniqueId();
		timer.setTimer(id1, test1, 1000);
		timer.setTimer(id2, test2, 1000);
		
		try{
			Thread.sleep(500);
			timer.removeTimer(id1);
			Thread.sleep(501);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timer.terminate();
		assertFalse(test1.hasReceivedNotification());
		assertTrue(test2.hasReceivedNotification());
	}
	
	@Test
	public void testGetTimeLeft(){
		long timeLeft = 0;
		long id;
		TestClass test1 = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		id = timer.getNewUniqueId();
		timer.setTimer(id, test1, 1000);
		
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timeLeft = timer.timeLeft(id);
		
		assertTrue(timeLeft <= 501000000 && timeLeft >= 499000000);
		
		timer.terminate();
	}
	
	@Test
	public void testGetTimeLeftOnRemovedObject(){
		long id;
		long timeLeft = 0;
		TestClass test1 = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		id = timer.getNewUniqueId();
		timer.setTimer(id, test1, 1000);
		
		try{
			Thread.sleep(500);
			timer.removeTimer(id);
			Thread.sleep(501);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		timeLeft = timer.timeLeft(id);
		
		assertTrue(timeLeft == -1);
		
		timer.terminate();
	}
	
	@Test
	public void testTimerPause(){
		TestClass test1 = new TestClass();
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		
		thread.start();
		timer.setTimer(0, test1, 1000);
		
		try{
			Thread.sleep(500);
			timer.pause();
			Thread.sleep(600);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		assertFalse(test1.hasReceivedNotification());
		timer.resume();
		
		try{
			Thread.sleep(501);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		assertTrue(test1.hasReceivedNotification());
		timer.terminate();
	}
	
}
