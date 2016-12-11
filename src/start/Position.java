package start;

public class Position{

	private final int x, y, delta;
	
	public Position(int x, int y){
		this(x, y, 1);
	}
	
	public Position(Position position){
		this(position.getX(), position.getY(), 1);
	}
	
	public Position(Position position, int delta){
		this(position.getX(), position.getY(), delta);
	}
	
	public Position(int x, int y, int delta){
		this.x = x;
		this.y = y;
		this.delta = delta;
	}
	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public int getDelta(){
		return delta;
	}

	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + x;
		result += prime * result + y;
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
		Position other = (Position) obj;
		if(x != other.x){
			return false;
		}
		if(y != other.y){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return "x: " + x + "; y: " + y + "; " +
				super.toString();
	}
	
	public AreaPosition toArea(){
		int newX, newY;
		newX = (x / delta) * delta;
		newY = (y / delta) * delta;
		return new AreaPosition(newX, newY, delta, delta);
	}
}
