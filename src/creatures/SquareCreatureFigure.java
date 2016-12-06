package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.GameLevel;
import start.Position;

public class SquareCreatureFigure extends CreatureFigure {

	public static final Figures shape = Figures.SQUARE;

	public SquareCreatureFigure(int hue, float scale, Position position,
			Orientation orientation, GameLevel level){
		super(hue, scale, position, orientation, level);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(this.getColor());
		g2d.fillRect((int)(this.getPosition().getX() -
				(this.getScale() * CreatureFigure.BASE_SIZE / 2)),
				(int)(this.getPosition().getY() - 
						(this.getScale() * CreatureFigure.BASE_SIZE / 2)),
				(int)(this.getScale() * CreatureFigure.BASE_SIZE),
				(int)(this.getScale() * CreatureFigure.BASE_SIZE));
	}

	@Override
	public Figures getShape() {
		return shape;
	}

	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		double outerRadius = this.getScale() * CreatureFigure.BASE_SIZE / 2 * 1.415;
		double collPointAngle;
		double innerRadius;
		
		if(dx > outerRadius || dy > outerRadius){
			return false;
		}
		
		if(dx == 0){
			collPointAngle = 0;
		}else if(dy / dx <= 1){
			collPointAngle = Math.atan(dy / dx);
		}else{
			collPointAngle = Math.PI / 2 - Math.atan(dy / dx);
		}
		
		innerRadius = this.getScale()  * CreatureFigure.BASE_SIZE / 
				(2 * Math.cos(collPointAngle));
		
		if(innerRadius >= Math.sqrt(Math.pow(dx, 2) +
				Math.pow(dy, 2))){
			return true;
		}
		
		return false;
	}
	
}
