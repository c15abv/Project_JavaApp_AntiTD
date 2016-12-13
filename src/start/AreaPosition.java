package start;

public class AreaPosition extends Position{

	private final int length, height;
	
	public AreaPosition(Position position){
		this(position.getX(), position.getY(), position.getDelta(),
				position.getDelta());
	}
	
	public AreaPosition(Position position, int length, int height){
		this(position.getX(), position.getY(), length, height);
	}
	
	public AreaPosition(int x, int y, int length, int height){
		super(x, y);
		this.length = length;
		this.height = height;
	}
	
	public static boolean withinArea(Position inside, Position outside,
			int length, int height){
		return outside.getX() < inside.getX() + length / 2 &&
				outside.getX() >= inside.getX() - length / 2 &&
				outside.getY() < inside.getY() + length / 2 &&
				outside.getY() >= inside.getY() - length / 2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + getX();
		result += prime * result + getY();
		return result;
	}
	
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
