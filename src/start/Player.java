package start;

public abstract class Player{
	
	private int credits;
	
	public Player(int credits){
		this.credits = credits;
	}
	
	public abstract void update();
	public abstract void render();
	
	public void setCredits(int credits){
		this.credits = credits;
	}
	
	public int getCredits() {
		return credits;
	}
}
