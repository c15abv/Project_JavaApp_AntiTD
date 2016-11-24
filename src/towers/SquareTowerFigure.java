package towers;

import java.awt.Graphics2D;

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
		g2d.fillRect(this.getPosition().getX(),
				this.getPosition().getY(),
				TowerFigure.TEMP_SIZE,
				TowerFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

}
