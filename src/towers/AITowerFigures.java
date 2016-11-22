package towers;

import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure;
import projectiles.ProjectileFigure;

public class AITowerFigures{

	private ArrayList<CreatureFigure> currentHorde;
	
	public AITowerFigures(ArrayList<CreatureFigure> currentHorde){
		this.currentHorde = currentHorde;
	}
	
	public void update(ArrayList<TowerFigure> currentDefence){
		/*run through currentDefence/towers;
		1. if no target link exists:
			+ choose an adequate target in currentHorde based on (priority):
				- range #if nothing within range: skip
				- hue
				- speed/hitpoints
			+ make a link between tower and creature by
			  inserting into the tower.
		
		2. if target link exists:
			+ check if target creature is alive and within range;
				#if not, then go to 1.
		
		3. update*/
		for(TowerFigure tower : currentDefence){
			tower.update();
		}
	}
}
