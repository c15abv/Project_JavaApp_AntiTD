package tiles;

import java.util.HashMap;
import java.util.Map;

import start.Position;
import tiles.PathTile.Direction;

public class ConnectedPositions{
	protected HashMap<Direction, Position> map;
	protected Position position;
	
	public ConnectedPositions(Position position){
		this(position, null);
	}
	
	public ConnectedPositions(Position position, ConnectedPositions old){
		this.position = position;
		map = new HashMap<Direction, Position>();
		
		if(old != null){
			for(Map.Entry<Direction, Position> entry :
				old.getMap().entrySet()){
				this.add(entry.getKey());
			}
		}
	}
	
	public void add(Direction dirTo){
		Position newPosition;
		switch(dirTo){
		case EAST:
			newPosition = new Position(position.getX() + 1,
					position.getY());
			break;
		case NORTH:
			newPosition = new Position(position.getX(),
					position.getY() - 1);
			break;
		case SOUTH:
			newPosition = new Position(position.getX(),
					position.getY() + 1);
			break;
		case WEST:
			newPosition = new Position(position.getX() - 1,
					position.getY());
			break;
		case NA:
		default:
			newPosition = position;
			break;
		}
		
		map.put(dirTo, newPosition);
	}
	
	public HashMap<Direction, Position> getMap(){
		return new HashMap<Direction, Position>(map);
	}
	
	public boolean hasConnections(){
		return !map.isEmpty();
	}
	
	public int getNumberOfConnections(){
		return map.size();
	}
	
	public int getNumberOfConnections(Direction ... directions){
		int subStractedDirections = 0;
		
		for(Direction dir : directions){
			if(map.containsKey(dir)){
				subStractedDirections++;
			}
		}
		return getNumberOfConnections() - subStractedDirections;
	}
	
	public Position getPosition(){
		return position;
	}
	
	public boolean changesDirection(Direction dir){
		return map.size() > 2 || (!map.containsKey(opposite(dir)) &&
				map.size() > 1);
	}
	

	public static Direction opposite(Direction direction){
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