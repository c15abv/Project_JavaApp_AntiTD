package tiles;

import start.Position;

public abstract class BuildableTile extends Tile{

	private volatile boolean isOccupied;
	
	public BuildableTile(Position position){
		super(position);
		isOccupied = false;
	}

	@Override
	public boolean buildable(){
		return true;
	}
	
	@Override
	public boolean walkable(){
		return false;
	}
	
	public synchronized boolean occupied(){
		return isOccupied;
	}
	
	public synchronized void setIsOccupied(boolean isOccupied){
		this.isOccupied = isOccupied;
	}
}
