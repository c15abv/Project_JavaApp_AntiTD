package start;

import java.util.ArrayList;

import towers.AITowerFigures;
import towers.TowerFigure;

public class DefendingPlayer extends Player{
	
	private ArrayList<TowerFigure> currentDefence;
	private AITowerFigures ai;
	
	public DefendingPlayer(int credits, AITowerFigures ai){
		super(credits);
		this.ai = ai;
		currentDefence = new ArrayList<TowerFigure>();
	}

	@Override
	public void update(){
		ai.update(currentDefence);
	}

	@Override
	public void render(){
	}
	
	public void addTowerFigure(TowerFigure figure){
		currentDefence.add(figure);
	}
}
