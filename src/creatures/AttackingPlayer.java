package creatures;

import java.awt.Graphics2D;
import java.util.ArrayList;

import start.GameLevel;
import start.Player;

/**
 * The AttackingPlayer class holds the information
 * regarding the attacking player's current horde, and
 * the credits and points which have been spent and 
 * earned during an ongoing game session.<br>
 * <br>
 * Since the AttackingPlayer class is responsible
 * for the horde of creatures, it is also responsible
 * for making the appropriate calls to update and 
 * render them.
 * 
 * @author Alexander Beliaev
 *
 */
public class AttackingPlayer extends Player{

	private ArrayList<CreatureFigure> currentHorde;
	private AICreatureFigures ai;
	private int points, creaturesBought, lowestCost;
	
	/**
	 * AttackingPlayer constructor.<br>
	 * <br>
	 * Creates an AttackingPlayer instance with the
	 * specified level.
	 * @param level the current level.
	 */
	public AttackingPlayer(GameLevel level){
		super(level.getAttackerCredit(), level);
		currentHorde = new ArrayList<CreatureFigure>();
		ai = new AICreatureFigures(this);
		points = creaturesBought = lowestCost = 0;
	}

	/* (non-Javadoc)
	 * @see start.Player#update()
	 */
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

	/* (non-Javadoc)
	 * @see start.Player#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d){
		for(CreatureFigure figure : currentHorde){
			figure.render(g2d);
		}
	}
	
	/**
	 * Adds a new creature to
	 * @param figure
	 */
	public void addCreatureFigure(CreatureFigure figure){
		currentHorde.add(figure);
		creaturesBought++;
	}
	
	//Setters and getters
	
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
