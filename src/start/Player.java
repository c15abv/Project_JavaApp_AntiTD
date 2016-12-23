package start;

import java.awt.Graphics2D;

/**
 * The abstract class Player defines what information is necessary to hold in
 * order to create a new player in the AntiTD application.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public abstract class Player {

	private int credits;
	private GameLevel level;

	/**
	 * Creates a new Player with the specified credits and level.
	 * 
	 * @param credits
	 *            the credits.
	 * @param level
	 *            the level.
	 */
	public Player(int credits, GameLevel level) {
		this.credits = credits;
		this.level = level;
	}

	/**
	 * Performs any logic which is deemed necessary to be run/updated for any
	 * object held by the Player.
	 */
	public abstract void update();

	/**
	 * Renders any objects held by the Player.
	 * 
	 * @param g2d
	 *            the Graphics2D object.
	 */
	public abstract void render(Graphics2D g2d);

	// Setters and getters

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getCredits() {
		return credits;
	}

	public GameLevel getLevel() {
		return level;
	}
}
