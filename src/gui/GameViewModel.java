package gui;

import javax.swing.JPanel;

public interface GameViewModel {
	void pauseGame();
	void resumeGame();
	void addCreature(FigureRepresentation figure);
	void initGame(JPanel panel);
	void startGame();
	LevelInfo getLevelInfo();
}
