package towers;

import java.util.HashMap;

import creatures.Action;
import creatures.CreatureFigure;
import projectiles.ProjectileFigure;
import start.Figures;
import start.Position;
import utilities.TimerListener;

public abstract class TowerFigure implements TimerListener{

	public static final int DEFAULT_BASE_DAMAGE = 0;
	
	private int baseDamage;
	private int hue;
	private int range;
	private Position position;
	private boolean isOnCooldown;
	private Action towerAction;
	private CreatureFigure currentTarget;
	
	private HashMap<ProjectileFigure, CreatureFigure> projectiles;
	
	public TowerFigure(int baseDamage, int hue, int range, 
			Position position){
		this.baseDamage = baseDamage;
		this.hue = hue;
		this.range = range;
		this.position = position;
		isOnCooldown = false;
	}
	
	public void update(){
		//if not on cooldown
		//call towerAction
		if(!isOnCooldown && currentTarget != null){
			towerAction.executeAction();
			isOnCooldown = true;
		}
	}
	
	public void setTarget(CreatureFigure target){
		currentTarget = target;
	}
	
	public CreatureFigure getTarget(){
		return currentTarget;
	}
	
	public abstract void render();
	public abstract Figures getShape();
	
	protected void setTowerAction(Action action){
		towerAction = action;
	}
	
	@Override
	public void receiveNotification(Integer id){
		isOnCooldown = false;
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
}
