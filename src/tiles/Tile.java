package tiles;

import java.awt.Graphics2D;

import start.Position;

/**
 * Skeleton by Alexander Beliaev.<br>
 * Edited by Jan Nylén, Alexander Ekstrom.<br>
 * Rewritten and edited by Alexander Beliaev.<br>
 * <br>
 * 
 * The abstract class Tile is used to build upon to
 * create tiles used in the AntiTD game.
 * 
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  
 */
public abstract class Tile{

	public static final int size = 100; 
	
	private Position position;
	private long id;
	private boolean selected;
	
	/**
	 * Creates a new Tile at the given position.
	 * 
	 * @param position a position.
	 */
	public Tile(Position position){
		this.position = position;
		id = 0;
	}
	
	/**
	 * Updates any logic related to a specific tile.
	 */
	public abstract void update();
	
	
	/**
	 * Renders the tile with the given Graphics2D object.
	 * 
	 * @param g2d the Graphics2D object.
	 */
	public abstract void render(Graphics2D g2d);
	
	
	/**
	 * Returns whether or not the Tile is traversable.
	 * 
	 * @return whether or not the Tile is traversable.
	 */
	public abstract boolean walkable();
	
	
	/**
	 * Returns whether or not the Tile can be built upon.
	 * 
	 * @return whether or not the Tile can be built upon.
	 */
	public abstract boolean buildable();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result 
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	//Setters and getters
	public Position getPosition(){
		return position;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public boolean isGoalPosition(Position position){
		return false;
	}
	
	public boolean isStartPosition(Position position){
		return false;
	}
	
	public boolean hasEffect(){
		return false;
	}
	
	public boolean isGoal(){
		return false;
	}
	
	public boolean isStart(){
		return false;
	}
	
	public boolean selectable(){
		return false;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
