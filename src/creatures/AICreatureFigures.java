package creatures;

import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure.Orientation;
import start.AreaPosition;
import start.GameLevel;
import start.Position;
import tiles.ConnectedPositions;
import tiles.PathTile;
import tiles.PathTile.Direction;
import tiles.PathTile.PositionConnection;
import tiles.Tile;

/**
 * The AICreatureFigures class uses a depth-first search inspired algorithm to
 * help an AttackingPlayer's current horde traverse a level given to the
 * AttackingPlayer class. The algorithm takes the creature's orientation into
 * account when choosing a certain path, hence the algorithm is strongly
 * inspired by but not solely based on the depth-first search pattern. The path
 * taken by each creature within the horde is remembered separately. If one
 * creature finds it way to a goal any other creature within the horde will be
 * unaware of it.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class AICreatureFigures {

	private AttackingPlayer attacker;

	/**
	 * AICreatureFigures constructor.<br>
	 * <br>
	 * Creates an AICreatureFigures instance for the specified AttackingPlayer.
	 * 
	 * @param attacker
	 *            the holder class of the attacking player.
	 */
	public AICreatureFigures(AttackingPlayer attacker) {
		this.attacker = attacker;
	}

	/**
	 * The intent of this method is to be used in conjunction and in a
	 * simultaneous fashion with the game's other update methods.<br>
	 * <br>
	 * Each call to the update method updates each creature in the attacking
	 * player's horde by one logical step. What a logical step may infer to
	 * depends on the given creature. Some may move several positions on the x-
	 * and y-axis while others none until several logical steps later. If the
	 * AICreatureFigures class finds a creature to be out of place it will
	 * inevitably remove it from the game. If a creature can not find a new
	 * unexplored position, its memory is wiped; this is in the case it would
	 * happen to enter a series of teleport tiles which would lead to it
	 * traversing the same path several times. Hence, instead of going to the
	 * extreme and entirely removing the creature from the game its is wiped.
	 */
	public void update() {
		ArrayList<CreatureFigure> horde = attacker.getHorde();
		GameLevel level = attacker.getLevel();
		HashMap<AreaPosition, Tile> map = level.getLevelMap();
		Tile tile = null;
		PositionConnection pair = null;
		ConnectedPositions connected = null;
		Position figPos;
		int moveNumTiles = 0;

		for (CreatureFigure figure : horde) {
			moveNumTiles = figure.getTilesMoved();
			for (int i = 0; i < moveNumTiles; i++) {
				figPos = new Position(
						figure.getPosition().getX() + Tile.size / 2,
						figure.getPosition().getY() + Tile.size / 2, Tile.size);
				if ((tile = map.get(figPos.toArea())) != null
						&& tile.walkable()) {
					pair = ((PathTile) tile).getPosPair(figure.getPosition());
					connected = pair.getConnectedPositions();
					if (!findNewPos(connected, figure)) {
						figure.getMemory().eraseMemory();
					} else if (((PathTile) tile)
							.isGoalPosition(figure.getPosition())) {
						figure.setHasReachedGoal(true);
						figure.setFinished(true);
					} else if (((PathTile) tile).hasEffect()) {
						((PathTile) tile).landOn(figure);
					}
				} else {
					figure.remove();
				}
			}
			if (moveNumTiles > 0)
				figure.resetTilesMoved();
		}
	}

	/*
	 * Sets a new position for the creature based on the direction of it as well
	 * as the available paths in the level.
	 */
	private boolean setNewPos(Direction direction,
			HashMap<Direction, Position> posMap, CreatureFigure figure) {
		Position newPos = null;
		if (direction == null) {
			return false;
		}
		figure.setNavigation(direction);
		if ((newPos = posMap.get(direction)) != null) {
			figure.setPosition(newPos);
			return true;
		}

		return false;
	}

	/**
	 * Finds a new position for the creature based on the given quantity of
	 * valid positions.
	 * 
	 * The path taken up until this point is also stored and remembered.
	 * @param connected
	 * @param figure
	 * @return
	 */
	private boolean findNewPos(ConnectedPositions connected,
			CreatureFigure figure) {
		HashMap<Direction, Position> posMap = connected.getMap();
		Position newPos = null;
		Orientation orient = figure.getOrientation();
		Direction translatedDir = null;
		PathMemory memory = figure.getMemory();

		if (figure.getPosition().equals(memory.getMostRecentCrossing())
				&& memory.positionExplored(figure.getPosition())) {
			return setNewPos(memory.getBackTrackDirection(figure.getPosition()),
					posMap, figure);
		}

		if (connected.changesDirection(figure.getNavigationFrom())) {
			memory.addPosition(figure.getPosition(), connected,
					figure.getNavigationFrom());
			memory.setMostRecentCrossing(connected);
		}

		memory.removeDirection(figure.getPosition(),
				figure.getNavigationFrom());

		if (memory.positionExplored(figure.getPosition())) {
			return setNewPos(memory.getBackTrackDirection(), posMap, figure);
		}

		if (orient == Orientation.RANDOM) {
			orient = Orientation.randomOrientation();
		}

		for (int i = 0; i < 3; i++) {
			translatedDir = getTranslatedDirection(figure.getNavigation(),
					orient);
			newPos = posMap.get(translatedDir);

			if (newPos != null) {
				if (connected.changesDirection(figure.getNavigationFrom())
						&& memory.isValidDirection(figure.getPosition(),
								translatedDir)) {
					figure.setNavigation(translatedDir);
					memory.rememberBackTrackDirection(figure.getPosition(),
							figure.getNavigationFrom());
					memory.removeDirection(figure.getPosition(),
							figure.getNavigation());
					figure.setPosition(newPos);
					return true;
				} else if (translatedDir == figure.getNavigation()
						&& connected.getNumberOfConnections() <= 2) {
					figure.setNavigation(translatedDir);
					figure.setPosition(newPos);
					return true;
				}
			}

			switch (orient) {
			case FORWARD:
				orient = Orientation.LEFT;
				break;
			case LEFT:
				orient = Orientation.RIGHT;
				break;
			case RIGHT:
				orient = Orientation.FORWARD;
				break;
			default:
				break;
			}
		}

		return setNewPos(memory.getBackTrackDirection(), posMap, figure);
	}

	/**
	 * Translates a given facing direction in conjunction with an orientation to
	 * the direction of the orientation.
	 * 
	 * @param dirFacing
	 * @param orientTo
	 * @return
	 */
	private Direction getTranslatedDirection(Direction dirFacing,
			Orientation orientTo) {
		switch (dirFacing) {
		case EAST:
			switch (orientTo) {
			case FORWARD:
				return Direction.EAST;
			case LEFT:
				return Direction.NORTH;
			case RIGHT:
				return Direction.SOUTH;
			default:
				return null;
			}
		case NORTH:
			switch (orientTo) {
			case FORWARD:
				return Direction.NORTH;
			case LEFT:
				return Direction.WEST;
			case RIGHT:
				return Direction.EAST;
			default:
				return null;
			}
		case SOUTH:
			switch (orientTo) {
			case FORWARD:
				return Direction.SOUTH;
			case LEFT:
				return Direction.EAST;
			case RIGHT:
				return Direction.WEST;
			default:
				return null;
			}
		case WEST:
			switch (orientTo) {
			case FORWARD:
				return Direction.WEST;
			case LEFT:
				return Direction.SOUTH;
			case RIGHT:
				return Direction.NORTH;
			default:
				return null;
			}
		default:
			return null;
		}
	}
}
