package creatures;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Figures;
import start.GameLevel;
import start.Position;


/**
 * A circle CreatureFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class CircleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.CIRCLE;
	
	/**
	 * See CreatureFigure constructor.
	 */
	public CircleCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level){
		this(hue, scale, position, orientation, level, BASE_SPEED);
	}
	
	/**
	 * See CreatureFigure constructor.
	 */
	public CircleCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level, double speed){
		super(hue, scale, position, orientation, level, speed);
	}

	/* (non-Javadoc)
	 * @see creatures.CreatureFigure#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(getColor());
		g2d.fillOval((int)(getPosition().getX() -
				(getScale() * CreatureFigure.BASE_SIZE) / 2),
				(int)(getPosition().getY() - 
						(getScale() * CreatureFigure.BASE_SIZE) / 2),
				(int)(getScale() * CreatureFigure.BASE_SIZE),
				(int)(getScale() * CreatureFigure.BASE_SIZE));
		g2d.setColor(Color.WHITE);
		g2d.drawString(""+percentLife(), (int)(getPosition().getX() -
				(getScale() * CreatureFigure.BASE_SIZE) / 2), getPosition().getY());
	}

	/* (non-Javadoc)
	 * @see creatures.CreatureFigure#getShape()
	 */
	@Override
	public Figures getShape(){
		return shape;
	}

	/* (non-Javadoc)
	 * @see creatures.CreatureFigure#isCollision(start.Position)
	 */
	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - getPosition().getX());
		int dy = Math.abs(position.getY() - getPosition().getY());
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
