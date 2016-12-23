package creatures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import start.Figures;
import start.GameLevel;
import start.Position;
import tiles.PathTile;
import tiles.PathTile.Direction;
import tiles.PathTile.ValidPath;
import tiles.TeleportTile;
import tiles.Tile;
import utilities.Action;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.Lock;
import utilities.TimerListener;

/**
 * 
 * The abstract class CreatureFigure holds the very basics of what a creature
 * is. It is only when the CreatureFigure class is extended when certain
 * characteristics are set.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 *
 */
public abstract class CreatureFigure implements TimerListener {

	public enum Orientation {
		RIGHT, LEFT, RANDOM, FORWARD;

		public static Orientation randomOrientation() {
			int random = new Random().nextInt(3);
			return random == 0 ? Orientation.RIGHT
					: random == 1 ? Orientation.LEFT : Orientation.FORWARD;
		}
	}

	public static final int DEFAULT_CREDIT = 10;
	public static final int BASE_HITPOINTS = 100;
	public static final int BASE_SIZE = 36;
	public static final double BASE_SPEED = 2;

	private Color creatureColor;
	private Orientation orientation;
	private PathTile.Direction navigation;
	private PathTile.Direction navigationFrom;
	private PathMemory memory;
	private GameLevel level;
	private int hue, creditOnGoal, creditOnKill;
	private double scale, speed, tilesMoved;
	private Position position;
	private int hitPoints;
	private int startHitPoints;
	private boolean isAlive;
	private boolean finished;
	private Lock lock;
	private ArrayList<Action> onSpawnActionList;
	private HashMap<Long, Action> onSpawnTimedActionMap;
	private ArrayList<Action> onDeathActionList;
	private volatile ArrayList<Long> notifiedTimedActions;
	private ActionTimer actionTimer;
	private ArrayList<Action> onActiveActionList;
	private boolean hasSpawned;
	private boolean hasReachedGoal;

