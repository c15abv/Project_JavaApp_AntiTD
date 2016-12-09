package start;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import creatures.CreatureFigure;
import tiles.Tile;

public class GameLevel{
    
    //the actual level
	private HashMap<Position, Tile> levelMap;
	
	//name of a level
	private String levelName;
	
	//Rules associated with the level
	private int attackingPlayerScoreGoal;
	private int attackerCredit;
	private int defenderCredit;
	private int timeToFinish;
	private int nrOfTemplates;
	
	/*public GameLevel(int attackingPlayerScoreGoal, ArrayList<String> rules, ArrayList<String> landOnFiles, HashMap<Position, Tile> levelMap){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
		this.rules = rules;
		this.landOnFiles = landOnFiles;
		this.levelMap = levelMap;
	}*/
	
	/**
	 * Constructs a gameLevel. The rules and name are preset to a standard
	 *  but can be changed through setters. The levelMap field needs to be
	 *  set through a setter method to be used, otherwise it will be null.  
	 */
	public GameLevel(){
	    levelName  = "unknown name";
		levelMap = new HashMap<Position, Tile>();
		
		attackingPlayerScoreGoal = 0;
		attackerCredit = 200;
		defenderCredit = 200;
		timeToFinish = 120;
		nrOfTemplates = 3;
	}
	
	//setters and getters for the gameLevel fields
	public String getLevelName() {
        return levelName;
    }
	
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    
    public int getNrOfTemplates() {
        return nrOfTemplates;
    }
    
    public void setNrOfTemplates(int nrOfTemplates) {
        this.nrOfTemplates = nrOfTemplates;
    }
	
	public int getAttackerCredit() {
		return attackerCredit;
	}
	
	public void setAttackerCredit(int attackerCredit) {
		this.attackerCredit = attackerCredit;
	}
	
	public int getDefenderCredit() {
		return defenderCredit;
	}
	
	public void setDefenderCredit(int defenderCredit) {
		this.defenderCredit = defenderCredit;
	}
	
	public int getTimeToFinish() {
		return timeToFinish;
	}
	
	public void setTimeToFinish(int timeToFinish) {
		this.timeToFinish = timeToFinish;
	}
	
	public HashMap<Position, Tile> getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(HashMap<Position, Tile> levelMap) {
		this.levelMap = levelMap;
	}
	
	public int getAttackingPlayerScoreGoal() {
        return attackingPlayerScoreGoal;
    }

	public void setAttackingPlayerScoreGoal(int attackingPlayerScoreGoal) {
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
	}
	
	public void render(Graphics2D g2d){
        for (Map.Entry<Position, Tile> entry : levelMap.entrySet()) {
            Position key = entry.getKey();
            Tile value = entry.getValue();
            value.render(g2d);          
        }
    }

	public Position getNextPosition(Position position, int steps, 
			CreatureFigure.Orientation orientation){
		
		return new Position(1,0);
	}
	
	public Position getPreviousPosition(Position position, int steps,
			CreatureFigure.Orientation orientation){
		return new Position(0,0);
	}
	
	public boolean positionHasLandOnEffect(Position position){
		return true;
	}
	
	public void enableTileEffect(CreatureFigure creature){
		//if tile has effect
		//get tile with creature position from levelMap
		//if previous position was not on the same tile
		//cast effect on creature
	}
	
	public boolean isGoalTile(Position position){
	    //Skicka in Poistionen i level map och kontrollera ifall det är en goal tile
		return false;
	}
	
	public boolean isStartTile(Position position){
	  //Skicka in Poistionen i level map och kontrollera ifall det är en level tile
		return false;
	}
	
	
}
