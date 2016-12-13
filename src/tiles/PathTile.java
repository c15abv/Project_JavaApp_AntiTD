package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.HashMap;

import creatures.CreatureFigure;
import creatures.CreatureFigure.Orientation;
import start.Position;

public class PathTile extends Tile implements EnterTileEffect{
	
	public enum ValidPath{
		HORIZONTAL, VERTICAL, HORIZONTAL_T_SOUTH,
		HORIZONTAL_T_NORTH, VERTICAL_T_EAST,
		VERTICAL_T_WEST, CROSSROAD,
		L_TURN_HORIZONTAL_SOUTH_TO_EAST, L_TURN_HORIZONTAL_NORTH_TO_WEST,
		L_TURN_HORIZONTAL_NORTH_TO_EAST, L_TURN_HORIZONTAL_SOUTH_TO_WEST,
		NORTH, WEST, EAST, SOUTH;
	}
	
	public enum Direction{
		NORTH, EAST, WEST, SOUTH, NA;
	}
	
	public class PositionConnection{
		private ConnectedPositions conPos;
		
		PositionConnection(Position position, Direction ... directions){
			conPos = new ConnectedPositions(position);
			
			for(Direction dir : directions){
				conPos.add(dir);
			}
		}
		
		public ConnectedPositions getConnectedPositions(){
			return conPos;
		}
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
	
	public boolean hasEffect(){
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
	public void landOn(CreatureFigure creature){}
	
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
			
			g.setColor(Color.WHITE);
			g.setStroke(stroke);
			if(start1 != null){
				g.drawLine(start1.getX(), start1.getY(), end1.getX(),
						end1.getY());
			}
			if(start2 != null){
				g.drawLine(start2.getX(), start2.getY(), end2.getX(),
						end2.getY());
			}
	    }finally{
	         g.dispose();
	    }
	}
	
	private void generateValidPath(){
		start1 = start2 = end1 = end2 = null;
		switch(path){
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
			generateLTurnEastToVertNorth();
			break;
		case L_TURN_HORIZONTAL_SOUTH_TO_WEST:
			generateLTurnWestToVertSouth();
			break;
		case L_TURN_HORIZONTAL_SOUTH_TO_EAST:
			generateLTurnEastToVertSouth();
			break;
		case L_TURN_HORIZONTAL_NORTH_TO_WEST:
			generateLTurnWestToVertNorth();
			break;
		default:
			break;
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
		
		generateHorizontal();
		generateSouthSingle();
		
		start1 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2 + 1,
				getPosition().getY());
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2);
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.SOUTH,
				Direction.EAST);
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateHorizontalTNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateHorizontal();
		generateNorthSingle();
		
		start1 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2 + 1,
				getPosition().getY());
		start2 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end2 = new Position(getPosition().getX(),
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.NORTH,
				Direction.EAST);
		
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateVerticalTWest(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateVertical();
		generateWestSingle();
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end1 = new Position(getPosition().getX(),
				getPosition().getY() + Tile.size / 2 + 1);
		start2 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		end2 = new Position(getPosition().getX(),
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.SOUTH,
				Direction.WEST,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateVerticalTEast(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateVertical();
		generateEastSingle();
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		end1 = new Position(getPosition().getX() ,
				getPosition().getY() + Tile.size / 2);
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.SOUTH,
				Direction.EAST,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateCrossroad(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateVertical();
		generateHorizontal();
		
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
	
	private void generateLTurnEastToVertNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateEastSingle();
		generateNorthSingle();
		
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
	}
	
	private void generateLTurnWestToVertNorth(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateWestSingle();
		generateNorthSingle();
		
		start1 = new Position(getPosition().getX(),
				getPosition().getY());
		end1 = new Position(getPosition().getX(),
				getPosition().getY() - Tile.size / 2);
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX() - Tile.size / 2,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.WEST,
				Direction.NORTH);
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateLTurnEastToVertSouth(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateEastSingle();
		generateSouthSingle();
		
		start2 = new Position(getPosition().getX(),
				getPosition().getY());
		end2 = new Position(getPosition().getX() ,
				getPosition().getY()  + Tile.size / 2);
		start1 = new Position(getPosition().getX(),
				getPosition().getY());
		end1 = new Position(getPosition().getX() + Tile.size / 2,
				getPosition().getY());
		
		tempPos = new Position(
				getPosition().getX(),
				getPosition().getY());
		tempPair = new PositionConnection(
				tempPos,
				Direction.EAST,
				Direction.SOUTH);
		positionPairMap.put(tempPos, tempPair);
	}
	
	private void generateLTurnWestToVertSouth(){
		PositionConnection tempPair;
		Position tempPos;
		
		generateWestSingle();
		generateSouthSingle();
		
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
	}
}
