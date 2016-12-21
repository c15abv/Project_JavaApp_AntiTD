package towers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import creatures.CreatureFigure;
import projectiles.ProjectileFigure;
import start.Figures;
import start.Position;
import utilities.Action;
import utilities.ActionTimer;
import utilities.ColorCreator;
import utilities.TimerListener;

/**
 * TowerFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public abstract class TowerFigure implements TimerListener{
	
	public static final int SIZE = 80;
	public static final int COOLDOWN = 1000;
	
	private int baseDamage, hue, range, cooldown, killed;
	private Color towerColor;
	private Position position;
	private boolean isOnCooldown;
	private Action towerAction;
	private CreatureFigure currentTarget;
	private TimerListener specifedTimerListener;
	protected HashMap<ProjectileFigure, CreatureFigure> projectiles;
	private ActionTimer actionTimer;
	
	public TowerFigure(int baseDamage, int hue, int range, 
			int cooldown, Position position){
		this.baseDamage = baseDamage;
		this.hue = hue;
		this.range = range;
		this.cooldown = cooldown;
		this.position = position;
		towerColor = ColorCreator.generateColorFromHue(hue);
		projectiles = new HashMap<ProjectileFigure, CreatureFigure>();
		actionTimer = null;
		
		killed = 0;
		
		setDefaultBehaviour();
	}
	
	public ActionTimer getActionTimer() {
		return actionTimer;
	}

	public void setActionTimer(ActionTimer actionTimer) {
		this.actionTimer = actionTimer;
	}
	
	public boolean hasActionTimer(){
		return actionTimer != null;
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

	protected void update(){
		if(!isOnCooldown && currentTarget != null 
				&& towerAction != null){
			towerAction.executeAction();
		}
		
		Iterator<Entry<ProjectileFigure, CreatureFigure>> it = 
				projectiles.entrySet().iterator();
		Map.Entry<ProjectileFigure, CreatureFigure> pair;
	    while(it.hasNext()){
	    	pair = (Map.Entry<ProjectileFigure, CreatureFigure>)it.next();
	    	if(!pair.getKey().isAlive()){
	    		if(pair.getKey().killedTarget()){
	    			killed++;
	    		}
	    		it.remove();
	    	}else{
	    		pair.getKey().update(pair.getValue());
	    	}
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
	
	public void setIsOnCooldown(boolean isOnCooldown){
		this.isOnCooldown = isOnCooldown;
	}
	
	public abstract void render(Graphics2D g2d);
	public abstract Figures getShape();
	public abstract void attack();
	
	public void setTowerAction(Action action){
		towerAction = action;
	}
	
	public void setOnNotification(TimerListener listener){
		specifedTimerListener = listener;
	}
	
	public Position getPosition(){
		return position;
	}
	
	public int getKilled(){
		return killed;
	}
	
	@Override
	public void receiveNotification(Long id){
		if(specifedTimerListener != null){
			specifedTimerListener.receiveNotification(id);
		}
	}

	@Override
	public int hashCode(){
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

	public void resetDefaultBehaviour(){
		setDefaultBehaviour();
	}
	
	private void setDefaultBehaviour(){
		this.setTowerAction(() -> {
			if(hasActionTimer()){
				attack();
				isOnCooldown = true;
				actionTimer.setTimer(actionTimer.getNewUniqueId(),
						this, cooldown);
			}
		});
		
		this.setOnNotification(id -> {
			this.setIsOnCooldown(false);
		});
	}
}
