package creatures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;





import java.util.Map;

import start.AreaPosition;
import start.Figures;
import start.GameLevel;
import start.GameRunner;
import start.Position;
import tiles.PathTile;
import tiles.PathTile.Direction;
import tiles.PathTile.ValidPath;
import tiles.TeleportTile;
import tiles.Tile;
import utilities.Action;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.Lock;
import utilities.TimerListener;

public abstract class CreatureFigure implements TimerListener{
	
	public enum Orientation{
		RIGHT, LEFT, RANDOM, FORWARD;
	}
	
	public static final int BASE_HITPOINTS = 100;
	public static final int BASE_SIZE = 30;
	public static final double BASE_SPEED = 1;

	private Color creatureColor;
	private Orientation orientation;
	private PathTile.Direction navigation;
	private PathTile.Direction navigationFrom;
	private PathMemory memory;
	private GameLevel level;
	private int hue;
	private double scale, speed, tilesMoved;
	private Position position;
	private int hitPoints;
	private int startHitPoints;
	private boolean isAlive;
	private boolean finished;
	private Lock lock;
	private ArrayList<Action> onSpawnActionList;
	private HashMap<Long, Action> onSpawnTimedActionMap;
	private ArrayList<Action> onDeathActionList;
	private volatile ArrayList<Long> notifiedTimedActions;
	private ActionTimer actionTimer;
	private ArrayList<Action> onActiveActionList;
	private boolean hasSpawned;
	private boolean hasReachedGoal;
	
	public CreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level, double speed){
		this.hue = hue;
		this.scale = scale;
		this.position = position;
		this.creatureColor = ColorCreator.generateColorFromHue(hue);
		this.orientation = orientation;
		this.level = level;
		this.speed = speed;
		
		init();
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract boolean isCollision(Position position);
	public abstract Figures getShape();
	
	public void update(){
		//System.out.println(getNavigationFrom()+ ": "+ getPosition().toString());
		if(isAlive && !hasReachedGoal){
			if(!hasSpawned){
				for(Action action : this.onSpawnActionList){
					action.executeAction();
				}
				this.hasSpawned = true;
			}
			try{
				lock.lock();
				for(Long id : notifiedTimedActions){
					onSpawnTimedActionMap.get(id).executeAction();
				}
				notifiedTimedActions = new ArrayList<Long>();
			}catch(InterruptedException e){
			}finally{
				lock.unlock();
			}
	
			for(Action action : this.onActiveActionList){
				action.executeAction();
			}
			
			tilesMoved += speed;
			
		}else if(!isAlive && !finished){
			for(Action action : onDeathActionList){
				action.executeAction();
			}
			finished = true;
		}
	}
	
	public Color getColor(){
		return creatureColor;
	}
	
	public void setDamageTaken(int damage){
		if(!hasReachedGoal){
			hitPoints -= damage;
		}
		if(hitPoints <= 0){
			isAlive = false;
		}
		
	}
	
	public int getHitPoints(){
		return hitPoints;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	protected void remove(){
		isAlive = false;
		finished = true;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public int percentLife(){
		return Math.round((int)((double)hitPoints * 100 / (double)startHitPoints));
	}
	
	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + hue;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		CreatureFigure other = (CreatureFigure) obj;
		if(hue != other.hue){
			return false;
		}
		
		return true;
	}

	public void enableTeleport(Long time){
		final long id = level.getNewUniqueId();
		
		addOnSpawnTimedAction(time, () -> {
			performTeleportCreationAction(id, null);
		});
		
		addOnDeathAction(() -> {
			TeleportTile tile = (TeleportTile)level.getTileById(id);
			if(tile != null){
				performTeleportCreationAction(0, tile);
			}
		});
		
	}
	
	public void addOnSpawnAction(Action action){
		onSpawnActionList.add(action);
	}
	
	public void addOnSpawnTimedAction(Long time, Action action){
		long id;
		if(hasActionTimer()){
			id = actionTimer.getNewUniqueId();
			onSpawnTimedActionMap.put(id, action);
			actionTimer.setTimer(id, this, time);
		}
	}
	
	public void addOnDeathAction(Action action){
		onDeathActionList.add(action);
	}
	
	public void addActiveAction(Action action){
		onActiveActionList.add(action);
	}
	
	public ActionTimer getActionTimer(){
		return actionTimer;
	}

	public void setActionTimer(ActionTimer actionTimer){
		this.actionTimer = actionTimer;
	}
	
	public boolean hasActionTimer(){
		return actionTimer != null;
	}
	
	@Override
	public void receiveNotification(Long id){
		try{
			lock.lock();
			notifiedTimedActions.add(id);
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
		//id is the key in onSpawnTimerActionMap.
	}
	
	private void init(){
		isAlive = true;
		hasReachedGoal = false;
		finished = false;
		hasSpawned = false;
		onSpawnActionList = new ArrayList<Action>();
		onSpawnTimedActionMap = new HashMap<Long, Action>();
		onDeathActionList = new ArrayList<Action>();
		notifiedTimedActions = new ArrayList<Long>();
		onActiveActionList = new ArrayList<Action>();
		lock = new Lock();
		memory = new PathMemory();
		
		hitPoints = startHitPoints = (int)(BASE_HITPOINTS * scale);
		navigation = navigationFrom = PathTile.Direction.NA;
		
		tilesMoved = 1;
	}
	
	public int getHue(){
		return hue;
	}

	public double getScale(){
		return scale;
	}

	public void setScale(float scale){
		this.scale = scale;
	}

	public Position getPosition(){
		return position;
	}

	public void setPosition(Position position){
		this.position = position;
	}

	public Orientation getOrientation(){
		return orientation;
	}

	public void setOrientation(Orientation orientation){
		this.orientation = orientation;
	}
	
	public boolean hasReachedGoal(){
		return hasReachedGoal;
	}
	
	protected void setHasReachedGoal(boolean hasReachedGoal){
		this.hasReachedGoal = hasReachedGoal;
	}
	
	protected void setFinished(boolean finished){
		this.finished = finished;
	}
	
	public PathTile.Direction getNavigation(){
		return navigation;
	}
	
	public PathTile.Direction getNavigationFrom(){
		return navigationFrom;
	}
	
	public PathMemory getMemory(){
		return memory;
	}
	
	public void setNavigation(PathTile.Direction navigation){
		this.navigation = navigation;
		navigationFrom = Direction.getOpposite(navigation);
	}
	
	private void performTeleportCreationAction(final long id,
			final TeleportTile otherTeleportTile){
		Position figPos = new Position(getPosition().getX()
				+ Tile.size / 2, getPosition().getY()
				+ Tile.size / 2, Tile.size);
		Tile tile = level.getLevelMap()
				.get(figPos.toArea());
		Position tilePosition = tile.getPosition();
		ValidPath path;
		TeleportTile teleportTile;
		
		if(tile.walkable()){
			path = ((PathTile)tile).getValidPath();
			teleportTile = new TeleportTile(tilePosition, path);
			teleportTile.setTeleporterAt(getPosition());
			if(id != 0){
				teleportTile.setId(id);
			}
			if(otherTeleportTile != null){
				otherTeleportTile.setConnection(teleportTile);
				teleportTile.setConnection(otherTeleportTile);
			}
			level.changeTile(figPos.toArea(), 
					teleportTile);
			
		}
	}
	
	public int getTilesMoved(){
		return (int)tilesMoved;
	}
	
	public void resetTilesMoved(){
		tilesMoved = Math.round(tilesMoved) - tilesMoved;
	}
}
