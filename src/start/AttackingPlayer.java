package start;

import java.awt.Graphics2D;
import java.util.ArrayList;

import creatures.CreatureFigure;

public class AttackingPlayer extends Player{

	private ArrayList<CreatureFigure> currentHorde;
	private int points;
	
	public AttackingPlayer(int credits){
		super(credits);
		currentHorde = new ArrayList<CreatureFigure>();
		points = 0;
	}

	@Override
	public void update(){
		CreatureFigure figure;
		for(int i=0; i<currentHorde.size(); i++){
			figure = currentHorde.get(i);
			figure.update();
			if(figure.hasReachedGoal()){
				points++;
			}
			if(figure.isFinished()){
				currentHorde.remove(i);
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
