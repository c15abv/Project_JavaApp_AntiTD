package creatures;

import java.util.HashMap;
import java.util.Stack;

import start.Position;
import tiles.ConnectedPositions;
import tiles.PathTile.Direction;

public class PathMemory{
	
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
	
	private HashMap<Position, RememberedPositionConnection> remMap;
	private Stack<Direction> remDir;
	
	public PathMemory(){
		remMap = new HashMap<Position, RememberedPositionConnection>();
		remDir = new Stack<Direction>();
	}
	
	public boolean addPosition(Position position, ConnectedPositions conPos,
			Direction dirFrom){
		if(!remMap.containsKey(position)){
			remMap.put(position,
					new RememberedPositionConnection(conPos, dirFrom));
			return true;
		}
		
		return false;
	}
	
	public void rememberBackTrackDirection(Direction dirFrom){
		remDir.add(dirFrom);
	}
	
	public void removeDirection(Position position, Direction dir){
		RememberedPositionConnection remCon = remMap.get(position);
		
		if(remCon != null){
			remCon.remove(dir);
		}
	}
	
	public boolean isValidDirection(Position position, Direction dir){
		RememberedPositionConnection remCon = remMap.get(position);
		boolean isValid = false;
		
		if(remCon != null){
			isValid = remCon.getMap().containsKey(dir);
		}
		
		return isValid;
	}
	
	public boolean positionExplored(Position position){
		RememberedPositionConnection remCon = remMap.get(position);
		boolean isExplored = false;
		
		if(remCon != null){
			isExplored = !remCon.hasConnections();
		}
		
		return isExplored;
	}
	
	public Direction getBackTrackDirection(){
		if(!remDir.isEmpty()){
			return remDir.pop();
		}
		return null;
	}
	
}
