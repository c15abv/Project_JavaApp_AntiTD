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
public interface ViewModel {

	/**
	 * Initiates the game.
	 * 
	 * @param view
	 */
	void initGame(View view, int levelIndex);

	/**
	 * Starts the game.
	 */
	void startGame();

	/**
	 * Pauses the game.
	 */
	void pauseGame();

	/**
	 * Resumes the game.
	 */
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
	 * Inserts the given paramters into the database.
	 * 
	 * @param name
	 * @param score
	 * @param time
	 * @param level
	 * @throws SQLException
	 */
	void insertIntoDataBase(String name, int score, Time time, String level)
			throws SQLException;

	int getHitpoints(int index);

	int getCredits();

	/**
	 * Quits game.
	 */
	void quitGame();

	/**
	 * Adapts game canvas size to screen size.
	 * 
	 * @param width
	 */
	void changeSizeOfGameCanvas(int width);

	/**
	 * Checks if game is running.
	 * 
	 */
	boolean gameIsRunning();

	/**
	 * Checks if game is initiated.
	 * 
	 */
	boolean gameIsInitiated();

	/**
	 * Quit game that has not started.
	 */
	void quitBeforeStart();
	
	public int getCurrentLevel();

	public double getSpeed(int index);

	long getTotalScore();

	long getTotalTime();

	void setTotalScore(long totalScore);

	void setTotalTime(long totalTime);

}
