package start;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tiles.Tile;
import utilities.IdCounter;
import utilities.Lock;

public class GameLevel{

	public static final int DEFAULT_PLAYER_SCORE_GOAL = 100;
	public static final int DEFAULT_CREDIT = 100;
	public static final int DEFAULT_TIME = 120;
	public static final int DEFAULT_TEMPLATES = 3;
	public static final String DEFAULT_MAP_NAME = "Unknown";

	private HashMap<AreaPosition, Tile> levelMap;
	private IdCounter idCounter;
	private Lock lock;
	
	private String levelName = DEFAULT_MAP_NAME;
	private int attackingPlayerScoreGoal = DEFAULT_PLAYER_SCORE_GOAL;
	private int attackerCredit = DEFAULT_CREDIT;
	private int defenderCredit = DEFAULT_CREDIT;
	private int timeToFinish = DEFAULT_TIME;
	private int nrOfTemplates = DEFAULT_TEMPLATES; 
	
	public GameLevel(){
        this(DEFAULT_PLAYER_SCORE_GOAL, null, DEFAULT_MAP_NAME,
                DEFAULT_CREDIT, DEFAULT_CREDIT, DEFAULT_TIME, DEFAULT_TEMPLATES);
    }
	
	/**
     * @param attackingPlayerScoreGoal
     * @param levelMap
     * @param levelName
     * @param attackerCredit
     * @param defenderCredit
     * @param timeToFinish
     * @param nrOfTemplates
     */
	public GameLevel(int attackingPlayerScoreGoal,
            HashMap<AreaPosition, Tile> levelMap, String levelName,
            int attackerCredit, int defenderCredit, int timeToFinish,
            int nrOfTemplates){
				
		this.levelName  = levelName; 
        this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;		
        this.attackerCredit = attackerCredit;
        this.defenderCredit = defenderCredit;
        this.timeToFinish = timeToFinish;
        this.nrOfTemplates = nrOfTemplates;

        if((this.levelMap = levelMap) == null){
            this.levelMap = new HashMap<AreaPosition, Tile>();
        }
		
		idCounter = new IdCounter();
		lock = new Lock();
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
	
	public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setNrOfTemplates(int nrOfTemplates) {
        this.nrOfTemplates = nrOfTemplates;
    }

    public int getNrOfTemplates() {
        return nrOfTemplates;
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
		return new HashMap<AreaPosition, Tile>(levelMap);
	}

	public void setLevelMap(HashMap<AreaPosition, Tile> levelMap) {
		this.levelMap = levelMap;
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
	
	public AreaPosition selectTile(int x, int y){
		AreaPosition clickedAreaPosition = new Position(x, y,
				Tile.size).toArea();
		Tile tile = levelMap.get(clickedAreaPosition);
		
		if(tile != null && tile.selectable()){
			tile.setSelected(true);
			return clickedAreaPosition;
		}
		
		return null;
	}
	
	public void deselectTile(AreaPosition clickedAreaPosition){
		Tile tile = levelMap.get(clickedAreaPosition);
		
		if(tile != null && tile.selectable()){
			tile.setSelected(false);
		}
	}
}
