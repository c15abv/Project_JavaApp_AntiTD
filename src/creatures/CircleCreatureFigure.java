package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.GameLevel;
import start.Position;

public class CircleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.CIRCLE;
	
	public CircleCreatureFigure(int hue, float scale, Position position,
			Orientation orientation, GameLevel level){
		super(hue, scale, position, orientation, level);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval((int)(this.getPosition().getX() -
				(this.getScale() * CreatureFigure.BASE_SIZE / 2)),
				(int)(this.getPosition().getY() - 
						(this.getScale() * CreatureFigure.BASE_SIZE / 2)),
				(int)(this.getScale() * CreatureFigure.BASE_SIZE),
				(int)(this.getScale() * CreatureFigure.BASE_SIZE));
	}

	@Override
	public Figures getShape(){
		return shape;
	}

	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		double radius = this.getScale() * CreatureFigure.BASE_SIZE / 2;
		
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
