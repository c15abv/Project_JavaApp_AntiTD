package tiles;

import java.awt.Graphics2D;

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
	public void update(){}

	@Override
	public void render(Graphics2D g2d){}
	
	@Override
	public boolean walkable(){
		return false;
	}

	@Override
	public boolean buildable(){
		return false;
	}
}
