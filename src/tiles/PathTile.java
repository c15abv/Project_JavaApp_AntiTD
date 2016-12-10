package tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure;
import creatures.CreatureFigure.Orientation;
import start.Position;

public class PathTile extends Tile{
	
	public enum ValidPath{
		HORIZONTAL, VERTICAL, HORIZONTAL_T_SOUTH,
		HORIZONTAL_T_NORTH, VERTICAL_T_EAST,
		VERTICAL_T_WEST, WEST_T, EAST_T, CROSSROAD;
	}
	
	public enum Direction{
		NORTH, EAST, WEST, SOUTH, NA;
	}
	
	class PostionPair{
		
		HashMap<Direction, PositionNext> map;
		Position position;
		
		PostionPair(Position position, Direction ... directions){
			ArrayList<Direction> dirsTemp = new ArrayList<Direction>();
			this.position = position;
			
			for(Direction dir : directions){
				for(Direction dirTemp : directions){
					if(dir != dirTemp){
						dirsTemp.add(dirTemp);
					}
				}
				map.put(dir, create(dir, 
						dirsTemp.toArray(new Direction[dirsTemp.size()])));
			}
		}
		
		public PositionNext getPair(Direction dir){
			return map.get(dir);
		}
		
		private PositionNext create(Direction dirFrom, Direction ... dirTos){
			PositionNext posNext = new PositionNext();
			for(Direction dirTo : dirTos){
				switch(dirFrom){
				case EAST:
					switch(dirTo){
					case NORTH:
						posNext.setPosRight(Direction.EAST,
								new Position(position.getX(),
										position.getY() - 1));
						break;
					case SOUTH:
						posNext.setPosLeft(Direction.EAST,
								new Position(position.getX(),
										position.getY() + 1));
						break;
					case WEST:
						posNext.setPosForward(Direction.EAST,
								new Position(position.getX() - 1,
										position.getY()));
						break;
					default:
						break;
					}
					
				case NORTH:
					switch(dirTo){
					case EAST:
						posNext.setPosLeft(Direction.NORTH,
								new Position(position.getX() + 1,
										position.getY()));
						break;
					case SOUTH:
						posNext.setPosForward(Direction.NORTH,
								new Position(position.getX(),
										position.getY() + 1));
						break;
					case WEST:
						posNext.setPosRight(Direction.NORTH,
								new Position(position.getX() - 1,
										position.getY()));
						break;
					default:
						break;
					}
				case SOUTH:
					switch(dirTo){
					case EAST:
						posNext.setPosRight(Direction.SOUTH,
								new Position(position.getX() + 1,
										position.getY()));
						break;
					case NORTH:
						posNext.setPosForward(Direction.SOUTH,
								new Position(position.getX(),
										position.getY() - 1));
						break;
					case WEST:
						posNext.setPosLeft(Direction.SOUTH,
								new Position(position.getX() - 1,
										position.getY()));
						break;
					default:
						break;
					}
				case WEST:
					switch(dirTo){
					case EAST:
						posNext.setPosForward(Direction.EAST,
								new Position(position.getX() + 1,
										position.getY()));
						break;
					case NORTH:
						posNext.setPosLeft(Direction.EAST,
								new Position(position.getX(),
										position.getY() - 1));
						break;
					case SOUTH:
						posNext.setPosRight(Direction.EAST,
								new Position(position.getX(),
										position.getY() + 1));
						break;
					default:
						break;
					}
				default:
					break;
				}
			}
			
			return posNext;
		}
	}
	
	class PositionNext{
		
		private HashMap<Direction, HashMap<Orientation, Position>> posMap;
		private HashMap<Orientation, Position> rightMap;
		private HashMap<Orientation, Position> leftMap;
		private HashMap<Orientation, Position> forwardMap;
		
		PositionNext(){
			posMap = new HashMap<Direction, HashMap<Orientation, Position>>();
			rightMap = new HashMap<Orientation, Position>();
			leftMap = new HashMap<Orientation, Position>();
			forwardMap = new HashMap<Orientation, Position>();
		}
		
		void setPosRight(Direction dir, Position pos1){
			rightMap.put(Orientation.RIGHT, pos1);
			posMap.put(dir, rightMap);
		}
		
		void setPosLeft(Direction dir, Position pos2){
			leftMap.put(Orientation.LEFT, pos2);
			posMap.put(dir, leftMap);
		}
				
		void setPosForward(Direction dir, Position pos3){
			forwardMap.put(Orientation.FORWARD, pos3);
			posMap.put(dir, forwardMap);
		}
		
		Position getPos(Direction dir,
				Orientation orient){
			return posMap.get(dir).get(orient);
		}
		
		HashMap<Orientation, Position> getPosMap(Direction dir){
			return new HashMap<Orientation, Position>(posMap.get(dir));
		}
	}
	
	private HashMap<Position, PostionPair> positionPairMap;
	private String tileType;
	private String landOnEffect;
	private ValidPath path;

