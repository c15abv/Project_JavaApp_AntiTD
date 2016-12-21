package tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import start.Position;

/**
 * Skeleton by Alexander Beliaev.<br>
 * Edited by Jan Nylén, Alexander Ekstrom.<br>
 * Rewritten and edited by Alexander Beliaev.<br>
 * <br>
 * 
 * The VoidTile is a buildable tile.
 * 
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  
 */
public class VoidTile extends BuildableTile{
	
	private Font font;
	private Color color;
	
	public VoidTile(Position position) {
		super(position);
		font = new Font("Monospaced", Font.PLAIN, 20);
		color = getColor();
	}
	
	/* (non-Javadoc)
	 * @see tiles.Tile#update()
	 */
	@Override
	public void update(){}
	
	/* (non-Javadoc)
	 * @see tiles.Tile#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d) {
		if(!this.occupied()){
			g2d.setColor(color);
			g2d.setFont(font); 
			g2d.drawString("X", this.getPosition().getX() - 5,
					this.getPosition().getY() + 5);
		}
	}
	
	//A color used with the tile
	private static final Color getColor(){
		return new Color((float)220/255, (float)200/255, (float)120/255, 0.2f);
	}
	
}
