package towers;

import java.util.ArrayList;

import creatures.CreatureFigure;
import start.Position;

public class AITowerFigures{

	private ArrayList<CreatureFigure> currentHorde;
	
	public AITowerFigures(ArrayList<CreatureFigure> currentHorde){
		this.currentHorde = currentHorde;
	}
	
	public void update(ArrayList<TowerFigure> currentDefence){
		for(TowerFigure tower : currentDefence){
			if(!tower.isOnCooldown() && (!tower.hasTarget() || 
					!tower.getTarget().isAlive() ||
					!targetWithinRange(tower, tower.getTarget()))){
				findTarget(tower);
			}
			
			tower.update();
		}
	}
	
	private void findTarget(TowerFigure tower){
		CreatureFigure tempTarget = null;
		
		for(CreatureFigure creature : currentHorde){
			if(targetWithinRange(tower, creature) &&
					betterCompared(tower, tempTarget, creature)){
				tempTarget = creature;
			}
		}
		
		tower.setTarget(tempTarget);
	}
	
	private boolean targetWithinRange(TowerFigure tower,
			CreatureFigure target){
		return tower.getRange() >= getTargetDistance(tower.getPosition(),
				target.getPosition());
	}
	
	private double getTargetDistance(Position towerPosition,
			Position targetPosition){
		return Math.sqrt(Math.pow(towerPosition.getY() -
				targetPosition.getY(), 2) + Math.pow(towerPosition.getX() - 
						targetPosition.getX(), 2));
	}
	
	private boolean betterCompared(TowerFigure tower,
			CreatureFigure oldTarget,
			CreatureFigure newTarget){
		int oldDiffHue = Math.abs(tower.getHue() - oldTarget.getHue());
		int newDiffHue = Math.abs(tower.getHue() - newTarget.getHue());
		int oldLife = oldTarget.percentLife();
		int newLife = newTarget.percentLife();
		
		if(oldDiffHue > 30 && oldDiffHue > newDiffHue){
			return true;
		}
		
		if(oldDiffHue - 10 > newDiffHue){
			return true;
		}else if(oldDiffHue - 5 > newDiffHue && oldLife >= newLife){
			return true;
		}
		
		if(oldDiffHue > newDiffHue && oldDiffHue - 5 <= newDiffHue && 
				oldLife - 40 >= newLife){
			return true;
		}
		
		return false;
	}
}
