package projectiles;

import start.Figures;
import start.Position;

public class TriangleProjectileFigure extends ProjectileFigure{
	
	public static final Figures shape = Figures.TRIANGLE;
	
	public TriangleProjectileFigure(int hue, float functioning, 
			Position position){
		super(hue, functioning, position);
	}

	@Override
	public void render(){
	}

	@Override
	public Figures getShape(){
		return shape;
	}
}
