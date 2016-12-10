package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure.Orientation;
import start.Position;

public class PathTile extends Tile{
	
	public enum ValidPath{
		HORIZONTAL, VERTICAL, HORIZONTAL_T_SOUTH,
		HORIZONTAL_T_NORTH, VERTICAL_T_EAST,
		VERTICAL_T_WEST, WEST_T, EAST_T, CROSSROAD,
		L_TURN_HORIZONTAL_SOUTH_TO_EAST, L_TURN_HORIZONTAL_NORTH_TO_WEST,
		L_TURN_HORIZONTAL_NORTH_TO_EAST, L_TURN_HORIZONTAL_SOUTH_TO_WEST,
		NORTH, WEST, EAST, SOUTH;
	}
	
	public enum Direction{
		NORTH, EAST, WEST, SOUTH, NA;
	}
	
	public class ConnectedPositions{
		private HashMap<Direction, Position> map;
		private Position position;
		
		ConnectedPositions(Position position){
			this.position = position;
			map = new HashMap<Direction, Position>();
		}
		
		void add(Direction dirTo){
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
		
	}
	
	public class PositionConnection{
		/*HashMap<Direction, PositionNext> map;
		private Position position;*/
		private ConnectedPositions conPos;
		
		PositionConnection(Position position, Direction ... directions){
			conPos = new ConnectedPositions(position);
			
			for(Direction dir : directions){
				conPos.add(dir);
			}
			
		}
			
			/*ArrayList<Direction> dirsTemp = new ArrayList<Direction>();
			map = new HashMap<Direction, PositionNext>();
			this.position = position;
			
			for(Direction dir : directions){
				for(Direction dirTemp : directions){
					if(dir != dirTemp){
						dirsTemp.add(dirTemp);
					}
				}
				
				if(dirsTemp.isEmpty()){
					map.put(dir, create(Direction.NA, dir));
				}else{
					map.put(dir, create(dir, 
						dirsTemp.toArray(new Direction[dirsTemp.size()])));
				}
			}
		}*/
		
		public ConnectedPositions getConnectedPositions(){
			return conPos;
		}
		
		/*public PositionNext getConnection(Direction dirFrom){
			return map.get(dirFrom);
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
					case NA:
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
					case NA:
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
					case NA:
					default:
						break;
					}
				case WEST:
					switch(dirTo){
					case EAST:
						posNext.setPosForward(Direction.WEST,
								new Position(position.getX() + 1,
										position.getY()));
						break;
					case NORTH:
						posNext.setPosLeft(Direction.WEST,
								new Position(position.getX(),
										position.getY() - 1));
						break;
					case SOUTH:
						posNext.setPosRight(Direction.WEST,
								new Position(position.getX(),
										position.getY() + 1));
						break;
					case NA:
					default:
						break;
					}
				case NA:
				default:
					switch(dirTo){
					case EAST:
						posNext.setPos(Direction.EAST,
								new Position(position.getX() + 1,
										position.getY()));
						break;
					case NORTH:
						posNext.setPos(Direction.NORTH,
								new Position(position.getX(),
										position.getY() - 1));
						break;
					case SOUTH:
						posNext.setPos(Direction.SOUTH,
								new Position(position.getX(),
										position.getY() + 1));
						break;
					case WEST:
						posNext.setPos(Direction.WEST,
								new Position(position.getX() - 1,
										position.getY()));
						break;
					default:
						break;
					}
					break;
				}
			}
			
			if(posNext.isEmpty())
				System.out.println("is empty: " + posNext.isEmpty() + "; " + posNext);
			
			return posNext;
		}*/
	}
	
	public class PositionNext{
		private HashMap<Direction, HashMap<Orientation, Position>> posMap;
		private HashMap<Orientation, Position> rightMap;
		private HashMap<Orientation, Position> leftMap;
		private HashMap<Orientation, Position> forwardMap;
		
		public PositionNext(){
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
		
		void setPos(Direction dir, Position pos){
			forwardMap.put(Orientation.FORWARD, pos);
			leftMap.put(Orientation.LEFT, pos);
			rightMap.put(Orientation.RIGHT, pos);
			posMap.put(dir, forwardMap);
			posMap.put(dir, leftMap);
			posMap.put(dir, rightMap);
		}
		
		public Position getPos(Direction dir,
				Orientation orient){
			return posMap.get(dir).get(orient);
		}
		
		public HashMap<Orientation, Position> getPosMap(Direction dir){
			if(posMap.get(dir) == null){
				return null;
			}
			return new HashMap<Orientation, Position>(posMap.get(dir));
		}
		
		public boolean isEmpty(){
			return posMap.isEmpty();
		}
	}
	
