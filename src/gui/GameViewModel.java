package gui;

import javax.swing.JPanel;

public interface GameViewModel {
	void pauseGame();
	void resumeGame();
	void buyCreature(int index);
	void addTroop(FigureRepresentation figure);
	void initGame(JPanel panel);
	void startGame();
	LevelInfo getLevelInfo();
}
