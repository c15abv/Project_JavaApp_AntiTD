package start;

public class Position{

	private int x;
	private int y;
	
	public Position(int x, int y){
		setX(x);
		setY(y);
	}

	public int getX(){
		return x;
	}

	private void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	private void setY(int y){
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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
}
