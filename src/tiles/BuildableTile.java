package tiles;

import start.Position;

/**
 * The abstract class BuildableTile may be
 * inherited to define a certain Tile to be buildable.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public abstract class BuildableTile extends Tile{

	private volatile boolean isOccupied;
	
	/**
	 * Creates a new BuildableTile, initialized
	 * as free to build on.
	 * 
	 * @param position
	 */
	public BuildableTile(Position position){
		super(position);
		isOccupied = false;
	}
	
	//setters and getters

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
