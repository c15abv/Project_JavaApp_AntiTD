package utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ActionTimer.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class ActionTimer implements Runnable {

	class TimedObject<T extends TimerListener> {
		private volatile T object;
		private volatile long time, timeC;

		TimedObject(T object, long time) {
			this.object = object;
			this.time = time;
			timeC = System.nanoTime();
		}

		T getTimedObject() {
			return object;
		}

		long getTime() {
			return time;
		}

		long getTimeC() {
			return timeC;
		}
	}

	private volatile IdCounter idCounter;
	private volatile HashMap<Long, TimedObject<?>> activeTimers;
	private volatile boolean isRunning;
	private volatile boolean paused;
	private volatile long timeCreated, timeAtPaused, timeLost;
	private Lock lock;

	public ActionTimer() {
		idCounter = new IdCounter();
		activeTimers = new HashMap<Long, TimedObject<?>>();
		isRunning = true;
		paused = false;
		lock = new Lock();
		timeCreated = timeAtPaused = timeLost = 0;
	}

	@Override
	public void run() {
		timeCreated = System.nanoTime();

		while (isRunning) {
			if (!paused) {
				try {
					lock.lock();

					Iterator<Entry<Long, TimedObject<?>>> it = activeTimers
							.entrySet().iterator();
					Map.Entry<Long, TimedObject<?>> pair;
					while (it.hasNext()) {
						pair = (Map.Entry<Long, TimedObject<?>>) it.next();
						if (pair.getValue().time <= getCurrentTime()) {
							pair.getValue().object
									.receiveNotification(pair.getKey());
							it.remove();
						}
					}
				} catch (InterruptedException e) {
				} finally {
					lock.unlock();
				}
			} else {

			}
		}
	}

	public synchronized <T extends TimerListener> void setTimer(long id,
			T object, long delta) {
		try {
			lock.lock();
			activeTimers.put(id, new TimedObject<T>(object,
					(long) (delta * 10e5) + getCurrentTime()));
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
	}

	public synchronized void removeTimer(Long id) {
		try {
			lock.lock();
			activeTimers.remove(id);
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
	}

	public synchronized long timeLeft(Long id) {
		long timeLeft = -1;
		TimedObject<?> obj;

		try {
			lock.lock();
			obj = activeTimers.get(id);
			if (obj != null) {
				if (obj.getTime() - getCurrentTime() < 0) {
					timeLeft = 0;
				} else {
					timeLeft = (long) (obj.getTime() - getCurrentTime());
				}
			}
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}

		return timeLeft;
	}

	public synchronized long timeElapsed() {
		return paused ? timeAtPaused - timeCreated
				: getCurrentTime() - timeCreated;
	}

	public synchronized long timeElapsed(Long id) {
		long timeElapsed = -1;
		TimedObject<?> obj;

		try {
			lock.lock();
			obj = activeTimers.get(id);
			if (obj != null) {
				if (obj.getTime() - getCurrentTime() < 0) {
					timeElapsed = getCurrentTime() - obj.getTimeC();
				} else {
					timeElapsed = (long) (obj.getTime() - obj.getTimeC());
				}
			}
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}

		return timeElapsed;
	}

	public synchronized long getNewUniqueId() {
		long id = idCounter.getId();
		idCounter.increment();
		return id;
	}

	public synchronized void terminate() {
		isRunning = false;
	}

	public synchronized void pause() {
		timeAtPaused = System.nanoTime();
		paused = true;
	}

	public synchronized void resume() {
		timeLost += (System.nanoTime() - timeAtPaused);
		paused = false;
	}

	private long getCurrentTime() {
		return System.nanoTime() - timeLost;
	}
}