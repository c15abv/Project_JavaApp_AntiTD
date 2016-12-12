package creatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import creatures.CreatureFigure.Orientation;
import start.AreaPosition;
import start.GameLevel;
import start.Position;
import tiles.ConnectedPositions;
import tiles.PathTile;
import tiles.PathTile.Direction;
import tiles.PathTile.PositionConnection;
import tiles.Tile;

public class AICreatureFigures{

	private AttackingPlayer attacker;
	
	public AICreatureFigures(AttackingPlayer attacker){
		this.attacker = attacker;
	}
	
	public void update(){
		ArrayList<CreatureFigure> horde = attacker.getHorde();
		GameLevel level = attacker.getLevel();
		HashMap<AreaPosition, Tile> map = level.getLevelMap();
		Tile tile = null;
		PositionConnection pair = null;
		ConnectedPositions connected = null;
		Position figPos;
		
		for(CreatureFigure figure : horde){
			figPos = new Position(figure.getPosition().getX()
					+ Tile.size / 2, figure.getPosition().getY()
					+ Tile.size / 2, Tile.size);
			if((tile = map.get(figPos.toArea())) != null &&
					tile.walkable()){
				pair = ((PathTile)tile).getPosPair(figure.getPosition());
				connected = pair.getConnectedPositions();
				if(!findNewPos(connected, figure)){
					figure.remove();
				}else if(((PathTile)tile).isGoalPosition(figure.getPosition())){
					figure.setHasReachedGoal(true);
					figure.setFinished(true);
				}else if(((PathTile)tile).hasEffect()){
					((PathTile)tile).landOn(figure);
				}
			}else{
				figure.remove();
			}
		}
	}
	
	private boolean setNewPos(Direction direction,
			HashMap<Direction, Position> posMap,
			CreatureFigure figure){
		Position newPos = null;
		if(direction == null){
			return false;
		}
		figure.setNavigation(direction);
		if((newPos = posMap.get(direction)) != null){
			figure.setPosition(newPos);
			return true;
		}
		
		return false;
	}
	
	private Orientation randomOrientation(){
		int randomInt = 0;
		return (randomInt = new Random().nextInt(3)) == 0 ? 
				Orientation.FORWARD : randomInt == 1 ?
						Orientation.LEFT : Orientation.RIGHT;
	}
	
	private boolean findNewPos(ConnectedPositions connected, CreatureFigure figure){
		HashMap<Direction, Position> posMap = connected.getMap();
		Position newPos = null;
		Orientation orient = figure.getOrientation();
		Direction translatedDir = null;
		PathMemory memory = figure.getMemory();
		
		if(figure.getPosition().equals(memory.getMostRecentCrossing())){
			return setNewPos(memory.getBackTrackDirection(figure.getPosition()),
					posMap, figure);
		}
		
		if(connected.changesDirection(figure.getNavigationFrom())){
			memory.addPosition(figure.getPosition(), connected,
					figure.getNavigationFrom());
			memory.setMostRecentCrossing(connected);
		}
		
		memory.removeDirection(figure.getPosition(), figure.getNavigationFrom());
		
		if(memory.positionExplored(figure.getPosition())){
			return setNewPos(memory.getBackTrackDirection(), posMap, figure);
		}
		
		if(orient == Orientation.RANDOM){
			orient = randomOrientation();
		}
		
		for(int i = 0; i < 3; i++){
			translatedDir = getTranslatedDirection(figure.getNavigation(),
					orient);
			newPos = posMap.get(translatedDir);
			
			if(newPos != null){
				if(connected.changesDirection(figure.getNavigationFrom()) &&
						memory.isValidDirection(figure.getPosition(), translatedDir)){
					figure.setNavigation(translatedDir);
					memory.rememberBackTrackDirection(figure.getPosition(),
							figure.getNavigationFrom());
					memory.removeDirection(figure.getPosition(),
							figure.getNavigation());
					figure.setPosition(newPos);
					return true;
				}else if(translatedDir == figure.getNavigation() &&
						connected.getNumberOfConnections() <= 2){
					figure.setNavigation(translatedDir);
					figure.setPosition(newPos);
					return true;
				}
			}
			
			switch(orient){
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
	
	private Direction getTranslatedDirection(Direction dirFacing, Orientation orientTo){
		switch(dirFacing){
		case EAST:
			switch(orientTo){
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
			switch(orientTo){
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
			switch(orientTo){
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
			switch(orientTo){
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