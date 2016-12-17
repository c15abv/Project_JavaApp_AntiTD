package start;

public class Test3 {

	public static void main(String[] args){
		/*
		levelMap.put(new AreaPosition(0, 0, Tile.size, Tile.size), 
				new WallTile(new Position(0, 0)));
		levelMap.put(new AreaPosition(Tile.size, 0, Tile.size, Tile.size), 
				new WallTile(new Position(Tile.size, 0)));
		levelMap.put(new AreaPosition(2 * Tile.size, 0, Tile.size, Tile.size), 
				new WallTile(new Position(2 * Tile.size, 0)));
		levelMap.put(new AreaPosition(3 * Tile.size, 0, Tile.size, Tile.size), 
				new WallTile(new Position(3 * Tile.size, 0)));
		
		levelMap.put(new AreaPosition(0, Tile.size, Tile.size, Tile.size), 
				new WallTile(new Position(0, Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, Tile.size, Tile.size, Tile.size), 
				new StartTile(new Position(Tile.size, Tile.size), ValidPath.EAST));
		levelMap.put(new AreaPosition(2 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		levelMap.put(new AreaPosition(3 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(3 * Tile.size, Tile.size)));
		
		levelMap.put(new AreaPosition(0, 2 * Tile.size, Tile.size, Tile.size), 
				new WallTile(new Position(0, 2 * Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(Tile.size, 2 * Tile.size)));
		levelMap.put(new AreaPosition(2 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL));
		levelMap.put(new AreaPosition(3 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(3 * Tile.size, 2 * Tile.size)));
		
		levelMap.put(new AreaPosition(0, 3 * Tile.size, Tile.size, Tile.size), 
				new WallTile(new Position(0, 3 * Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(Tile.size, 3 * Tile.size)));
		levelMap.put(new AreaPosition(2 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, 3 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 3 * Tile.size), ValidPath.HORIZONTAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 3 * Tile.size), ValidPath.CROSSROAD));
		
		
		tele1 = new TeleportTile(new Position(5 * Tile.size, 3 * Tile.size), ValidPath.HORIZONTAL);
		levelMap.put(new AreaPosition(5 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				tele1);
		
		levelMap.put(new AreaPosition(4 * Tile.size, 4 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 4 * Tile.size), ValidPath.VERTICAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 5 * Tile.size), ValidPath.HORIZONTAL_T_NORTH));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 5 * Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 6 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
		
		levelMap.put(new AreaPosition(5 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(5 * Tile.size, 5 * Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		
		levelMap.put(new AreaPosition(5 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(5 * Tile.size, 6 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 6 * Tile.size), ValidPath.HORIZONTAL_T_SOUTH));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 7 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(4 * Tile.size, 7 * Tile.size), ValidPath.NORTH));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
		
		levelMap.put(new AreaPosition(6 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, 3 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
		
		levelMap.put(new AreaPosition(6 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL_T_EAST));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(7 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL_T_WEST));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 1 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(7 * Tile.size, 1 * Tile.size), ValidPath.SOUTH));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(7 * Tile.size, 3 * Tile.size), ValidPath.NORTH));
		
		tele2 = new TeleportTile(new Position(5 * Tile.size, Tile.size), ValidPath.HORIZONTAL);
		tele2.setConnection(tele1);
		tele1.setConnection(tele2);
		levelMap.put(new AreaPosition(5 * Tile.size, Tile.size, Tile.size, Tile.size), 
				tele2);
		
		levelMap.put(new AreaPosition(6 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		
	*/	
	}
}
