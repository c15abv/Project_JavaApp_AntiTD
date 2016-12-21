package projectiles;

import java.awt.Color;
import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;
import towers.AITowerFigures;
import utilities.ColorCreator;

/**
 * ProjectileFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public abstract class ProjectileFigure{
	
	public static final int TEMP_SIZE = 10;
	public static final int SPEED = 6;
	
	private int hue;
	private Position position;
	private int updates;
	private boolean isAlive;
	private Color color;
	protected boolean killedTarget;
	
	public ProjectileFigure(int hue, Position position){
		this.hue = hue;
		this.position = position;
		updates = 0;
		isAlive = true;
		color = ColorCreator.generateColorFromHue(hue);
		killedTarget = false;
	}
	
	public void update(CreatureFigure figure){
		int deltaX, deltaY;
		double scaleX, scaleY, newX, newY;
		double speed = SPEED;
		Position position = figure.getPosition();
		double distance = AITowerFigures.getTargetDistance(this.position, 
				position);
		
		if(++updates < 30){
			speed *= (double)updates/(double)30;
		}
		
		if(distance <= speed){
			this.position = new Position(position);
		}else{
			deltaX = this.position.getX() - position.getX();
			deltaY = this.position.getY() - position.getY();
			scaleX = deltaX / distance * (-1);
			scaleY = deltaY / distance * (-1);
			newX = this.position.getX() + speed * scaleX;
			newY = this.position.getY() + speed * scaleY;
			
			this.position = new Position((int)newX, (int)newY);
		}
	}
	
	public static double calculateDamageMultiplier(Figures shape, Figures targetShape, 
			int hue, int targetHue){
		double multiplier;
		double hueDiffMultiplier;
		int hueDiff;
		if(shape == targetShape){
			multiplier = 1;
		}else if(shape == Figures.STAR){
			multiplier = 0.55;
		}else{
			multiplier = 0.20;
		}
		
		hueDiff = ColorCreator.getHueDiff(hue, targetHue);
		hueDiffMultiplier = (double)(360 - hueDiff) / (double)360;
		
		return multiplier * hueDiffMultiplier;
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract Figures getShape();
	
	public Position getPosition(){
		return position;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	protected void setIsDead(){
		isAlive = false;
	}
	
	public int getHue(){
		return hue;
	}
	
	public Color getColor(){
		return color;
	}
	
	public boolean killedTarget(){
		return killedTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + hue;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectileFigure other = (ProjectileFigure) obj;
		if (hue != other.hue)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
}
