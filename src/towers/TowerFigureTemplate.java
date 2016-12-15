package towers;

import start.Figures;
import start.Position;

/**
 * TowerFigureTemplate.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class TowerFigureTemplate{
	
	private Figures towerType;
	private int hue, range, baseDamage, cooldown, cost;
	
	public TowerFigureTemplate(Figures towerType, int hue, int range,
			int cooldown, int baseDamage, int cost){
		this.towerType = towerType;
		this.hue = hue;
		this.range = range;
		this.baseDamage = baseDamage;
		this.cooldown = cooldown;
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}
	
	public TowerFigure createNewTower(Position position){
		return towerType == Figures.CIRCLE ? 
				createNewCircleTower(position) : 
			(towerType == Figures.SQUARE ? 
					createNewSquareTower(position) :
				(towerType == Figures.TRIANGLE ? 
						createNewTriangleTower(position) : 
							createNewStarTower(position)));
	}
	
	private CircleTowerFigure createNewCircleTower(
			Position position){
		return new CircleTowerFigure(baseDamage, hue, range, cooldown,
				position);
	}
	
	private SquareTowerFigure createNewSquareTower(
			Position position){
		return new SquareTowerFigure(baseDamage, hue, range, cooldown,
				position);
	}

	private TriangleTowerFigure createNewTriangleTower(
			Position position){
		return new TriangleTowerFigure(baseDamage, hue, range, cooldown,
				position);
	}

	private StarTowerFigure createNewStarTower(
			Position position){
		return new StarTowerFigure(baseDamage, hue, range, cooldown,
				position);
	}
}
