package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;

public class SquareCreatureFigure extends CreatureFigure {

	public static final Figures shape = Figures.SQUARE;

	public SquareCreatureFigure(int hue, float scale, Position position) {
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
	public void render(Graphics2D g2d) {
		g2d.setColor(this.getColor());
		g2d.fillRect(this.getPosition().getX()  - CreatureFigure.TEMP_SIZE/2,
				this.getPosition().getY() - CreatureFigure.TEMP_SIZE/2,
				CreatureFigure.TEMP_SIZE, CreatureFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape() {
		return shape;
	}

	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		double outerRadius = CreatureFigure.TEMP_SIZE / 2 * 1.415;
		double collPointAngle;
		double innerRadius;
		
		if(dx > outerRadius || dy > outerRadius){
			return false;
		}
		
		if(dy / dx <= 1){
			collPointAngle = Math.atan(dy / dx);
		}else{
			if(dx > 0){
				collPointAngle = 0;
			}else{
				collPointAngle = Math.PI / 2 - Math.atan(dy / dx);
			}
		}
		
		innerRadius = CreatureFigure.TEMP_SIZE / 
				(2 * Math.cos(collPointAngle));
		
		if(innerRadius >= Math.sqrt(Math.pow(dx, 2) +
				Math.pow(dy, 2))){
			return true;
		}
		
		return false;
	}
	
}
