package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Position;

public class GoalTile extends Tile{
	
	private String tileType;

	public GoalTile(Position position) {
		super(position);
	}
	
	public GoalTile(Position position, String tileType){
		super(position);
		this.tileType = tileType;
	}	

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(this.getPosition().getX() - Tile.size/2,
				this.getPosition().getY() - Tile.size/2,
				Tile.size,
				Tile.size);
	}

}
