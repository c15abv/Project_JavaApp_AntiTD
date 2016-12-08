package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Position;

public class TeleportTile extends Tile implements EnterTileEffect{

	public TeleportTile(Position position){
		super(position);
	}

	@Override
	public void landOn(CreatureFigure creature){
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.ORANGE);
		g2d.fillRect(this.getPosition().getX() - Tile.size/2,
				this.getPosition().getY() - Tile.size/2,
				Tile.size,
				Tile.size);
	}

}
