package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Position;

public class VoidTile extends Tile{

	public VoidTile(Position position) {
		super(position);
	}
	

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.GREEN);
		g2d.fillRect(this.getPosition().getX() - Tile.size/2,
				this.getPosition().getY() - Tile.size/2,
				Tile.size,
				Tile.size);
	}

}
