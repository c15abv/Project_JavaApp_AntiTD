package towers;

import java.awt.Graphics2D;

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
		g2d.fillOval(this.getPosition().getX(),
				this.getPosition().getY(),
				TowerFigure.TEMP_SIZE,
				TowerFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}
	
	

}
