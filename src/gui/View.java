package gui;

import java.sql.Time;

import javax.swing.JPanel;

import start.Game.GameResult;

/**
 * Interface to implement for communication between gui and game logic.
 * 
 * @author Karolina Jonzén
 * @version 1.0
 */
public interface View {

	/**
	 * Shows game result when game has ended based on the given parameters.
	 * 
	 * @param result
	 * @param score
	 * @param time
	 */
	void showResult(GameResult result, int score, Time time);

	/**
	 * Sets current credit in gui.
	 * 
	 * @param credit
	 */
	void setCredits(int credit);

	
	/**
	 * Sets current points in gui.
	 * 
	 * @param points
	 */
	void setPoints(int points);

	// void setSpeed(int speed);
	
	/**
	 * Gets the level map panel.
	 * 
	 * @return
	 */
	JPanel getLevelMapPanel();
}
