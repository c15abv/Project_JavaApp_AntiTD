package utilities;

/**
 * The TimerListener interface is used when it is deemed
 * necessary to be able to notify an object which may
 * have different solutions for various signals/ids.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public interface TimerListener{
	
	/**
	 * Notifies the object with an id.
	 * This notification may do anything.
	 * 
	 * @param id the id.
	 */
	void receiveNotification(Long id);
}
