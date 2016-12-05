package start;

import java.awt.Graphics2D;

public abstract class Player{
	
	private int credits;
	
	public Player(int credits){
		this.credits = credits;
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
	
	public void setCredits(int credits){
		this.credits = credits;
	}
	
	public int getCredits() {
		return credits;
	}
}
