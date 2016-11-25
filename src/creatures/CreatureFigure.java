package creatures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import start.Figures;
import start.Position;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.TimerListener;

public abstract class CreatureFigure implements TimerListener{
	protected static final int TEMP_SIZE = 50;

	private Color creatureColor;
	private int hue;
	private float scale;
	private Position position;
	private int hitPoints;
	private boolean isAlive;
	private ArrayList<Action> onPermanentActiveActionList;
	protected ArrayList<Action> onSpawnActionList;
	private HashMap<Integer, Action> onSpawnTimedActionMap;
	private ArrayList<Action> onDeathActionList;
	private volatile ArrayList<Integer> notifiedTimedActions;
	private ActionTimer actionTimer;
	protected ArrayList<Action> onActiveActionList;
	protected int hasSpawned;
	
	public CreatureFigure(int hue, float scale, Position position){
		this.hue = hue;
		this.scale = scale;
		this.position = position;
		this.creatureColor = ColorCreator.generateColorFromHue(hue);
		
		this.onActiveActionList = new ArrayList<>();
		this.onSpawnActionList = new ArrayList<>();
		this.hasSpawned = 0;

		init();
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
	public abstract Figures getShape();
	
	public Color getColor(){
		return creatureColor;
	}
	
	public void setDamageTaken(int damage){
		 hitPoints -= damage;
		 if(hitPoints <= 0){
			 isAlive = false;
			 //if can be resurrected (tile effect -> aura)
			 //clean non-permanent action data structures
		 }
	}
	
	public int getHitPoints(){
		return hitPoints;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
	
	protected void addPermanentOnSpawnAction(Action action){
		onSpawnActionList.add(action);
	}
	
	protected void addPermanentOnSpawnTimedAction(Integer id, Action action){
		onSpawnTimedActionMap.put(id, action);
		onSpawnActionList.add(action);
	}
	
	protected void addPermanentOnDeathAction(Action action){
		onDeathActionList.add(action);
	}
	
	protected void addOnSpawnAction(Action action){
		onSpawnActionList.add(action);
	}
	
	protected void addOnSpawnTimedAction(Integer id, Action action){
	}
	
	protected void addOnDeathAction(Action action){
	}
	
	protected void addPermanentActiveAction(Action action){
		onPermanentActiveActionList.add(action);
	}
	
	public ActionTimer getActionTimer() {
		return actionTimer;
	}

	public void setActionTimer(ActionTimer actionTimer) {
		this.actionTimer = actionTimer;
	}

	protected void addPermanentActiveTimedAction(Action action){
	}
	
	protected void addActiveAction(Action action){
		onActiveActionList.add(action);
	}
	
	protected void addActiveTimedAction(Action action){
	}
	
	@Override
	public void receiveNotification(Integer id){
		notifiedTimedActions.add(id);
		//id is the key in onSpawnTimerActionMap.
	}
	
	private void init(){
		isAlive = true;
		onSpawnActionList = new ArrayList<Action>();
		onSpawnTimedActionMap = new HashMap<Integer, Action>();
		onDeathActionList = new ArrayList<Action>();
		notifiedTimedActions = new ArrayList<Integer>();
	}
	
	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
