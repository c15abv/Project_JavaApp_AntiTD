package towers;

import start.Figures;
import start.Position;

public class SquareTowerFigure extends TowerFigure{

	public static final Figures SHAPE = Figures.SQUARE;
	
	public SquareTowerFigure(int baseDamage, int hue, int range,
			Position position) {
		super(baseDamage, hue, range, position);
	}

	@Override
	public void render(){
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

}
