package gui;

/**
 * Class that holds level information.
 *
 * @author Karolina Jonzen
 * @version 1.0
 */
public class LevelInfo {
	private int nrOfTroops;
	private int creatureCost;
	private int teleporterCost;
	private int startCredit;
	private String levelName;

	/**
	 * Constructor that initiates the object with the given parameters.
	 *
	 * @param nrOfTroops
	 *            The number of troops to create at the current level.
	 * @param creatureCost
	 *            The base cost for creatures at the current level.
	 * @param teleporterCost
	 *            The teleporter cost for creatures at the current level.
	 * @param startCredit
	 *            The attacking player's start credit at the current level
	 * @param levelName
	 *            The current level's name.
	 */
	public LevelInfo(int nrOfTroops, int creatureCost, int teleporterCost,
			int startCredit, String levelName) {
		this.nrOfTroops = nrOfTroops;
		this.creatureCost = creatureCost;
		this.teleporterCost = teleporterCost;
		this.startCredit = startCredit;
		this.levelName = levelName;

	}

	public int getNrOfTroops() {
		return nrOfTroops;
	}

	public void setNrOfTroops(int nrOfTroops) {
		this.nrOfTroops = nrOfTroops;
	}

	public int getCreatureCost() {
		return creatureCost;
	}

	public void setCreatureCost(int creatureCost) {
		this.creatureCost = creatureCost;
	}

	public int getTeleporterCost() {
		return teleporterCost;
	}

	public void setTeleporterCost(int teleporterCost) {
		this.teleporterCost = teleporterCost;
	}

	public int getStartCredit() {
		return startCredit;
	}

	public void setStartCredit(int startCredit) {
		this.startCredit = startCredit;
	}

	public String getLevelName() {
		return levelName;
	}

}
