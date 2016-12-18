package tiles;

import creatures.CreatureFigure;

/**
 * @author Alexander Beliaev
 * An interface used for defining a landOn method that
 * defines what is supposed to happen when a unit reaches a
 * given tile.
 */
public interface EnterTileEffect{
	void landOn(CreatureFigure creature);
}
