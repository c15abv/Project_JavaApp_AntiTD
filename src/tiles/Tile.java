package tiles;

import java.awt.Graphics2D;

import start.Position;

public abstract class Tile{

	private Position position;
	public static final int size = 50; 
	
	public Tile(Position position){
		this.position = position;
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
	public abstract boolean walkable();
	public abstract boolean buildable();
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
}
