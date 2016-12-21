package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Position;

/**
 * Authored by Jan Nylén, Alexander Ekstrom<br>
 * Rewritten and edited by Alexander Beliaev<br>
 * <br>
 * 
 * The GoalTile class holds the position of a goal.
 * It may be used in conjunction with any regular 
 * path. However, the goal position will always be 
 * at the very centre of the PathTile.
 * 
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *  
 */
public class GoalTile extends PathTile{
	
	private static final int RENDER_COUNT_LIMIT = 50;
	private static final int SLEEP_RENDER_COUNT = 30;
	
	private int renderAnimationCount;
	private int sleepCount;
	private boolean sleepRender;

	/**
	 * Creates a new GoalTile with the given position
	 * and path type.
	 * 
	 * @param position a position.
	 * @param path the path type.
	 */
	public GoalTile(Position position, ValidPath path){
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
		color = new Color((float)125/255, (float)150/255, (float)25/255, alpha);
		
		g2d.setColor(color);
		g2d.drawOval(getPosition().getX() - size / 2, getPosition().getY() - size / 2, size, size);
	}
	
	/* (non-Javadoc)
	 * @see tiles.Tile#isGoalPosition(start.Position)
	 */
	@Override
	public boolean isGoalPosition(Position position){
		return getPosition().equals(position);
	}
	
	/* (non-Javadoc)
	 * @see tiles.Tile#isGoal()
	 */
	@Override
	public boolean isGoal(){
		return true;
	}

}
