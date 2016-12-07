package tiles;

import start.Position;

public class TowerTile extends Tile{
	
	private String tileType;

	public TowerTile(Position position) {
		super(position);
	}
	
	public TowerTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	

	@Override
	public void render() {
		//render black
	}

}
