package towers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import start.GameLevel;
import start.Player;

/**
 * DefendingPlayer.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class DefendingPlayer extends Player{
	
	private ArrayList<TowerFigure> currentDefence;
	private AITowerFigures ai;
	
	public DefendingPlayer(GameLevel level){
		super(level.getDefenderCredit(), level);
		this.ai = null;
		currentDefence = new ArrayList<TowerFigure>();
	}

	@Override
	public void update(){
		if(ai != null){
			ai.update();
		}
	}
	
	public void setTowersAI(AITowerFigures ai){
		this.ai = ai;
	}

	@Override
	public void render(Graphics2D g2d){
		for(TowerFigure tower : currentDefence){
			tower.render(g2d);
		}
	}
	
	public void addTowerFigure(TowerFigure figure){
		currentDefence.add(figure);
		figure.setIsOnCooldown(false);
	}
	
	public ArrayList<TowerFigure> getDefence(){
		return new ArrayList<TowerFigure>(currentDefence);
	}
}
