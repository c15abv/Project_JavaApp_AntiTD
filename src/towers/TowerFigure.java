package towers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import creatures.Action;
import creatures.CreatureFigure;
import projectiles.ProjectileFigure;
import start.Figures;
import start.Position;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.TimerListener;

public abstract class TowerFigure implements TimerListener{
	
	protected static final int TEMP_SIZE = 50;
	
	private int baseDamage;
	private int hue;
	private int range;
	private Color towerColor;
	private Position position;
	private boolean isOnCooldown;
	private Action towerAction;
	private CreatureFigure currentTarget;
	private TimerListener specifedTimerListener;
	private HashMap<ProjectileFigure, CreatureFigure> projectiles;
	private ActionTimer actionTimer;
	
	public ActionTimer getActionTimer() {
		return actionTimer;
	}

	public void setActionTimer(ActionTimer actionTimer) {
		this.actionTimer = actionTimer;
	}

	public TowerFigure(int baseDamage, int hue, int range, 
			Position position){
		this.baseDamage = baseDamage;
		this.hue = hue;
		this.range = range;
		this.position = position;
		towerColor = ColorCreator.generateColorFromHue(hue);
		projectiles = new HashMap<ProjectileFigure, CreatureFigure>();
	}
	
	public int getBaseDamage(){
		return baseDamage;
	}

	protected void setBaseDamage(int baseDamage){
		this.baseDamage = baseDamage;
	}

	public int getRange(){
		return range;
	}

	protected void setRange(int range){
		this.range = range;
	}

	public int getHue(){
		return hue;
	}
	
	public Color getColor(){
		return towerColor;
	}

	public boolean isOnCooldown(){
		return isOnCooldown;
	}

	protected HashMap<ProjectileFigure, CreatureFigure> getProjectiles() {
		return new HashMap<ProjectileFigure, CreatureFigure>(projectiles);
	}

	protected void update(){
		if(!isOnCooldown && currentTarget != null 
				&& towerAction != null){
			towerAction.executeAction();
		}
	}
	
	protected void setTarget(CreatureFigure target){
		currentTarget = target;
	}
	
	public CreatureFigure getTarget(){
		return currentTarget;
	}
	
	public boolean hasTarget(){
		return currentTarget != null;
	}
	
	protected void setIsOnCooldown(boolean isOnCooldown){
		this.isOnCooldown = isOnCooldown;
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract Figures getShape();
	protected abstract void attack();
	
	protected void setTowerAction(Action action){
		towerAction = action;
	}
	
	protected void setOnNotification(TimerListener listener){
		specifedTimerListener = listener;
	}
	
	public Position getPosition(){
		return position;
	}
	
	@Override
	public void receiveNotification(Integer id){
		if(specifedTimerListener == null){
			setIsOnCooldown(false);
		}else{
			specifedTimerListener.receiveNotification(id);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 92821;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : 
			position.hashCode());
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
		TowerFigure other = (TowerFigure) obj;
		if(position == null){
			if(other.position != null){
				return false;
			}
		}else if(!position.equals(other.position)){
			return false;
		}
		return true;
	}
	
	
}
