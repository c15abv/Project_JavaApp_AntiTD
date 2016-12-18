package start;

/**
 * The Position class holds a x- and y-coordinate in 
 * conjunction with a delta value which in turn can
 * be used to generate an AreaPosition object.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class Position{

	private final int x, y, delta;
	
	/**
	 * Creates a Position object with the x- and y-
	 * coordinates.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 */
	public Position(int x, int y){
		this(x, y, 1);
	}
	
	/**
	 * Creates a new Position with an already existing
	 * Position.
	 * @param position a Position.
	 */
	public Position(Position position){
		this(position.getX(), position.getY(), 1);
	}
	
	/**
	 * Creates a new Position with an already existing
	 * Position, and with a delta value.
	 * @param position a Position.
	 * @param delta a delta value.
	 */
	public Position(Position position, int delta){
		this(position.getX(), position.getY(), delta);
	}
	
	/**
	 * Creates a Position object with the x- and y-
	 * coordinates, and with a delta value.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param delta a delta value.
	 */
	public Position(int x, int y, int delta){
		this.x = x;
		this.y = y;
		this.delta = delta;
	}
	
	//getters 
	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public int getDelta(){
		return delta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + x;
		result += prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Position other = (Position) obj;
		if(x != other.x){
			return false;
		}
		if(y != other.y){
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "x: " + x + "; y: " + y + "; " +
				super.toString();
	}
	
	
	/**
	 * Generates an AreaPosition with the delta of
	 * this Position.
	 * @return an AreaPosition with the delta of
	 * this Position.
	 */
	public AreaPosition toArea(){
		int newX, newY;
		newX = (x / delta) * delta;
		newY = (y / delta) * delta;
		return new AreaPosition(newX, newY, delta, delta);
	}
}
