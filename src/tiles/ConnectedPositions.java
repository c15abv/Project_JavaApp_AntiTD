package tiles;

import java.util.HashMap;
import java.util.Map;

import start.Position;
import tiles.PathTile.Direction;


/**
 * ConnectedPositions connects two to four different
 * positions together. Using various methods a user 
 * may then determine what position is best suited
 * for a certain situation.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
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
	
	/**
	 * Creates a new connection with the currently managed position by
	 * adding a direction to the position.
	 * 
	 * @param dirTo the direction.
	 */
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
	
	/**
	 * Returns if the positions has any connections.
	 * 
	 * @return if the position has any connections.
	 */
	public boolean hasConnections(){
		return !map.isEmpty();
	}
	
	/**
	 * Returns the number of connections to this position.
	 *
	 * @return the number of connections to this position.
	 */
	public int getNumberOfConnections(){
		return map.size();
	}
	
	/**
	 * Returns the number of connections this position has
	 * excluded the given directions.
	 * 
	 * @param directions the excluded directions.
	 * @return the number of connections to this position.
	 */
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
	
	/**
	 * Returns <true> if and only if the current position
	 * has more than two connections or if the number of connections
	 * are greater than one and at least
	 * one of the connections to the position is not an opposite direction
	 * to the given direction. <false> is returned otherwise.
	 * 
	 * @param dir a direction.
	 * @return <true> if there is a connection which changes the
	 * direction (not being an opposite), <false> otherwise.
	 */
	public boolean changesDirection(Direction dir){
		return map.size() > 2 || (!map.containsKey(Direction.getOpposite(dir)) &&
				map.size() > 1);
	}
	
}