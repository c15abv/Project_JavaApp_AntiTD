package start;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tiles.StartTile;
import tiles.Tile;
import utilities.IdCounter;

/**
 * Class that represents a game level and holds information about maps and
 * rules.
 * 
 * @author Alexander Beliaev, Jan Nylen and Alexander Ekstrom
 *
 */
public class GameLevel {

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

	/**
	 * Constructor.
	 */
	public GameLevel() {
		this(DEFAULT_PLAYER_SCORE_GOAL, null, null, null);
	}

	/**
	 * Constructor that initiates the GameLevel with the given parameters.
	 * 
	 * @param attackingPlayerScoreGoal
	 * @param levelMap
	 * @param levelName
	 * @param attackerCredit
	 * @param defenderCredit
	 * @param timeToFinish
	 * @param nrOfTemplates
	 * @param nrOfX
	 * @param nrOfY
	 */
	public GameLevel(int attackingPlayerScoreGoal,
			HashMap<AreaPosition, Tile> levelMap, String levelName,
			int attackerCredit, int defenderCredit, int timeToFinish,
			int nrOfTemplates, int nrOfX, int nrOfY) {

		this.levelName = levelName;
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
		this.attackerCredit = attackerCredit;
		this.defenderCredit = defenderCredit;
		this.timeToFinish = timeToFinish;
		this.nrOfTemplates = nrOfTemplates;
		this.tilesX = nrOfX;
		this.tilesY = nrOfY;

		if ((this.levelMap = levelMap) == null) {
			this.levelMap = new HashMap<AreaPosition, Tile>();
		}

		this.levelMapHash = this.levelMap.hashCode();
		startPositions = new ArrayList<StartTile>();

		findStartTiles();

		idCounter = new IdCounter(1);
	}

	/**
	 * Constructor that initiates the GameLevel with the given paramters.
	 * 
	 * @param attackingPlayerScoreGoal
	 * @param rules
	 * @param landOnFiles
	 * @param levelMap
	 */
	public GameLevel(int attackingPlayerScoreGoal, ArrayList<String> rules,
			ArrayList<String> landOnFiles,
			HashMap<AreaPosition, Tile> levelMap) {
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;

		if ((this.rules = rules) == null) {
			this.rules = new ArrayList<String>();
		}
		if ((this.landOnFiles = landOnFiles) == null) {
			this.landOnFiles = new ArrayList<String>();
		}
		if ((this.levelMap = levelMap) == null) {
			this.levelMap = new HashMap<AreaPosition, Tile>();
			levelMapHash = 0;
		} else {
			levelMapHash = this.levelMap.hashCode();
		}

		tilesX = TILES_X;
		tilesY = TILES_Y;
		idCounter = new IdCounter(1);
	}

	public void update() {
		for (Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()) {
			entry.getValue().update();
		}
	}

	public void render(Graphics2D g2d) {
		for (Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()) {
			entry.getValue().render(g2d);
		}
	}

	public synchronized int getTilesX() {
		return tilesX;
	}

	public int getWidth() {
		return tilesX * Tile.size;
	}

	public synchronized int getTilesY() {
		return tilesY;
	}

	public int getHeight() {
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

	public int getInitialLevelMapHash() {
		return levelMapHash;
	}

	public void changeTile(AreaPosition areaPosition, Tile newTile) {
		if (levelMap.containsKey(areaPosition) && newTile != null) {
			levelMap.put(areaPosition, newTile);
		}
	}

	public long getNewUniqueId() {
		long id = idCounter.getId();
		idCounter.increment();
		return id;
	}

	public Tile getTileById(long id) {
		for (Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()) {
			if (entry.getValue().getId() == id) {
				return entry.getValue();
			}
		}

		return null;
	}

	public Position getAdjacentStartPosition(int x, int y) {
		AreaPosition clickedAreaPosition = new Position(x, y, Tile.size)
				.toArea();
		Tile tile = levelMap.get(clickedAreaPosition);

		if (tile != null && tile.isStart()) {
			return new Position(tile.getPosition().getX(),
					tile.getPosition().getY(), Tile.size);
		}

		return null;
	}

	public AreaPosition selectTile(int x, int y) {
		AreaPosition clickedAreaPosition = new Position(x, y, Tile.size)
				.toArea();
		Tile tile = levelMap.get(clickedAreaPosition);

		if (tile != null && tile.selectable()) {
			tile.setSelected(true);

			return clickedAreaPosition;
		}

		return null;
	}

	public void deselectTile(AreaPosition clickedAreaPosition) {
		Tile tile = levelMap.get(clickedAreaPosition);

		if (tile != null && tile.selectable()) {
			tile.setSelected(false);
		}
	}

	public String getLevelName() {
		return levelName;
	}

	public ArrayList<StartTile> getStartTiles() {
		return new ArrayList<StartTile>(startPositions);
	}

	private void findStartTiles() {
		for (Map.Entry<AreaPosition, Tile> entry : levelMap.entrySet()) {
			if (entry.getValue().isStart()) {
				startPositions.add((StartTile) entry.getValue());
			}
		}
	}

	public int getNrOfTemplates() {
		return nrOfTemplates;
	}

}