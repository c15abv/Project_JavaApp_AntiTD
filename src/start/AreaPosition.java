package start;

/**
 * The AreaPosition class describes all possible positions
 * counting from the a specific position and outward (delta).
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class AreaPosition extends Position{

	private final int length, height;
	
	/**
	 * Creates an AreaPosition with a given Position.
	 * The delta value is fetched from the Position object.
	 * 
	 * @param position a Position.
	 */
	public AreaPosition(Position position){
		this(position.getX(), position.getY(), position.getDelta(),
				position.getDelta());
	}
	
	/**
	 * Creates an AreaPosition with a given Position, length
	 * and height.
	 * 
	 * @param position a Position.
	 * @param length the length.
	 * @param height the height.
	 */
	public AreaPosition(Position position, int length, int height){
		this(position.getX(), position.getY(), length, height);
	}
	
	/**
	 * Creates an AreaPosition with a given x- and y-coordinate, length
	 * and height.
	 * 
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @param length the length.
	 * @param height the height.
	 */
	public AreaPosition(int x, int y, int length, int height){
		super(x, y);
		this.length = length;
		this.height = height;
	}
	
	/**
	 * Checks if a certain position is within a certain area.
	 * 
	 * @param inside position in the middle of the area.
	 * @param outside position to be checked.
	 * @param length the length of the area.
	 * @param height the height of the area.
	 * @return
	 */
	public static boolean withinArea(Position inside, Position outside,
			int length, int height){
		return outside.getX() < inside.getX() + length / 2 &&
				outside.getX() >= inside.getX() - length / 2 &&
				outside.getY() < inside.getY() + length / 2 &&
				outside.getY() >= inside.getY() - length / 2;
	}
	
	/* (non-Javadoc)
	 * @see start.Position#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + getX();
		result += prime * result + getY();
		return result;
	}
	
	/* (non-Javadoc)
	 * @see start.Position#equals(java.lang.Object)
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
		AreaPosition other = (AreaPosition) obj;
		if(this.getX() != other.getX()){
			return false;
		}
		if(this.getY() != other.getY()){
			return false;
		}
		if(length != other.length){
			return false;
		}
		if(height != other.height){
			return false;
		}
		
		return true;
	}
	
	
	
}