	/**
	 * Creates a new creature with the specified characteristics.
	 * 
	 * @param hue
	 *            the hue of the creature.
	 * @param scale
	 *            the scale of the creature.
	 * @param position
	 *            the initial position of the creature.
	 * @param orientation
	 *            the orientation of the creature.
	 * @param level
	 *            the level.
	 * @param speed
	 *            the speed of the creature.
	 */
	public CreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level, double speed) {
		this.hue = hue;
		this.scale = scale;
		this.position = position;
		this.creatureColor = ColorCreator.generateColorFromHue(hue);
		this.orientation = orientation;
		this.level = level;
		this.speed = speed;

		init();
	}

	/**
	 * Renders the figure.
	 * 
	 * @param g2d
	 *            the Graphics2D object.
	 */
	public abstract void render(Graphics2D g2d);

	/**
	 * Performs a check if a certain position is within the body of the
	 * creature.
	 * 
	 * @param position
	 *            the position to be checked.
	 * @return <code>true</code> if and only if the given position is on or
	 *         within the body of the creature.
	 */
	public abstract boolean isCollision(Position position);

	/**
	 * Returns a Figures value describing the shape of the creature.
	 * 
	 * @return a Figures value describing the shape of the creature.
	 */
	public abstract Figures getShape();

	/**
	 * Updates the creature by iterating over any data structures holding any
	 * previously defined actions; as well as incrementing the amount of steps
	 * it should be taking. The amount of steps taken is directly dependent upon
	 * the scaling factor of the creature. The creature may therefore not
	 * precisely one position each update, but rather parts of or several whole.
	 */
	public void update() {
		if (isAlive && !hasReachedGoal) {
			if (!hasSpawned) {
				for (Action action : this.onSpawnActionList) {
					action.executeAction();
				}
				this.hasSpawned = true;
			}
			try {
				lock.lock();
				for (Long id : notifiedTimedActions) {
					onSpawnTimedActionMap.get(id).executeAction();
				}
				notifiedTimedActions = new ArrayList<Long>();
			} catch (InterruptedException e) {
			} finally {
				lock.unlock();
			}

			for (Action action : this.onActiveActionList) {
				action.executeAction();
			}

			tilesMoved += speed;

		} else if (!isAlive && !finished) {
			for (Action action : onDeathActionList) {
				action.executeAction();
			}
			finished = true;
		}
	}

	/**
	 * Reduces the current life of the creature by the specified amount.
	 * 
	 * @param damage
	 *            the damage to inflict upon the creature.
	 */
	public void setDamageTaken(int damage) {
		if (!hasReachedGoal) {
			hitPoints -= damage;
		}
		if (hitPoints <= 0) {
			isAlive = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + hue;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CreatureFigure other = (CreatureFigure) obj;
		if (hue != other.hue) {
			return false;
		}

		return true;
	}

	/**
	 * A default method to enable a creature to be able to create
	 * TeleportTiles.<br>
	 * <br>
	 * The time given to the method is the amount of time before the first
	 * teleport is supposed to be set.
	 * 
	 * @param time
	 *            the time to be elapsed before setting the first teleport.
	 */
	public void enableTeleport(Long time) {
		final long id = level.getNewUniqueId();

		addOnSpawnTimedAction(time, () -> {
			performTeleportCreationAction(id, null);
		});

		addOnDeathAction(() -> {
			TeleportTile tile = (TeleportTile) level.getTileById(id);
			if (tile != null) {
				performTeleportCreationAction(id, tile);
			}
		});
	}

	/**
	 * Adds an action which will be performed upon the spawning of the creature.
	 * 
	 * @param action
	 *            the action to be added.
	 */
	public void addOnSpawnAction(Action action) {
		onSpawnActionList.add(action);
	}

	/**
	 * Adds an action which will be performed after a certain time following the
	 * spawning of the creature.
	 * 
	 * @param time
	 *            the time in milliseconds to wait before performing the
	 *            specified action.
	 * @param action
	 *            the action to be added.
	 */
	public void addOnSpawnTimedAction(Long time, Action action) {
		long id;
		if (hasActionTimer()) {
			id = actionTimer.getNewUniqueId();
			onSpawnTimedActionMap.put(id, action);
			actionTimer.setTimer(id, this, time);
		}
	}

	/**
	 * Adds an action which will be performed upon the death of the creature.
	 * 
	 * @param action
	 *            the action to be added.
	 */
	public void addOnDeathAction(Action action) {
		onDeathActionList.add(action);
	}

	/**
	 * Adds an action which will be performed each call to the update method.
	 * 
	 * @param action
	 *            the action to be added.
	 */
	public void addActiveAction(Action action) {
		onActiveActionList.add(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utilities.TimerListener#receiveNotification(java.lang.Long)
	 */
	@Override
	public void receiveNotification(Long id) {
		try {
			lock.lock();
			notifiedTimedActions.add(id);
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
		// id is the key in onSpawnTimerActionMap.
	}

	/*
	 * Initializes the creature.
	 */
	private void init() {
		isAlive = true;
		hasReachedGoal = false;
		finished = false;
		hasSpawned = false;
		onSpawnActionList = new ArrayList<Action>();
		onSpawnTimedActionMap = new HashMap<Long, Action>();
		onDeathActionList = new ArrayList<Action>();
		notifiedTimedActions = new ArrayList<Long>();
		onActiveActionList = new ArrayList<Action>();
		lock = new Lock();
		memory = new PathMemory();

		hitPoints = startHitPoints = (int) (BASE_HITPOINTS * scale);
		navigation = navigationFrom = PathTile.Direction.NA;
		creditOnGoal = creditOnKill = DEFAULT_CREDIT;

		speed = speed / scale;
		tilesMoved = 1;
	}

	/*
	 * Setters and getters.
	 */
	public ActionTimer getActionTimer() {
		return actionTimer;
	}

	public void setActionTimer(ActionTimer actionTimer) {
		this.actionTimer = actionTimer;
	}

	public boolean hasActionTimer() {
		return actionTimer != null;
	}

	protected void remove() {
		isAlive = false;
		finished = true;
	}

	public Color getColor() {
		return creatureColor;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isFinished() {
		return finished;
	}

	/**
	 * Returns the amount of life of the creature in parts whole parts of one
	 * hundred.
	 * 
	 * @return the percentage of life left.
	 */
	public int percentLife() {
		return Math.round(
				(int) ((double) hitPoints * 100 / (double) startHitPoints));
	}

	protected void setCreditOnGoal(int creditOnGoal) {
		this.creditOnGoal = creditOnGoal;
	}

	public int getCreditOnGoal() {
		return creditOnGoal;
	}

	protected void setCreditOnKill(int creditOnKill) {
		this.creditOnKill = creditOnKill;
	}

	public int getCreditOnKill() {
		return creditOnKill;
	}

	public int getHue() {
		return hue;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public boolean hasReachedGoal() {
		return hasReachedGoal;
	}

	protected void setHasReachedGoal(boolean hasReachedGoal) {
		this.hasReachedGoal = hasReachedGoal;
	}

	protected void setFinished(boolean finished) {
		this.finished = finished;
	}

	public PathTile.Direction getNavigation() {
		return navigation;
	}

	public PathTile.Direction getNavigationFrom() {
		return navigationFrom;
	}

	protected PathMemory getMemory() {
		return memory;
	}

	public void setNavigation(PathTile.Direction navigation) {
		this.navigation = navigation;
		navigationFrom = Direction.getOpposite(navigation);
	}

	public int getTilesMoved() {
		return (int) tilesMoved;
	}

	public void resetTilesMoved() {
		tilesMoved = Math.round(tilesMoved) - tilesMoved;
	}

	/*
	 * The default action performed by a teleporter-creature.
	 * 
	 * A teleport can not be placed on an existing TeleportTile. If the creature
	 * would happen to be located on a TeleportTile the creature will not put a
	 * teleporter.
	 * 
	 * A teleport will only be 'active' if the creature dies after the first
	 * teleport has been set.
	 */
	private void performTeleportCreationAction(final long id,
			final TeleportTile otherTeleportTile) {
		Position figPos = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2, Tile.size);
		Tile tile = level.getLevelMap().get(figPos.toArea());
		Position tilePosition = tile.getPosition();
		ValidPath path;
		TeleportTile teleportTile;

		if (tile.walkable()) {
			path = ((PathTile) tile).getValidPath();
			if (tile.getClass().equals(PathTile.class)) {
				teleportTile = new TeleportTile(tilePosition, path);
				teleportTile.setTeleporterAt(getPosition());
				if (id != 0) {
					teleportTile.setId(id);
				}
				if (otherTeleportTile != null) {
					otherTeleportTile.setConnection(teleportTile);
					teleportTile.setConnection(otherTeleportTile);
				}
				level.changeTile(figPos.toArea(), teleportTile);
			}
		}
	}
}