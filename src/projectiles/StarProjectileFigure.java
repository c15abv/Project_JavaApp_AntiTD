package projectiles;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;
import utilities.CustomShapes;

public class StarProjectileFigure extends ProjectileFigure{

	public static final Figures shape = Figures.STAR;
	
	public StarProjectileFigure(int hue, Position position){
		super(hue, position);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fill(CustomShapes.createStar(this.getPosition(),
				ProjectileFigure.TEMP_SIZE));
	}

	@Override
	public Figures getShape(){
		return shape;
	}
}
