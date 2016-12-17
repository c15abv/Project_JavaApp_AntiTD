package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.AreaPosition;
import start.Position;

public class TeleportTile extends PathTile{

	private static final int RENDER_COUNT_LIMIT = 99;
	private static final int RENDER_COUNT_LIMIT2 = 33;
	private static final int SLEEP_RENDER_COUNT = 20;
	
	private TeleportTile connection;
	private int renderAnimationCount, renderAnimationCount2;
	private int sleepCount, sleepCount2;
	private boolean sleepRender, sleepRender2;
	private Position teleportPosition;
	
	public TeleportTile(Position position, ValidPath path){
		super(position, path);
		renderAnimationCount = renderAnimationCount2 =
				sleepCount = sleepCount2 = 0;
		sleepRender = sleepRender2 = false;
		teleportPosition = position;
	}

	@Override
	public void landOn(CreatureFigure creature){
		if(connection != null && creature.getPosition()
				.equals(getTeleportPosition())){
			creature.setPosition(connection.getTeleportPosition());
		}
	}
	
	@Override
	public boolean hasEffect(){
		return true;
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
		
		if(sleepRender2 && sleepCount2 < SLEEP_RENDER_COUNT){
			sleepCount2++;
		}else if(sleepRender2 && sleepCount2 >= SLEEP_RENDER_COUNT){
			sleepRender2 = false;
			renderAnimationCount2 = 0;
		}else if(!sleepRender2 && renderAnimationCount2 < RENDER_COUNT_LIMIT2){
			renderAnimationCount2++;
		}else if(!sleepRender2 && renderAnimationCount2 >= RENDER_COUNT_LIMIT2){
			renderAnimationCount2 = RENDER_COUNT_LIMIT2;
			sleepRender2 = true;
			sleepCount2 = 0;
		}
	}
	
	@Override
	public void render(Graphics2D g2d){
		Color color;
		float alpha, alpha2;
		int size, size2;
		
		super.render(g2d);
		
		color = Color.GRAY;
		
		alpha = 1.0f - (float)renderAnimationCount / 
				(float)RENDER_COUNT_LIMIT;
		alpha2 = 1.0f - (float)renderAnimationCount2 / 
				(float)RENDER_COUNT_LIMIT2;
		size = (3 * Tile.size) / 4 - (int)((3 * Tile.size) / 4 *
				(double)renderAnimationCount / (double)RENDER_COUNT_LIMIT);
		size2 = Tile.size / 4 + (int)((Tile.size / 4) *
				(double)renderAnimationCount2 / (double)RENDER_COUNT_LIMIT2);
		if(connection != null)
			color = new Color((float)85/255, (float)175/255, (float)230/255, alpha);
		
		g2d.setColor(color);
		g2d.drawOval(teleportPosition.getX() - size / 2,
				teleportPosition.getY() - size / 2, size, size);
		
		if(connection != null)
			color = new Color((float)85/255, (float)175/255, (float)230/255, alpha2);
		g2d.setColor(color);
		g2d.drawOval(teleportPosition.getX() - size2 / 2,
				teleportPosition.getY() - size2 / 2, size2, size2);
	}
	
	public void setConnection(TeleportTile connection){
		this.connection = connection;
	}
	
	public void setTeleporterAt(Position position){
		if(position != null && AreaPosition.withinArea(getPosition(),
				position, Tile.size, Tile.size)){
			teleportPosition = position;
		}
	}
	
	public Position getTeleportPosition(){
		return teleportPosition;
	}
}