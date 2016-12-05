package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;

public class CircleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.CIRCLE;
	
	public CircleCreatureFigure(int hue, float scale, Position position){
		super(hue, scale, position);
	}

	public void moveForward() {
		if (this.isAlive()) {
			Position currentPosition = this.getPosition();

			Position newPosition = new Position(currentPosition.getX() + 1,
					currentPosition.getY() + 1);

			this.setPosition(newPosition);
		}
	}


	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval(this.getPosition().getX() - CreatureFigure.TEMP_SIZE/2,
				this.getPosition().getY() - CreatureFigure.TEMP_SIZE/2,
				CreatureFigure.TEMP_SIZE,
				CreatureFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return shape;
	}

	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		int radius = CircleCreatureFigure.TEMP_SIZE/2;
		
		if(dx > radius || dy > radius){
			return false;
		}
		if(dx + dy <= radius){
			return true;
		}
		if(Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(radius, 2)){
			return true;
		}
		
		return false;
	}
}
