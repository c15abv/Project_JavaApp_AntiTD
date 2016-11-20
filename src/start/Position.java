package start;

public class Position{

	private int x;
	private int y;
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
}
