package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Position;

/**
 * Authored by Jan Nylén, Alexander Ekstrom.<br>
 * Rewritten and edited by Alexander Beliaev.<br>
 * <br>
 * 
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  
 */
public class StartTile extends PathTile{
	
	private static final int RENDER_COUNT_LIMIT = 50;
	private static final int SLEEP_RENDER_COUNT = 30;
	
	private int renderAnimationCount;
	private int sleepCount;
	private boolean sleepRender;

	
	/**
	 * Creates a new StartTile with the given position
	 * and path type.
	 * @param position
	 * @param path
	 */
	public StartTile(Position position, ValidPath path){
		super(position, path);
		renderAnimationCount = sleepCount = 0;
		sleepRender = false;
	}	

	/* (non-Javadoc)
	 * @see tiles.PathTile#update()
	 */
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
	
	/* (non-Javadoc)
	 * @see tiles.PathTile#render(java.awt.Graphics2D)
	 */
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
		color = new Color((float)225/255, 0, (float)80/255, alpha);
		
		g2d.setColor(color);
		g2d.drawOval(getPosition().getX() - size / 2, getPosition().getY() - size / 2, size, size);
	}
	
	/* (non-Javadoc)
	 * @see tiles.Tile#isStartPosition(start.Position)
	 */
	@Override
	public boolean isStartPosition(Position position){
		return getPosition().equals(position);
	}
	
	/**
	 * Returns a valid starting direction from the
	 * start position. The order in which the methods
	 * check the directions are: east -> west -> south -> north.
	 * If no directions are valid Direction.NA is returned.
	 * 
	 * @return a valid direction.
	 */
	public Direction getStartingDirection(){
		if(getValidPath() == ValidPath.EAST){
			return Direction.EAST;
		}else if(getValidPath() == ValidPath.WEST){
			return Direction.WEST;
		}else if(getValidPath() == ValidPath.SOUTH){
			return Direction.SOUTH;
		}else if(getValidPath() == ValidPath.NORTH){
			return Direction.NORTH;
		}else{
			return Direction.NA;
		}
	}
	
	@Override
	public boolean isStart(){
		return true;
	}
	
	
	
}
