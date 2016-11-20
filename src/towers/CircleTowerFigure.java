package towers;

import start.Figures;
import start.Position;

public class CircleTowerFigure extends TowerFigure{
	
	public static final Figures SHAPE = Figures.SQUARE;
	
	public CircleTowerFigure(int baseDamage, int hue, int range,
			Position position){
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
