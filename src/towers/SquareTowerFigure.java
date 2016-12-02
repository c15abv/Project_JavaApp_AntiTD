package towers;

import java.awt.Graphics2D;

import projectiles.SquareProjectileFigure;
import start.Figures;
import start.Position;

public class SquareTowerFigure extends TowerFigure{

	public static final Figures SHAPE = Figures.SQUARE;
	
	public SquareTowerFigure(int baseDamage, int hue, int range,
			Position position) {
		super(baseDamage, hue, range, position);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillRect(this.getPosition().getX() - TowerFigure.TEMP_SIZE/2,
				this.getPosition().getY() - TowerFigure.TEMP_SIZE/2,
				TowerFigure.TEMP_SIZE,
				TowerFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

	@Override
	protected void attack() {
		if(this.hasTarget()){
			this.getProjectiles().put(new SquareProjectileFigure(
					this.getHue(), 1,
					new Position(this.getPosition())),
					this.getTarget());
		}
		
	}

}
