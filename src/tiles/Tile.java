package tiles;

import start.Position;

public abstract class Tile{

	private Position position;
	private int size; //some size
	
	public Tile(Position position){
		this.position = position;
	}
	
	public abstract void render();
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
}
