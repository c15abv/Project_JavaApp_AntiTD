package start;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tiles.Tile;
import utilities.IdCounter;

public class GameLevel{

	public static final int DEFAULT_PLAYER_SCORE_GOAL = 100;
	public static final int DEFAULT_CREDIT = 100;
	public static final int DEFAULT_TIME = 120;
	
	private HashMap<AreaPosition, Tile> levelMap;
	private ArrayList<String> rules;
	private ArrayList<String> landOnFiles;
	private IdCounter idCounter;
	
	private int attackingPlayerScoreGoal = DEFAULT_PLAYER_SCORE_GOAL;
	private int attackerCredit = DEFAULT_CREDIT;
	private int defenderCredit = DEFAULT_CREDIT;
	private int timeToFinish = DEFAULT_TIME; 
	
	public GameLevel(){
		this(DEFAULT_PLAYER_SCORE_GOAL, null, null, null);
	}
	
	public GameLevel(int attackingPlayerScoreGoal, ArrayList<String> rules,
			ArrayList<String> landOnFiles,
			HashMap<AreaPosition, Tile> levelMap){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
		
		idCounter = new IdCounter();
				
		if((this.rules = rules) == null){
			this.rules = new ArrayList<String>();
		}
		if((this.landOnFiles = landOnFiles) == null){
			this.landOnFiles = new ArrayList<String>();
		}
		if((this.levelMap = levelMap) == null){
			this.levelMap = new HashMap<AreaPosition, Tile>();
		}
	}
	
	
	public void update(){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			entry.getValue().update();
		}
	}
	
	public void render(Graphics2D g2d){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
		    entry.getValue().render(g2d);
		}
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
	
	public HashMap<AreaPosition, Tile> getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(HashMap<AreaPosition, Tile> levelMap) {
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
	
	public int getAttackingPlayerScoreGoal() {
		return attackingPlayerScoreGoal;
	}
	
	public void changeTile(AreaPosition areaPosition, Tile newTile){
		if(levelMap.containsKey(areaPosition) && newTile != null){
			levelMap.put(areaPosition, newTile);
		}
	}
	
	public long getNewUniqueId(){
		long id = idCounter.getId();
		idCounter.increment();
		return id;
	}
	
	public Tile getTileById(long id){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			if(entry.getValue().getId() == id){
				return entry.getValue();
			}
		}
		
		return null;
	}

	public Position getAdjacentStartPosition(int x, int y){
		AreaPosition clickedAreaPosition = new Position(x, y,
				Tile.size).toArea();
		Tile tile = levelMap.get(clickedAreaPosition);
		
		if(tile != null && tile.isStart()){
			return tile.getPosition();
		}
		
		return null;
	}
	
}
