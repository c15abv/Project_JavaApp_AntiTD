package tiles;

import java.awt.Graphics2D;

import start.Position;

public abstract class Tile{

	private Position position;
	public static final int size = 50; 
	
	public Tile(Position position){
		this.position = position;
	}
	
	public abstract void render(Graphics2D g2d);
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
	public Position getPosition(){
		return position;
	}
}
