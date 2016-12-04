package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;
import utilities.CustomShapes;

public class TriangleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.TRIANGLE;
	
	public TriangleCreatureFigure(int hue, float scale, Position position){
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
		g2d.fill(CustomShapes.createTriangle(this.getPosition(),
				CreatureFigure.TEMP_SIZE));
	}

	@Override
	public Figures getShape(){
		return shape;
	}

	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		int outerRadius = CreatureFigure.TEMP_SIZE/2;
		double innerRadius;
		double collPointAngle;
		
		if(dx > outerRadius || dy > outerRadius){
			return false;
		}
		
		if(dx > 0){
			collPointAngle = Math.atan(dy / dx);
		}else{
			collPointAngle = Math.PI / 2;
		}
		
		innerRadius = getInnerRadius(collPointAngle);
		
		if(innerRadius >= Math.sqrt(Math.pow(dx, 2) +
				Math.pow(dy, 2))){
			return true;
		}
		
		
		return false;
	}
	
	private double getInnerRadius(double angle){
		return CreatureFigure.TEMP_SIZE/2 * Math.sin(Math.PI / 6) / 
				Math.sin(2 * Math.PI / 6 + angle);
	}

}
