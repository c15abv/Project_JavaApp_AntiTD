package towers;

import start.Figures;
import start.Position;

public class TriangleTowerFigure extends TowerFigure{

	public static final Figures SHAPE = Figures.TRIANGLE;
	
	public TriangleTowerFigure(int baseDamage, int hue, int range,
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
