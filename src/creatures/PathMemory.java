package creatures;

import java.util.HashMap;
import java.util.Stack;

import start.Position;
import tiles.ConnectedPositions;
import tiles.PathTile.Direction;

/**
 * The PathMemory class holds various positions 
 * and positions taken from that position, in 
 * other words connections.<br>
 * <br>
 * Various methods exist to determine the 
 * adequacy of a certain direction or position in
 * relation to previously stored/removed connections.<br>
 * <br>
 * If the user would like to only use positions which has
 * not been fully explored i.e., has not been approached to/
 * departed from from all possible directions, then the 
 * <code>positionExplored</code> method will be of use.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class PathMemory{
	
	/**
	 * RememberedPositionConnection inherits from the 
	 * ConnectedPositions class, and uses this inheritance
	 * to enable the removal of connected positions. 
	 * 
	 * @author Alexander Beliaev
	 * @version 1.0
	 */
	class RememberedPositionConnection extends ConnectedPositions{
		
		RememberedPositionConnection(ConnectedPositions conPos,
				Direction dir){
			super(conPos.getPosition(), conPos);
			this.remove(dir);
		}

		void remove(Direction dir){
			this.map.remove(dir);
		}
	}
	
	/**
	 * DirectionHolder associates a position with a certain
	 * direction.
	 * 
	 * @author Alexander Believ
	 * @version 1.0
	 */
	class DirectionHolder{
		private Position position;
		private Direction direction;
		
		DirectionHolder(Position position, Direction direction){
			this.position = position;
			this.direction = direction;
		}
		
		Position getPosition(){
			return position;
		}
		
		Direction getDirection(){
			return direction;
		}
	}
	
	private HashMap<Position, RememberedPositionConnection> remMap;
	private Stack<DirectionHolder> remDir;
	private Position mostRecentCrossing;
	
	
	/**
	 * PathMemory constructor.
	 * 
	 */
	public PathMemory(){
		remMap = new HashMap<Position, RememberedPositionConnection>();
		remDir = new Stack<DirectionHolder>();
		mostRecentCrossing = null;
	}
	
	
	/**
	 * Adds a position which is associated with the connected positions
	 * <code>conPos</code> and the direction it was approached from.
	 * @param position the position.
	 * @param conPos a ConnectedPositions instance.
	 * @param dirFrom a Direction.
	 * @return <code>true</code> if and only if the position has not 
	 * been added, <code>false</code> otherwise.
	 */
	public boolean addPosition(Position position, ConnectedPositions conPos,
			Direction dirFrom){
		if(!remMap.containsKey(position)){
			remMap.put(position,
					new RememberedPositionConnection(conPos, dirFrom));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds a position in conjunction with a direction to
	 * be remembered in the case of when backtracking is necessary.
	 * 
	 * @param position the Position to be remembered.
	 * @param dirFrom a Direction.
	 */
	public void rememberBackTrackDirection(Position position,
			Direction dirFrom){
		remDir.add(new DirectionHolder(position, dirFrom));
	}
	
	/**
	 * Removes a direction in conjunction with a direction.
	 * This method may be called if certain directions from a 
	 * certain position is not to be visited again.
	 * 
	 * @param position the position.
	 * @param dir a Direction.
	 */
	public void removeDirection(Position position, Direction dir){
		RememberedPositionConnection remCon = remMap.get(position);
		
		if(remCon != null){
			remCon.remove(dir);
		}
	}
	
	/**
	 * Checks whether or not a certain direction from a given
	 * position is valid.
	 * 
	 * @param position the position.
	 * @param dir a Direction.
	 * @return <code>true</code> if and only if a connection exists
	 * with the given parameters, <code>false</code> otherwise.
	 */
	public boolean isValidDirection(Position position, Direction dir){
		RememberedPositionConnection remCon = remMap.get(position);
		boolean isValid = false;
		
		if(remCon != null){
			isValid = remCon.getMap().containsKey(dir);
		}
		
		return isValid;
	}
	
	
	/**
	 * Checks if a position has been visited from all possible
	 * directions.
	 * 
	 * @param position the position.
	 * @return <code>true</code> if and only if the position still
	 * has connections which has yet not been visited, <code>false</code>
	 * otherwise.
	 */
	public boolean positionExplored(Position position){
		RememberedPositionConnection remCon = remMap.get(position);
		boolean isExplored = false;
		
		if(remCon != null){
			isExplored = !remCon.hasConnections();
		}
		
		return isExplored;
	}
	
	
	/**
	 * Returns the direction of the next backtracking
	 * position.
	 * @return the direction of the next backtracking
	 * position.
	 */
	public Direction getBackTrackDirection(){
		if(!remDir.isEmpty()){
			return remDir.pop().getDirection();
		}
		return null;
	}
	
	/**
	 * Returns the direction of the next backtracking
	 * position with a specific position in consideration.
	 * @param position the position.
	 * @return the direction of the next backtracking
	 * position with a specific position in consideration.
	 */
	public Direction getBackTrackDirection(Position position){
		DirectionHolder holder = null;
		
		while(!remDir.isEmpty()){
			holder = remDir.pop();
			if(holder.getPosition().equals(position) &&
					!remDir.isEmpty()){
				return remDir.pop().getDirection();
			}
		}
		return null;
	}
	
	/**
	 * Sets the most recently visited ConnectedPositions,
	 * i.e., the position with more than 2 connections.
	 * 
	 * @param conPos a ConnectedPositions.
	 */
	public void setMostRecentCrossing(ConnectedPositions conPos){
		if(conPos.getNumberOfConnections() > 2){
			mostRecentCrossing = conPos.getPosition();
		}
	}
	
	/**
	 * Returns the position of the most recently visited
	 * crossing i.e., a position with more than 2 connections. 
	 * 
	 * @return the position of the most recently visited
	 * crossing i.e., a position with more than 2 connections. 
	 */
	public Position getMostRecentCrossing(){
		return mostRecentCrossing;
	}
	
}
