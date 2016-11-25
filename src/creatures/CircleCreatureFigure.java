package creatures;

import start.Figures;
import start.Position;

public class CircleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.CIRCLE;
	
	public CircleCreatureFigure(int hue, float scale, Position position){
		super(hue, scale, position);
	}

	@Override
	public void update(){
	}

	@Override
	public void render(){
	}

	@Override
	public Figures getShape(){
		return shape;
	}
}
