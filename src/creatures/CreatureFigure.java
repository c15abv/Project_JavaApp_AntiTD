package creatures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import start.Figures;
import start.Position;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.Lock;
import utilities.TimerListener;

public abstract class CreatureFigure implements TimerListener{
	protected static final int TEMP_SIZE = 30;

	private Color creatureColor;
	private int hue;
	private float scale;
	private Position position;
	private int hitPoints;
	private int startHitPoints;
	private boolean isAlive;
	private boolean finished;
	private Lock lock;
	protected ArrayList<Action> onSpawnActionList;
	private HashMap<Integer, Action> onSpawnTimedActionMap;
	private ArrayList<Action> onDeathActionList;
	private volatile ArrayList<Integer> notifiedTimedActions;
	private ActionTimer actionTimer;
	protected ArrayList<Action> onActiveActionList;
	protected int hasSpawned;
	protected boolean hasReachedGoal;
	
	public CreatureFigure(int hue, float scale, Position position){
		this.hue = hue;
		this.scale = scale;
		this.position = position;
		this.creatureColor = ColorCreator.generateColorFromHue(hue);
		
		init();
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract boolean isCollision(Position position);
	public abstract Figures getShape();
	
	public void update(){
		if(isAlive){
			if(this.hasSpawned == 0){
				for(Action action : this.onSpawnActionList){
					action.executeAction();
				}
				this.hasSpawned++;
			}
			
			try{
				lock.lock();
				for(Integer id : notifiedTimedActions){
					onSpawnTimedActionMap.get(id).executeAction();
				}
				notifiedTimedActions = new ArrayList<Integer>();
			}catch(InterruptedException e){
			}finally{
				lock.unlock();
			}
	
			for(Action action : this.onActiveActionList){
				action.executeAction();
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatureFigure other = (CreatureFigure) obj;
		if (hue != other.hue)
			return false;
		return true;
	}

	protected void addOnSpawnAction(Action action){
		onSpawnActionList.add(action);
	}
	
	protected void addOnSpawnTimedAction(Integer id, Action action){
		onSpawnTimedActionMap.put(id, action);
	}
	
	protected void addOnDeathAction(Action action){
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
	
	@Override
	public void receiveNotification(Integer id){
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
		hasSpawned = 0;
		onSpawnActionList = new ArrayList<Action>();
		onSpawnTimedActionMap = new HashMap<Integer, Action>();
		onDeathActionList = new ArrayList<Action>();
		notifiedTimedActions = new ArrayList<Integer>();
		onActiveActionList = new ArrayList<Action>();
		lock = new Lock();
		
		hitPoints = startHitPoints = 100;
	}
	
	public int getHue(){
		return hue;
	}

	public void setHue(int hue){
		this.hue = hue;
	}

	public float getScale(){
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
}
