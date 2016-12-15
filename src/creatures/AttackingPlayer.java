package creatures;

import java.awt.Graphics2D;
import java.util.ArrayList;

import start.GameLevel;
import start.Player;

public class AttackingPlayer extends Player{

	private ArrayList<CreatureFigure> currentHorde;
	private AICreatureFigures ai;
	private int points;
	
	public AttackingPlayer(int credits, GameLevel level){
		super(credits, level);
		currentHorde = new ArrayList<CreatureFigure>();
		points = 0;
		ai = new AICreatureFigures(this);
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
	}
	
	public ArrayList<CreatureFigure> getHorde(){
		return new ArrayList<CreatureFigure>(currentHorde);
	}
	
	public int getPoints(){
		return points;
	}

}
