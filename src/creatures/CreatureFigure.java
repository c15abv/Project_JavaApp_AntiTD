package creatures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import start.Figures;
import start.GameLevel;
import start.Position;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.Lock;
import utilities.TimerListener;

public abstract class CreatureFigure implements TimerListener{
	
	public enum Orientation{
		RIGHT, LEFT, RANDOM;
	}
	
	public enum Direction{
		FORWARD, BACKWARD, RANDOM;
	}
	
	public static final int BASE_HITPOINTS = 100;
	public static final int BASE_SIZE = 30;

	private Color creatureColor;
	private Orientation orientation;
	private GameLevel level;
	private Direction direction;
	private int hue;
	private double scale;
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
			Orientation orientation, GameLevel level){
		this.hue = hue;
		this.scale = scale;
		this.position = position;
		this.creatureColor = ColorCreator.generateColorFromHue(hue);
		this.orientation = orientation;
		this.level = level;
		
		init();
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract boolean isCollision(Position position);
	public abstract Figures getShape();
	
	public void update(){
		if(isAlive && !hasReachedGoal){
			if(!hasSpawned){
				for(Action action : this.onSpawnActionList){
					action.executeAction();
				}
				this.hasSpawned = true;
			}
			
			if(false/*level.isGoalTile(position)*/){
				hasReachedGoal = true;
				finished = true;
			}else{
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
				
				/*if(direction == Direction.BACKWARD){
					if(level.isStartTile(position)){
						direction = Direction.FORWARD;
					}else{
						position = level.getPreviousPosition(position,
								orientation);
					}
				}
				
				if(direction == Direction.FORWARD){
					position = level.getNextPosition(position,
							orientation);
				}*/
			}
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
		 hitPoints -= damage;
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
	
	public boolean isFinished(){
		return finished;
	}
	
	public int percentLife(){
		return Math.round(hitPoints / startHitPoints * 100);
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

	public void addOnSpawnAction(Action action){
		onSpawnActionList.add(action);
	}
	
	public void addOnSpawnTimedAction(int time, Action action){
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
		
		hitPoints = startHitPoints = (int)(BASE_HITPOINTS * scale);
		direction = Direction.FORWARD;
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

	public Direction getDirection(){
		return direction;
	}

	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	public boolean hasReachedGoal(){
		return hasReachedGoal;
	}
	
}
