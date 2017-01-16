package gui;

import java.sql.Time;

import javax.swing.JPanel;

import start.Game.GameResult;

/**
 * Interface to implement for communication between gui and game logic.
 *
 * @author Karolina Jonzen
 * @version 1.0
 */
public interface View {

	/**
	 * Shows game result when game has ended based on the given parameters.
	 *
	 * @param result
	 *            A GameResult, i.e. if the user won or lost the game
	 * @param score
	 *            The player's total score
	 * @param time
	 *            The time the player finished the level
	 */
	void showResult(GameResult result, int score, Time time);

	/**
	 * Sets current credit in gui.
	 *
	 * @param credit
	 *            The new credit.
	 */
	void setCredits(int credit);

	/**
	 * Sets current points in gui.
	 *
	 * @param points
	 *            The current points.
	 */
	void setPoints(int points);

	/**
	 * Gets the level map panel.
	 *
	 * @return The JPanel that should contain the level map.
	 */
	JPanel getLevelMapPanel();

	/**
	 * Gets the side panel.
	 *
	 * @return The side panel.
	 */
	JPanel getSidePanel();

	/**
	 * Shows a message dialog to the user when something went wrong in the
	 * reading/parsing of files.
	 */
	void showDialogOnLevelError();
}
