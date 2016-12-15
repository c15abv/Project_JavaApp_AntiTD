package tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import start.Position;
import utilities.CustomShapes;

public class VoidTile extends BuildableTile{

	private Color[] colors1 = {getColor1(), getColor3()};
	private Color[] colors2 = {getColor2(), getColor3()};
	private float[] distance = {0.0f, 1.0f};
	private RadialGradientPaint paint1, paint2;
	
	public VoidTile(Position position) {
		super(position);
		paint1 = new RadialGradientPaint(
				new Point2D.Float(getPosition().getX(),
						getPosition().getY()), Tile.size / 2,
		        		 distance, colors1);
		paint2 = new RadialGradientPaint(
				new Point2D.Float(getPosition().getX(),
						getPosition().getY()), Tile.size / 2,
		        		 distance, colors2);
	}
	
	@Override
	public void update(){}
	
	@Override
	public void render(Graphics2D g2d) {
		//g2d.setColor();
		if(!this.occupied()){
			g2d.setPaint(paint1);
			g2d.fill(CustomShapes.createStar(this.getPosition(),
					Tile.size));
			/*g2d.fillOval(this.getPosition().getX() - Tile.size/2,
					this.getPosition().getY() - Tile.size/2,
					Tile.size,
					Tile.size);*/
		}
	}
	
	private static final Color getColor1(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.2f);
	}
	
	private static final Color getColor2(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.4f);
	}
	
	private static final Color getColor3(){
		return new Color((float)200/255, (float)200/255, (float)200/255, 0f);
	}
}
