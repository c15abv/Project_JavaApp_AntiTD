package start;

import java.util.ArrayList;
import java.util.HashMap;

import creatures.CreatureFigure;
import tiles.Tile;

public class GameLevel{

	private HashMap<Position, Tile> levelMap;
<<<<<<< HEAD
	private ArrayList<String> rules;
	private ArrayList<String> landOnFiles;
	
	private int attackingPlayerScoreGoal;
	private int attackerCredit;
	private int defenderCredit;
	private int timeToFinish;
	
	public GameLevel(int attackingPlayerScoreGoal, ArrayList<String> rules, ArrayList<String> landOnFiles, HashMap<Position, Tile> levelMap){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
		this.rules = rules;
		this.landOnFiles = landOnFiles;
		this.levelMap = levelMap;
	}
	public GameLevel(){
		landOnFiles = new ArrayList<>();
		levelMap = new HashMap<>();
		attackingPlayerScoreGoal = 0;
		attackerCredit = 100;
		defenderCredit = 100;
		timeToFinish = 120;
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
	
	public ArrayList<String> getRules() {
		return rules;
	}

	public void setRules(ArrayList<String> rules) {
		this.rules = rules;
	}

	public ArrayList<String> getLandOnFiles() {
		return landOnFiles;
	}

	public void setLandOnFiles(ArrayList<String> landOnFiles) {
		this.landOnFiles = landOnFiles;
	}

	public void setAttackingPlayerScoreGoal(int attackingPlayerScoreGoal) {
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
	}

=======
	private int attackingPlayerScoreGoal;
	
	public GameLevel(int attackingPlayerScoreGoal){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
	}
	
>>>>>>> refs/remotes/origin/master
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
		return false;
	}
	
	public boolean isStartTile(Position position){
		return false;
	}
	
	public int getAttackingPlayerScoreGoal() {
		return attackingPlayerScoreGoal;
	}
<<<<<<< HEAD

	
=======
>>>>>>> refs/remotes/origin/master
}