	public PathTile(Position position, ValidPath path) {
		super(position);
		this.path = path;
		positionPairMap = new HashMap<Position, PostionPair>();
		generateValidPath();
	}
	
	public PathTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	
	
	public PathTile(Position position, String tileType, String landOnEffect){
		super(position);
		this.tileType = tileType;
		this.landOnEffect = landOnEffect;
		
	}
	
	public Position getNextPosition(CreatureFigure figure){
		PostionPair pair = positionPairMap.get(figure.getPosition());
		PositionNext next = pair.getPair(figure.getNavigation());
		Position returnPos = null;
		HashMap<Orientation, Position> map;
		Direction direction = Direction.NA;
		
		switch(figure.getNavigation()){
		case EAST:
			direction = Direction.WEST;
			break;
		case NORTH:
			direction = Direction.SOUTH;
			break;
		case SOUTH:
			direction = Direction.NORTH;
			break;
		case WEST:
			direction = Direction.EAST;
			break;
		default:
			break;
		}
		
		map = next.getPosMap(direction);
		
		switch(figure.getOrientation()){
		case FORWARD:
			if((returnPos = map.get(Orientation.FORWARD)) == null){
				
			}
			break;
		case LEFT:
			if((returnPos = map.get(Orientation.LEFT)) == null){
				
			}
			break;
		case RANDOM:
			break;
		case RIGHT:
			if((returnPos = map.get(Orientation.RIGHT)) == null){
				
			}
			break;
		default:
			break;
		}
		
		return null;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(this.getPosition().getX() - Tile.size/2,
				this.getPosition().getY() - Tile.size/2,
				Tile.size,
				Tile.size);
	}
	
	
	
	private void generateValidPath(){
		switch(path){
		case EAST_T:
			generateEastT();
			break;
		case HORIZONTAL:
			generateHorizontal();
			break;
		case HORIZONTAL_T_NORTH:
			generateHorizontalTNorth();
			break;
		case HORIZONTAL_T_SOUTH:
			generateHorizontalTSouth();
			break;
		case VERTICAL:
			generateVertical();
			break;
		case VERTICAL_T_EAST:
			generateVerticalTEast();
			break;
		case VERTICAL_T_WEST:
			generateVerticalTWest();
			break;
		case WEST_T:
			generateWestT();
			break;
		case CROSSROAD:
			generateCrossroad();
			break;
		}
	}
	
	private void generateEastT(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			if(i == Tile.size / 2){
				tempPair = new PostionPair(
						tempPos,
						Direction.NORTH,
						Direction.WEST,
						Direction.SOUTH);
			}else{
				tempPair = new PostionPair(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			}
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateWestT(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == Tile.size / 2){
				tempPair = new PostionPair(
						tempPos,
						Direction.NORTH,
						Direction.EAST,
						Direction.SOUTH);
			}else{
				tempPair = new PostionPair(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			}
			
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontal(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST
						);
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVertical(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PostionPair(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH
						);
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontalTSouth(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			if(i > 25){
				tempPos = new Position(
						getPosition().getX() + 25,
						getPosition().getY() + i);
				tempPair = new PostionPair(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == 25){
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.SOUTH,
						Direction.EAST);
			}else{
				tempPair = new PostionPair(
							tempPos,
							Direction.WEST,
							Direction.EAST);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontalTNorth(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			if(i < Tile.size / 2){
				tempPos = new Position(
						getPosition().getX(),
						getPosition().getY() + i);
				tempPair = new PostionPair(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == Tile.size / 2){
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.NORTH,
						Direction.EAST);
			}else{
				tempPair = new PostionPair(
							tempPos,
							Direction.WEST,
							Direction.EAST);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVerticalTWest(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			if(i < Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			if(i == Tile.size / 2){
				tempPair = new PostionPair(
						tempPos,
						Direction.SOUTH,
						Direction.WEST,
						Direction.NORTH);
			}else{
				tempPair = new PostionPair(
							tempPos,
							Direction.SOUTH,
							Direction.NORTH);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVerticalTEast(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			if(i == Tile.size / 2){
				tempPair = new PostionPair(
						tempPos,
						Direction.SOUTH,
						Direction.EAST,
						Direction.NORTH);
			}else{
				tempPair = new PostionPair(
							tempPos,
							Direction.SOUTH,
							Direction.NORTH);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateCrossroad(){
		PostionPair tempPair;
		Position tempPos;
		
		for(int i=0; i <= Tile.size; i++){
			
			if(i == Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY() + i);
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST,
						Direction.NORTH,
						Direction.SOUTH);
				positionPairMap.put(tempPos, tempPair);
			}else{
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY());
				tempPair = new PostionPair(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
				
				tempPos = new Position(
						getPosition().getX(),
						getPosition().getY() + i);
				tempPair = new PostionPair(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
				positionPairMap.put(tempPos, tempPair);
			}
		}
	}

}
