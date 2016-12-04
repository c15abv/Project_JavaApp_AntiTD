package towers;

import java.util.ArrayList;

import creatures.CreatureFigure;
import start.AttackingPlayer;
import start.Figures;
import start.Position;
import utilities.ColorCreator;

public class AITowerFigures{

	
	private AttackingPlayer attacker;
	private DefendingPlayer defender;
	
	private ArrayList<CreatureFigure> currentHorde;
	private ArrayList<TowerFigure> currentDefence;
	
	public AITowerFigures(AttackingPlayer attacker,
			DefendingPlayer defender){
		this.attacker = attacker;
		this.defender = defender;
	}
	
	public void update(){
		currentHorde = attacker.getHorde();
		currentDefence = defender.getDefence();
		
		//fixates when finds target;
		//keep this feature?
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
	
	public static double getTargetDistance(Position towerPosition,
			Position targetPosition){
		return Math.sqrt(Math.pow(towerPosition.getY() -
				targetPosition.getY(), 2) + Math.pow(towerPosition.getX() - 
						targetPosition.getX(), 2));
	}
	
	private boolean betterCompared(TowerFigure tower,
			CreatureFigure oldTarget,
			CreatureFigure newTarget){
		if(oldTarget == null){
			return true;
		}
		int oldDiffHue = ColorCreator.getHueDiff(tower.getHue(),
				oldTarget.getHue());
		int newDiffHue = ColorCreator.getHueDiff(tower.getHue(),
				newTarget.getHue());
		int oldLife = oldTarget.percentLife();
		int newLife = newTarget.percentLife();
		
		if(tower.getShape() != Figures.STAR && 
				oldTarget.getShape() != tower.getShape() &&
				newTarget.getShape() == tower.getShape()){
			return true;
		}else if(tower.getShape() == Figures.STAR ||
				newTarget.getShape() == tower.getShape() ||
				(oldTarget.getShape() != tower.getShape() &&
				newTarget.getShape() != tower.getShape())){
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
		}
		
		return false;
	}
	
	
}
