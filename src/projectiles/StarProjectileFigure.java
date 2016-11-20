package projectiles;

import start.Figures;
import start.Position;

public class StarProjectileFigure extends ProjectileFigure{

	public static final Figures shape = Figures.STAR;
	
	public StarProjectileFigure(int hue, float functioning, 
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
