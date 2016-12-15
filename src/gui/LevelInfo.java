package gui;

import start.GameLevel;

/**
 * Class that holds level information.
 * 
 * @author karro
 *
 */
public class LevelInfo {
	private int nrOfTroops;
	private int creatureCost;
	private int teleporterCost;
	private int startCredit;
	private GameLevel level;

	/**
	 * Constructor that initiates the object with the given parameters.
	 * 
	 * @param nrOfTroops
	 * @param creatureCost
	 * @param teleporterCost
	 * @param startCredit
	 * @param level
	 */
	public LevelInfo(int nrOfTroops, int creatureCost, int teleporterCost,
			int startCredit, GameLevel level) {
		this.nrOfTroops = nrOfTroops;
		this.creatureCost = creatureCost;
		this.teleporterCost = teleporterCost;
		this.startCredit = startCredit;
		this.level = level;
	}

	public GameLevel getLevel() {
		return level;
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

}
