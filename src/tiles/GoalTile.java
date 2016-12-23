package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Position;

/**
 * A goalTile used for creating goals for units to reach
 * 
 * @author Alexander Beliaev, Jan Nylen, Alexander Ekstrom
 */
public class GoalTile extends PathTile {

	private static final int RENDER_COUNT_LIMIT = 50;
	private static final int SLEEP_RENDER_COUNT = 30;

	private int renderAnimationCount;
	private int sleepCount;
	private boolean sleepRender;

	/**
	 * Constructor that sets the position and path to the given parameters.
	 * 
	 * @param position
	 * @param path
	 */
	public GoalTile(Position position, ValidPath path) {
		this(position, null, path);
	}

	/**
	 * Constructor that initiates the tile pased on the given parameters.
	 * 
	 * @param position
	 * @param tileType
	 * @param path
	 */
	public GoalTile(Position position, String tileType, ValidPath path) {
		super(position, path);
		renderAnimationCount = sleepCount = 0;
		sleepRender = false;
	}

	@Override
	public void update() {
		if (sleepRender && sleepCount < SLEEP_RENDER_COUNT) {
			sleepCount++;
		} else if (sleepRender && sleepCount >= SLEEP_RENDER_COUNT) {
			sleepRender = false;
			renderAnimationCount = 0;
		} else if (!sleepRender && renderAnimationCount < RENDER_COUNT_LIMIT) {
			renderAnimationCount++;
		} else if (!sleepRender && renderAnimationCount >= RENDER_COUNT_LIMIT) {
			renderAnimationCount = RENDER_COUNT_LIMIT;
			sleepRender = true;
			sleepCount = 0;
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		Color color;
		float alpha;
		int size;

		super.render(g2d);

		alpha = 1.0f
				- (float) renderAnimationCount / (float) RENDER_COUNT_LIMIT;
		size = Tile.size / 4 + (int) ((Tile.size / 4)
				* (double) renderAnimationCount / (double) RENDER_COUNT_LIMIT);
		color = new Color((float) 125 / 255, (float) 150 / 255,
				(float) 25 / 255, alpha);

		g2d.setColor(color);
		g2d.drawOval(getPosition().getX() - size / 2,
				getPosition().getY() - size / 2, size, size);
	}

	@Override
	public boolean isGoalPosition(Position position) {
		return getPosition().equals(position);
	}

	@Override
	public boolean isGoal() {
		return true;
	}

}
