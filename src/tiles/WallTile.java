package tiles;

import start.Position;

public class WallTile extends Tile{
	
	private String tileType;

	public WallTile(Position position) {
		super(position);
	}
	
	public WallTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	

	@Override
	public void render() {
		//render black
	}

}
