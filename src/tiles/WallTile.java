package tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import start.Position;

/**
 * Authored by Jan Nyl�n, Alexander Ekstrom.<br>
 * Rewritten and edited by Alexander Beliaev.<br>
 * <br>
 * 
 * WallTiles are used to represent the end points of all levels. 
 * 
 * @author Alexander Beliaev, Jan Nyl�n, Alexander Ekstrom
 * 
 */
public class WallTile extends Tile{
	
	private Color[] colors1 = {getColor1(), getColor2()};
	private float[] distance = {0.0f, 1.0f};
	private RadialGradientPaint paint1;

	public WallTile(Position position){
		super(position);
		paint1 = new RadialGradientPaint(
				new Point2D.Float(getPosition().getX(),
						getPosition().getY()), Tile.size / 2,
		        		 distance, colors1);
	}	
	
	@Override
	public void update(){}

	@Override
	public void render(Graphics2D g2d){
		g2d.setPaint(paint1);
		g2d.fillRect(getPosition().getX() - size / 2, getPosition().getY() - size / 2, size, size);
	}
	
	@Override
	public boolean walkable(){
		return false;
	}

	@Override
	public boolean buildable(){
		return false;
	}
	
	private static final Color getColor1(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.2f);
	}
	
	private static final Color getColor2(){
		return new Color((float)200/255, (float)200/255, (float)200/255, 0f);
	}
}
