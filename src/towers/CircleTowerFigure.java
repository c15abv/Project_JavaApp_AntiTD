package towers;

import java.awt.Graphics2D;

import projectiles.CircleProjectileFigure;
import start.Figures;
import start.Position;

public class CircleTowerFigure extends TowerFigure{
	
	public static final Figures SHAPE = Figures.SQUARE;
	
	public CircleTowerFigure(int baseDamage, int hue, int range,
			Position position){
		super(baseDamage, hue, range, position);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval(this.getPosition().getX() - TowerFigure.TEMP_SIZE/2,
				this.getPosition().getY() - TowerFigure.TEMP_SIZE/2,
				TowerFigure.TEMP_SIZE,
				TowerFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

	@Override
	protected void attack(){
		if(this.hasTarget()){
			this.getProjectiles().put(new CircleProjectileFigure(
					this.getHue(), 1,
					new Position(this.getPosition())),
					this.getTarget());
		}
	}
	
	

}
