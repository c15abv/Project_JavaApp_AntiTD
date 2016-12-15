package gui;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.JPanel;

import utilities.HighScoreInfo;

/**
 * Interface to implement for communication with game logic.
 * 
 * @author karro
 *
 */
public interface GameViewModel {
	void initGame(JPanel panel);

	void startGame();

	void pauseGame();

	void resumeGame();

	void addTroop(FigureRepresentation figure);

	void buyCreature(int index);

	void buyCreature(int index, long time);

	LevelInfo getLevelInfo();

	ArrayList<HighScoreInfo> getFromDataBase() throws SQLException;

	void insertIntoDataBase(String name, int score, Time time, String level) throws SQLException;
}
