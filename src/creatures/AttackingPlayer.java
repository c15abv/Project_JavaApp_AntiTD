package creatures;

import java.awt.Graphics2D;
import java.util.ArrayList;

import start.GameLevel;
import start.Player;

public class AttackingPlayer extends Player{

	private ArrayList<CreatureFigure> currentHorde;
	private AICreatureFigures ai;
	private int points, creaturesBought, lowestCost;
	
	public AttackingPlayer(GameLevel level){
		super(level.getAttackerCredit(), level);
		currentHorde = new ArrayList<CreatureFigure>();
		ai = new AICreatureFigures(this);
		points = creaturesBought = lowestCost = 0;
	}

	@Override
	public void update(){
		CreatureFigure figure;
		
		ai.update();
		
		for(int i=0; i<currentHorde.size(); i++){
			figure = currentHorde.get(i);
			figure.update();
			if(figure.isFinished()){
				currentHorde.remove(i);
				if(figure.hasReachedGoal()){
					points++;
					setCredits(getCredits() + figure.getCreditOnGoal());
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g2d){
		for(CreatureFigure figure : currentHorde){
			figure.render(g2d);
		}
	}
	
	public void addCreatureFigure(CreatureFigure figure){
		currentHorde.add(figure);
		creaturesBought++;
	}
	
	public ArrayList<CreatureFigure> getHorde(){
		return new ArrayList<CreatureFigure>(currentHorde);
	}
	
	public int getPoints(){
		return points;
	}
	
	public int getHordeSize(){
		return currentHorde.size();
	}
	
	public int getCreaturesBought(){
		return creaturesBought;
	}
	
	public void setLowestCost(int lowestCost){
		this.lowestCost = lowestCost;
	}
	
	public int getLowestCost(){
		return lowestCost;
	}
	
	public boolean noHorde(){
		return getHordeSize() == 0;
	}

}
