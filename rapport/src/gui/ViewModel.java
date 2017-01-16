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
	 *            A View
	 * @param levelIndex
	 *            The level index to initiate the game with.
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
	 *            The FigureRepresentation to add.
	 */
	void addTroop(FigureRepresentation figure);

	/**
	 * Adds a new creature to the attacking players horde based on the index
	 * given as parameter.
	 *
	 * @param index
	 *            The index at which the chosen creature was placed in the
	 *            creature troop.
	 */
	void buyCreature(int index) throws NullPointerException;

	/**
	 * Adds a new teleporter creature to the attacking players horde based on
	 * the index and teleporter drop time given as parameters.
	 *
	 * @param index
	 *            The index at which the creature was added to the troop.
	 * @param time
	 *            The drop time of the teleporter (chosen by user).
	 */
	void buyCreature(int index, long time) throws NullPointerException;

	int getScoreGoal();

	LevelInfo getLevelInfo();

	/**
	 * Gets an ArrayList of the 10 highest scores with the corresponding name,
	 * time and level from data base.
	 *
	 * @return An ArrayList of HighScoreInfo objects
	 * @throws SQLException
	 *             SQLException
	 */
	ArrayList<HighScoreInfo> getFromDataBase() throws SQLException;

	/**
	 * Inserts the given parameters into the database.
	 *
	 * @param name
	 *            The player's name (from user input)
	 * @param score
	 *            The player's resulting score
	 * @param time
	 *            The time the player finished the game.
	 * @param level
	 *            The level to which the user reached.
	 * @throws SQLException
	 *             SQLException
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
	 *            The with to set the game canvas to.
	 */
	void changeSizeOfGameCanvas(int width);

	/**
	 * Checks if game is running.
	 *
	 * @return True if game is running, else false.
	 */
	boolean gameIsRunning();

	/**
	 * Checks if game is initiated.
	 *
	 * @return True if game is initiated, else false.
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

	boolean isLastLevel();

}
