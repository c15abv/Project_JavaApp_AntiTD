package creatures;

import start.Figures;
import start.Position;

public class SquareCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.SQUARE;
	
	public SquareCreatureFigure(int hue, float scale, Position position){
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
