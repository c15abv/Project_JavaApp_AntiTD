package gui;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import utilities.HighScoreInfo;

/**
 * Interface to implement for communication between gui and game logic.
 * 
 * @author Karolina Jonzen and Alexander Ekstrom
 * @version 1.0
 */
public interface GameViewModel {

	/**
	 * Initiates the game with the level index given as parameter.
	 * 
	 * @param view
	 */
	void initGame(View view, int levelIndex);

	void startGame();

	void pauseGame();

	void resumeGame();

	/**
	 * Adds a new troop type from the figure given as parameter.
	 * 
	 * @param figure
	 */
	void addTroop(FigureRepresentation figure);

	/**
	 * Adds a new creature to the attacking players horde based on the index
	 * given as parameter.
	 * 
	 * @param index
	 */
	void buyCreature(int index) throws NullPointerException;

	/**
	 * Adds a new teleporter creature to the attacking players horde based on
	 * the index and teleporter drop time given as paramters.
	 * 
	 * @param index
	 * @param time
	 */
	void buyCreature(int index, long time) throws NullPointerException;

	/**
	 * Gets the level score goal.
	 * 
	 * @return
	 */
	int getScoreGoal();

	/**
	 * Gets information about current level.
	 * 
	 * @return
	 */
	LevelInfo getLevelInfo();

	/**
	 * Gets an ArrayList of the 10 highest scores with the corresponding name,
	 * time and level from data base.
	 * 
	 * @return
	 * @throws SQLException
	 */
	ArrayList<HighScoreInfo> getFromDataBase() throws SQLException;

	/**
	 * Inserts the given parameters into the database.
	 * 
	 * @param name
	 * @param score
	 * @param time
	 * @param level
	 * @throws SQLException
	 */
	void insertIntoDataBase(String name, int score, Time time, String level)
			throws SQLException;

	/**
	 * Gets the hit points of a creature with the index given as parameter.
	 * 
	 * @param index
	 * @return
	 */
	int getHitpoints(int index);

	/**
	 * Gets the attacking player's current credit.
	 * 
	 * @return
	 */
	int getCredits();

	/**
	 * Quits the game.
	 */
	void quitGame();

	/**
	 * Adapts the size of the game canvas to the center panel of gui.
	 * 
	 * @param width
	 */
	void changeSizeOfGameCanvas(int width);
}
