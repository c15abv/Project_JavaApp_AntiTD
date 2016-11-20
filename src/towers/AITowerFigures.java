package towers;

import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure;

public class AITowerFigures{

	private ArrayList<CreatureFigure> currentHorde;
	private HashMap<TowerFigure, CreatureFigure> targetMap;
	
	public AITowerFigures(ArrayList<CreatureFigure> currentHorde){
		this.currentHorde = currentHorde;
	}
	
	public void update(ArrayList<TowerFigure> currentDefence){
		/*run through currentDefence/towers;
		1. if off cooldown and no target link exists:
			+ choose an adequate target in currentHorde based on (priority):
				- range #if nothing within range: skip
				- hue
				- speed/hitpoints
			+ make a link between tower and creature by
			  inserting into the targetMap.
			+ create a projectile based on the tower.
		
		2. if off cooldown if target link exists:
			+ check if target creature is alive and within range;
				#if not, then go to 1.
				#if exist, create a projectile based on the tower.
		
		3. if not off cooldown:
			+ skip.*/
	}
}
