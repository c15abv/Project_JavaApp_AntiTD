package towers;

import start.Figures;
import start.Position;

public class TowerFigureTemplate{
	
	private Figures towerType;
	private int hue;
	private int range;
	private int baseDamage;
	private int cost;
	
	public TowerFigureTemplate(Figures towerType, int hue, int range,
			int baseDamage, int cost){
		this.towerType = towerType;
		this.hue = hue;
		this.range = range;
		this.baseDamage = baseDamage;
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
		return new CircleTowerFigure(baseDamage, hue, range, position);
	}
	
	private SquareTowerFigure createNewSquareTower(
			Position position){
		return new SquareTowerFigure(baseDamage, hue, range, position);
	}

	private TriangleTowerFigure createNewTriangleTower(
			Position position){
		return new TriangleTowerFigure(baseDamage, hue, range, position);
	}

	private StarTowerFigure createNewStarTower(
			Position position){
		return new StarTowerFigure(baseDamage, hue, range, position);
	}
}
