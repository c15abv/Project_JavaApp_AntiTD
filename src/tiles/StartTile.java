package tiles;

import start.Position;

public class StartTile extends Tile{
	
	private String tileType;

	public StartTile(Position position) {
		super(position);
	}
	
	public StartTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	

	@Override
	public void render() {
		//render black
	}

}
