package creatures;

import start.Figures;
import start.Position;

public class TriangleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.TRIANGLE;
	
	public TriangleCreatureFigure(int hue, float scale, Position position){
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
