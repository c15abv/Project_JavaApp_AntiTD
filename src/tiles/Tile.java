package tiles;

import java.awt.Graphics2D;

import start.Position;

/**
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  The abstract parent class for tiles that all tiles have to extend
 */
public abstract class Tile{

	public static final int size = 100; 
	
	private Position position;
	private long id;
	private boolean selected;
	
	public Tile(Position position){
		this.position = position;
		id = 0;
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
	public abstract boolean walkable();
	public abstract boolean buildable();
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result 
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

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
