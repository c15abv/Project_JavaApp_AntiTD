package tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import start.Position;

/**
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  A void tile used representing areas where towers are allowed to be build.
 */
public class VoidTile extends BuildableTile{
	
	private Font font;
	private Color color;
	
	public VoidTile(Position position) {
		super(position);
		font = new Font("Monospaced", Font.PLAIN, 20);
		color = getColor();
	}
	
	@Override
	public void update(){}
	
	@Override
	public void render(Graphics2D g2d) {
		if(!this.occupied()){
			g2d.setColor(color);
			g2d.setFont(font); 
			g2d.drawString("X", this.getPosition().getX() - 5,
					this.getPosition().getY() + 5);
		}
	}
	
	private static final Color getColor(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.2f);
	}
	
}
