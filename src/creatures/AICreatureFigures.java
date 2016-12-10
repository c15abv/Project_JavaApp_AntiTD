package creatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import creatures.CreatureFigure.Orientation;
import start.AreaPosition;
import start.AttackingPlayer;
import start.GameLevel;
import start.Position;
import tiles.PathTile;
import tiles.PathTile.ConnectedPositions;
import tiles.PathTile.Direction;
import tiles.PathTile.PositionNext;
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
				if(!setNewPos(connected, figure)){
					figure.remove();
				}
			}else{
				figure.remove();
			}
		}
		
		/*for(CreatureFigure figure : horde){
			figPos = new Position(figure.getPosition().getX()
					+ Tile.size / 2, figure.getPosition().getY()
					+ Tile.size / 2, Tile.size);
			if((tile = map.get(figPos.toArea())) != null &&
					tile.walkable()){
				pair = ((PathTile)tile).getPosPair(figure.getPosition());
				if(!setNewPos(pair, figure)){
					figure.remove();
				}
			}else if(tile != null && !tile.walkable()){
				figure.setNavigation(figure.getNavigationFrom());
				pair = ((PathTile)tile).getPosPair(figure.getPosition());
				if(!setNewPos(pair, figure)){
					figure.remove();
				}
			}else{
				System.out.println(tile);
				figure.remove();
			}
		}*/
	}
	
	private boolean setNewPos(ConnectedPositions connected, CreatureFigure figure){
		HashMap<Direction, Position> posMap = connected.getMap();
		Position newPos = null;
		Orientation orient = figure.getOrientation();
		int randomInt = 0;
		Direction translatedDir = null;
		
		if(orient == Orientation.RANDOM){
			orient = (randomInt = new Random().nextInt(3)) == 0 ? 
					Orientation.FORWARD : randomInt == 1 ?
							Orientation.LEFT : Orientation.RIGHT;
		}
		
		translatedDir = getTranslatedDirection(figure.getNavigation(),
				orient);
		
		if((newPos = posMap.get(translatedDir)) != null){
			figure.setNavigation(translatedDir);
			figure.setPosition(newPos);
			return true;
		}
		
		for(int i = 0; newPos == null && i < 3; i++){
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
			
			translatedDir = getTranslatedDirection(figure.getNavigation(),
					orient);
			newPos = posMap.get(translatedDir);
		}
		
		
		if(newPos != null){
			figure.setNavigation(translatedDir);
			figure.setPosition(newPos);
			return true;
		}else if((newPos = posMap.get(opposite(figure.getNavigation()))) != null){
			figure.setNavigation(opposite(figure.getNavigation()));
			figure.setPosition(newPos);
			return true;
		}
		
		return false;
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
	
	/*private boolean setNewPos(PositionConnection pair, CreatureFigure figure){
		Position nextPosition = null;
		
		if((nextPosition = getNewPos(pair, figure)) != null){
			figure.setPosition(nextPosition);
			return true;
		}else{
			return false;
		}
	}*/
	
	/*private Position getNewPos(PositionConnection pair, CreatureFigure figure){
		Direction direction = figure.getNavigationFrom();
		PositionNext next;
		Position returnPos;
		HashMap<Orientation, Position> map;
		Orientation orient;
		int randomInt;
		
		if(direction == Direction.NA){
			direction = opposite(figure.getNavigation());
		}
		
		if((next = pair.getConnection(direction)) == null){
			direction = figure.getNavigation();
		}
		
		for(int i=0; (next = pair.getConnection(direction)) == null && i < 4; i++){
			switch(direction){
			case EAST:
				direction = Direction.NORTH;
				break;
			case NORTH:
				direction = Direction.WEST;
				break;
			case SOUTH:
				direction = Direction.EAST;
				break;
			case WEST:
				direction = Direction.SOUTH;
				break;
			default:
				direction = Direction.EAST;
				i--;
			}
		}
		
		if(next == null){
			return null;
		}
	
		returnPos = null;
		orient = figure.getOrientation();
		map = next.getPosMap(direction);
		
		if(map == null){
			System.out.println("null");
		}
		
		if(orient == Orientation.RANDOM){
			orient = (randomInt = new Random().nextInt(3)) == 0 ? 
					Orientation.FORWARD : randomInt == 1 ?
							Orientation.LEFT : Orientation.RIGHT;
		}
		
		switch(orient){
		case FORWARD:
			if((returnPos = map.get(Orientation.FORWARD)) != null ||
					(returnPos = map.get(Orientation.RIGHT)) != null ||
					(returnPos = map.get(Orientation.LEFT)) != null){
				return returnPos;
			}
			break;
		case LEFT:
			if((returnPos = map.get(Orientation.LEFT)) != null ||
					(returnPos = map.get(Orientation.FORWARD)) != null ||
					(returnPos = map.get(Orientation.RIGHT)) != null){
				return returnPos;
			}
			break;
		case RIGHT:
			if((returnPos = map.get(Orientation.RIGHT)) != null ||
					(returnPos = map.get(Orientation.FORWARD)) != null ||
					(returnPos = map.get(Orientation.LEFT)) != null){
				return returnPos;
			}
			break;
		default:
			return null;
		}
		
		//backward
		if(returnPos == null){
			returnPos = next.getPos(figure.getNavigation(),
					Orientation.FORWARD);
		}
		
		return returnPos;
	}*/
	
	private Direction opposite(Direction direction){
		switch(direction){
		case EAST:
			return Direction.WEST;
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		case WEST:
			return Direction.EAST;
		case NA:
		default:
			return Direction.NA;
		}
	}
}
