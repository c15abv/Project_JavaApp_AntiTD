package creatures;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Figures;
import start.GameLevel;
import start.Position;

/**
 * A square CreatureFigure.
 * 
 * @author Alexander Believ
 * @version 1.0
 */
public class SquareCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.SQUARE;

	/**
	 * See CreatureFigure constructor.
	 */
	public SquareCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level){
		this(hue, scale, position, orientation, level, BASE_SPEED);
	}
	
	/**
	 * See CreatureFigure constructor.
	 */
	public SquareCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level, double speed){
		super(hue, scale, position, orientation, level, speed);
	}
	
	/* (non-Javadoc)
	 * @see creatures.CreatureFigure#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(getColor());
		g2d.fillRect((int)(getPosition().getX() -
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
