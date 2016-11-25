package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;
import towers.TowerFigure;

public class SquareCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.SQUARE;
	
	public SquareCreatureFigure(int hue, float scale, Position position){
		super(hue, scale, position);
	}

	@Override
	public void update(){
		
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillRect(this.getPosition().getX(),
				this.getPosition().getY(),
				CreatureFigure.TEMP_SIZE,
				CreatureFigure.TEMP_SIZE);
	}
	
	@Override
	public Figures getShape(){
		return shape;
	}

}
