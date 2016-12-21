package tiles;

import creatures.CreatureFigure;

/**

 * Implement this interface when a figure entering the tile
 * should trigger an effect/action.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public interface EnterTileEffect{
	
	/**
	 * Triggers an effect with the given creature
	 * as the the trigger.
	 * 
	 * @param creature the creature.
	 */
	void landOn(CreatureFigure creature);
}
