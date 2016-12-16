package tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import start.Position;

public class VoidTile extends BuildableTile{

	private Color[] colors1 = {getColor1(), getColor2()};
	private float[] distance = {0.0f, 1.0f};
	private RadialGradientPaint paint1;
	private Font font;
	
	public VoidTile(Position position) {
		super(position);
		paint1 = new RadialGradientPaint(
				new Point2D.Float(getPosition().getX(),
						getPosition().getY()), Tile.size / 2,
		        		 distance, colors1);
		font = new Font("Monospaced", Font.PLAIN, 20);
	}
	
	@Override
	public void update(){}
	
	@Override
	public void render(Graphics2D g2d) {
		if(!this.occupied()){
			g2d.setPaint(paint1);
			g2d.setFont(font); 
			g2d.drawString("X", this.getPosition().getX() - 5,
					this.getPosition().getY() + 5);
		}
	}
	
	private static final Color getColor1(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.2f);
	}
	
	private static final Color getColor2(){
		return new Color((float)200/255, (float)200/255, (float)200/255, 0f);
	}
}
