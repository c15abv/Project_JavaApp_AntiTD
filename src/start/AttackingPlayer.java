package start;

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
	}

	@Override
	public void render(){
	}
	
	public void addCreatureFigure(CreatureFigure figure){
		currentHorde.add(figure);
	}
	
	public int getPoints(){
		return points;
	}

}
