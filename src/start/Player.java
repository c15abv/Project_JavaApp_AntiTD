package start;

import java.awt.Graphics2D;

public abstract class Player{
	
	private int credits;
	private GameLevel level;
	
	public Player(int credits, GameLevel level){
		this.credits = credits;
		this.level = level;
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
	
	public void setCredits(int credits){
		this.credits = credits;
	}
	
	public int getCredits() {
		return credits;
	}
	
	public GameLevel getLevel(){
		return level;
	}
}