	private Position start1, start2, end1, end2;
	
	private HashMap<Position, PositionConnection> positionPairMap;
	private String tileType;
	private String landOnEffect;
	private ValidPath path;

	public PathTile(Position position, ValidPath path){
		this(position, null, null, path);
	}
	
	public PathTile(Position position, String tileType, ValidPath path){
		this(position, tileType, null, path);
	}	
	
	public PathTile(Position position, String tileType, String landOnEffect,
			ValidPath path){
		super(position);
		this.tileType = tileType;
		this.landOnEffect = landOnEffect;
		this.path = path;
		positionPairMap = new HashMap<Position, PositionConnection>();
		generateValidPath();
	}
	
	@Override
	public boolean walkable(){
		return true;
	}
	
	@Override
	public boolean buildable(){
		return false;
	}
	
	public boolean isGoalPosition(Position position){
		return false;
	}
	
	public boolean isStartPosition(Position position){
		return false;
	}
	
	public PositionConnection getPosPair(Position position){
		PositionConnection pair;
		return (pair = positionPairMap.get(position)) == null ? 
				positionPairMap.get(getPosition()) : pair;
	}

	@Override
	public void update(){}
	
	@Override
	public void render(Graphics2D g2d) {
		float[] dashingPattern1 = {2f, 2f};
		Stroke stroke = new BasicStroke(2f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
		 
		final Graphics2D g = (Graphics2D)g2d.create();
	    try{
	    	g.setColor(Color.BLACK);
			g.fillRect(this.getPosition().getX() - Tile.size/2,
					this.getPosition().getY() - Tile.size/2,
					Tile.size,
					Tile.size);
			
			g.setColor(Color.RED);
			g.setStroke(stroke);
			if(start1 != null){
				g.drawLine(start1.getX(), start1.getY(), end1.getX(), end1.getY());
			}
			if(start2 != null){
				g.drawLine(start2.getX(), start2.getY(), end2.getX(), end2.getY());
			}
	    }finally{
	         g.dispose();
	    }
	}
	
	private void generateValidPath(){
		start1 = start2 = end1 = end2 = null;
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
		case EAST:
			generateEastSingle();
			break;
		case NORTH:
			generateNorthSingle();
			break;
		case SOUTH:
			generateSouthSingle();
			break;
		case WEST:
			generateWestSingle();
			break;
		case L_TURN_HORIZONTAL_NORTH_TO_EAST:
			generateLTurnEastToHorNorth();
			break;
		case L_TURN_HORIZONTAL_SOUTH_TO_WEST:
			generateLTurnWestToHorSouth();
			break;
		case L_TURN_HORIZONTAL_SOUTH_TO_EAST:
			generateLTurnEastToHorSouth();
			break;
		case L_TURN_HORIZONTAL_NORTH_TO_WEST:
			generateLTurnWestToHorNorth();
			break;
		default:
			break;
		}
	}
	
	private void generateEastT(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX() + Tile.size - 1,
				getPosition().getY() + Tile.size / 2);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size - 1);
		
		for(int i=0; i <= Tile.size; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.WEST,
						Direction.SOUTH);
			}else{
				tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			}
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateWestT(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size - 1);
		
