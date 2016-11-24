package towers;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;
import utilities.CustomShapes;

public class StarTowerFigure extends TowerFigure{

	public static final Figures SHAPE = Figures.STAR;
	
	public StarTowerFigure(int baseDamage, int hue, int range, 
			Position position) {
		super(baseDamage, hue, range, position);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fill(CustomShapes.createStar(this.getPosition(),
				TowerFigure.TEMP_SIZE));
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

}
