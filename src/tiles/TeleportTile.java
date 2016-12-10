package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Position;

public class TeleportTile extends PathTile implements EnterTileEffect{

	private static final int RENDER_COUNT_LIMIT = 100;
	private static final int SLEEP_RENDER_COUNT = 30;
	
	private TeleportTile connection;
	private int renderAnimationCount;
	private int sleepCount;
	private boolean sleepRender;
	
	public TeleportTile(Position position, ValidPath path){
		super(position, path);
		renderAnimationCount = sleepCount = 0;
		sleepRender = false;
	}

	@Override
	public void landOn(CreatureFigure creature){
		if(connection != null){
			creature.setPosition(connection.getPosition());
		}
	}

	@Override
	public void update(){
		if(sleepRender && sleepCount < SLEEP_RENDER_COUNT){
			sleepCount++;
		}else if(sleepRender && sleepCount >= SLEEP_RENDER_COUNT){
			sleepRender = false;
			renderAnimationCount = 0;
		}else if(!sleepRender && renderAnimationCount < RENDER_COUNT_LIMIT){
			renderAnimationCount++;
		}else if(!sleepRender && renderAnimationCount >= RENDER_COUNT_LIMIT){
			renderAnimationCount = RENDER_COUNT_LIMIT;
			sleepRender = true;
			sleepCount = 0;
		}
	}
	
	@Override
	public void render(Graphics2D g2d){
		Color color;
		float alpha;
		int size;
		
		super.render(g2d);
		
		alpha = 1.0f - (float)renderAnimationCount / 
				(float)RENDER_COUNT_LIMIT;
		size = Tile.size / 4 + (int)((Tile.size / 4) *
				(double)renderAnimationCount / (double)RENDER_COUNT_LIMIT);
		color = new Color(50, 50, 150, alpha);
		
		g2d.setColor(color);
		g2d.drawOval(getPosition().getX() - Tile.size / 2,
				getPosition().getY() - Tile.size / 2, size, size);
	}
	
	public void setConnection(TeleportTile connection){
		this.connection = connection;
	}

}
