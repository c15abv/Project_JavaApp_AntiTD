package tiles;

import start.Position;

public class PathTile extends Tile{
	
	private String tileType;
	private String landOnEffect;

	public PathTile(Position position) {
		super(position);
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

	@Override
	public void render() {
		//render black
	}

}