		for(int i=0; i <= Tile.size; i++){
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.EAST,
						Direction.SOUTH);
			}else{
				tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			}
			
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontal(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		for(int i = -Tile.size / 2; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY());
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVertical(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2 + 1);
		
		for(int i = -Tile.size / 2; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH);
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontalTSouth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX() + Tile.size - 1,
				getPosition().getY() + Tile.size / 2);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size - 1);
		
		for(int i=0; i <= Tile.size; i++){
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + Tile.size / 2,
						getPosition().getY() + i);
				tempPair = new PositionConnection(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.SOUTH,
						Direction.EAST);
			}else{
				tempPair = new PositionConnection(
							tempPos,
							Direction.WEST,
							Direction.EAST);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateHorizontalTNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX() + Tile.size - 1,
				getPosition().getY() + Tile.size / 2);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		for(int i=0; i <= Tile.size; i++){
			if(i < Tile.size / 2){
				tempPos = new Position(
						getPosition().getX(),
						getPosition().getY() + i);
				tempPair = new PositionConnection(
						tempPos,
						Direction.SOUTH,
						Direction.NORTH);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.NORTH,
						Direction.EAST);
			}else{
				tempPair = new PositionConnection(
							tempPos,
							Direction.WEST,
							Direction.EAST);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVerticalTWest(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size - 1);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX(),
				getPosition().getY()  + Tile.size / 2);
		
		for(int i=0; i <= Tile.size; i++){
			if(i < Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.SOUTH,
						Direction.WEST,
						Direction.NORTH);
			}else{
				tempPair = new PositionConnection(
							tempPos,
							Direction.SOUTH,
							Direction.NORTH);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateVerticalTEast(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size - 1);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX() + Tile.size - 1,
				getPosition().getY()  + Tile.size / 2);
		
		for(int i=0; i <= Tile.size; i++){
			if(i > Tile.size / 2){
				tempPos = new Position(
						getPosition().getX() + i,
						getPosition().getY()
							+ Tile.size / 2);
				tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
				positionPairMap.put(tempPos, tempPair);
			}
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			if(i == Tile.size / 2){
				tempPair = new PositionConnection(
						tempPos,
						Direction.SOUTH,
						Direction.EAST,
						Direction.NORTH);
			}else{
				tempPair = new PositionConnection(
							tempPos,
							Direction.SOUTH,
							Direction.NORTH);
			}
				
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateCrossroad(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() - Tile.size / 2 - 1,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2 + 1,
				getPosition().getY());
		start2 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end2 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.EAST,
				Direction.NORTH,
				Direction.SOUTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY());
			tempPair = new PositionConnection(
					tempPos,
					Direction.WEST,
					Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PositionConnection(
					tempPos,
					Direction.NORTH,
					Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
		
		for(int i = - Tile.size / 2; i < Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY());
			tempPair = new PositionConnection(
					tempPos,
					Direction.WEST,
					Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PositionConnection(
					tempPos,
					Direction.NORTH,
					Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateEastSingle(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2 - 1,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.EAST);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY());
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateWestSingle(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX(),
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() - i,
					getPosition().getY());
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateNorthSingle(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end1 = new Position(getPosition().getX(),
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() - i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateSouthSingle(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY());
		end1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.SOUTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateLTurnEastToHorNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end1 = new Position(getPosition().getX(),
				getPosition().getY());
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.EAST,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 0; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i + 1,
					getPosition().getY());
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() - i - 1);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateLTurnWestToHorNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX(),
				getPosition().getY()  + Tile.size / 2);
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 0; i < Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY() + Tile.size / 2);
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX() + Tile.size / 2,
					getPosition().getY() + i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateLTurnEastToHorSouth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end2 = new Position(getPosition().getX() + Tile.size - 1,
				getPosition().getY()  + Tile.size / 2);
		start1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY()  + Tile.size - 1);
		
		tempPos = new Position(
				getPosition().getX() + Tile.size / 2,
				getPosition().getY() + Tile.size / 2);
		tempPair = new PositionConnection(
				tempPos,
				Direction.EAST,
				Direction.SOUTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = Tile.size / 2 + 1; i <= Tile.size; i++){
			tempPos = new Position(
					getPosition().getX() + i,
					getPosition().getY() + Tile.size / 2);
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX() + Tile.size / 2,
					getPosition().getY() + i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
	
	private void generateLTurnWestToHorSouth(){
		PositionConnection tempPair;
		Position tempPos;
		
		start1 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX(),
				getPosition().getY());
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX(),
				getPosition().getY()  + Tile.size / 2);
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.SOUTH);
		positionPairMap.put(tempPos, tempPair);
		
		for(int i = 1; i <= Tile.size / 2; i++){
			tempPos = new Position(
					getPosition().getX() - i,
					getPosition().getY());
			tempPair = new PositionConnection(
						tempPos,
						Direction.WEST,
						Direction.EAST);
			positionPairMap.put(tempPos, tempPair);
			
			tempPos = new Position(
					getPosition().getX(),
					getPosition().getY() + i);
			tempPair = new PositionConnection(
						tempPos,
						Direction.NORTH,
						Direction.SOUTH);
			positionPairMap.put(tempPos, tempPair);
		}
	}
}
