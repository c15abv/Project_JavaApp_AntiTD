package start;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tiles.StartTile;
import tiles.Tile;
import utilities.IdCounter;

/**
 * Skeleton by Alexander Beliaev.<br>
 * Edited by Jan Nylén, Alexander Ekstrom<br>
 * Edited by Alexander Beliaev<br>
 * <br>
 * 
 * The GameLevel contain all the contents of a loaded level.
 * 
 * @author Alexander Beliaev, Jan Nylén, Alexander Ekstrom
 *
 */
public class GameLevel{

	public static final int DEFAULT_PLAYER_SCORE_GOAL = 100;
	public static final int DEFAULT_CREDIT = 100;
	public static final int DEFAULT_TIME = 120;
	public static final int DEFAULT_TEMPLATES = 3;
	public static final String DEFAULT_MAP_NAME = "Unknown";
	
	private static final int TILES_X = 32;
	private static final int TILES_Y = 32;
	
	private HashMap<AreaPosition, Tile> levelMap;
	private ArrayList<String> rules;
	private ArrayList<String> landOnFiles;
	private ArrayList<StartTile> startPositions;
	private IdCounter idCounter;
	
	private String levelName = DEFAULT_MAP_NAME;
	private int attackingPlayerScoreGoal = DEFAULT_PLAYER_SCORE_GOAL;
	private int attackerCredit = DEFAULT_CREDIT;
	private int defenderCredit = DEFAULT_CREDIT;
	private int timeToFinish = DEFAULT_TIME; 
	private volatile int levelMapHash, tilesX, tilesY;
	private int nrOfTemplates = DEFAULT_TEMPLATES;
	
	public GameLevel(){
		this(DEFAULT_PLAYER_SCORE_GOAL, null, null, null);
	}
	
    public GameLevel(int attackingPlayerScoreGoal,
            HashMap<AreaPosition, Tile> levelMap, String levelName,
            int attackerCredit, int defenderCredit, int timeToFinish,
            int nrOfTemplates, int nrOfX, int nrOfY){
                
        this.levelName  = levelName; 
        this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;       
        this.attackerCredit = attackerCredit;
        this.defenderCredit = defenderCredit;
        this.timeToFinish = timeToFinish;
        this.nrOfTemplates = nrOfTemplates;
        this.tilesX = nrOfX;
        this.tilesY = nrOfY;
        
        if((this.levelMap = levelMap) == null){
            this.levelMap = new HashMap<AreaPosition, Tile>();
        }
        
        this.levelMapHash = this.levelMap.hashCode();
        startPositions = new ArrayList<StartTile>();
        
        findStartTiles();
        
        idCounter = new IdCounter(1);
    }
	
	public GameLevel(int attackingPlayerScoreGoal, ArrayList<String> rules,
			ArrayList<String> landOnFiles,
			HashMap<AreaPosition, Tile> levelMap){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
				
		if((this.rules = rules) == null){
			this.rules = new ArrayList<String>();
		}
		if((this.landOnFiles = landOnFiles) == null){
			this.landOnFiles = new ArrayList<String>();
		}
		if((this.levelMap = levelMap) == null){
			this.levelMap = new HashMap<AreaPosition, Tile>();
			levelMapHash = 0;
		}else{
			levelMapHash = this.levelMap.hashCode();
		}
		
		tilesX = TILES_X;
		tilesY = TILES_Y;
		idCounter = new IdCounter(1);
	}
	
	
	/**
	 * Updates any logic related to the contents of the level.
	 */
	public void update(){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			entry.getValue().update();
		}
	}
	
	/**
	 * Renders the level with the given Graphics2D object.
	 * @param g2d a Graphics2D object.
	 */
	public void render(Graphics2D g2d){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
		    entry.getValue().render(g2d);
		}
	}
	
	public synchronized int getTilesX(){
		return tilesX;
	}
	
	public int getWidth(){
		return tilesX * Tile.size;
	}
	
	public synchronized int getTilesY(){
		return tilesY;
	}
	
	public int getHeight(){
		return tilesY * Tile.size;
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
		levelMapHash = this.levelMap.hashCode();
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
	
	/**
	 * Returns the hashcode of the level when it was
	 * first loaded.
	 * 
	 * @return the hashcode of the newly created level.
	 */
	public int getInitialLevelMapHash(){
		return levelMapHash;
	}
	
	/**
	 * Changes the tile with the given AreaPosition to
	 * a new tile.
	 * 
	 * @param areaPosition an AreaPosition.
	 * @param newTile the new tile.
	 */
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
	
	/**
	 * Returns a tile by its id.
	 * 
	 * @param id and id.
	 * @return a Tile.
	 */
	public Tile getTileById(long id){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			if(entry.getValue().getId() == id){
				return entry.getValue();
			}
		}
		
		return null;
	}

	/**
	 * Returns the position of a starting position if
	 * the given coordinates are within a StartTile.
	 * 
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @return an adjacent starting position.
	 */
	public Position getAdjacentStartPosition(int x, int y){
		AreaPosition clickedAreaPosition = new Position(x, y,
				Tile.size).toArea();
		Tile tile = levelMap.get(clickedAreaPosition);
		
		if(tile != null && tile.isStart()){
			return new Position(tile.getPosition().getX(),
					tile.getPosition().getY(), Tile.size);
		}
		
		return null;
	}
	
	/**
	 * Selects a tile with the given coordinates.
	 * 
	 * @param x the x-coordinate.
	 * @param y the y-coordinate.
	 * @return the AreaPosition of the selected tile.
	 */
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
	
	/**
	 * Deselects a tile with the given AreaPosition.
	 * 
	 * @param clickedAreaPosition an AreaPosition.
	 */
	public void deselectTile(AreaPosition clickedAreaPosition){
		Tile tile = levelMap.get(clickedAreaPosition);
		
		if(tile != null && tile.selectable()){
			tile.setSelected(false);
		}
	}
	
	public String getLevelName() {
		return levelName;
	}
	
	public ArrayList<StartTile> getStartTiles(){
		return new ArrayList<StartTile>(startPositions);
	}
	
	private void findStartTiles(){
		for(Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()){
			if(entry.getValue().isStart()){
				startPositions.add((StartTile)entry.getValue());
			}
		}
	}
	
	public int getNrOfTemplates() {
		return nrOfTemplates;
	}
		
}