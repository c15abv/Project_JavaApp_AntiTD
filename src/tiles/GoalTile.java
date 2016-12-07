package tiles;

import start.Position;

public class GoalTile extends Tile{
	
	private String tileType;

	public GoalTile(Position position) {
		super(position);
	}
	
	public GoalTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	

	@Override
	public void render() {
		//render black
	}

}
