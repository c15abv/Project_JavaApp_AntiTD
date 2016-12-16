package gui;

import java.sql.Time;

import javax.swing.SwingUtilities;

import creatures.AttackingPlayer;
import start.Game;
import start.Game.GameResult;
import start.Game.GameState;
import utilities.Lock;

/**
 * Class that listens to changes in the game and passes information to gui.
 * 
 * @author Karolina Jonzén and Alexander Ekström
 * @version 1.0
 */
public class GameListener implements Runnable {
	private AttackingPlayer player;
	private boolean isRunning = true;
	private Game game;
	private View view;
	private int currentPoints;
	private int previousPoints;
	private int currentCredits;
	private int previousCredits;
	private Lock lock = new Lock();

	/**
	 * Constructor that initiates the object based on the given parameters.
	 * 
	 * @param game
	 * @param view
	 * @param player
	 */
	public GameListener(Game game, View view, AttackingPlayer player) {
		this.game = game;
		this.view = view;
		this.player = player;

	}

	@Override
	public void run() {

		while (isRunning) {

			try {
				lock.lock();
				currentPoints = player.getPoints();
				currentCredits = player.getCredits();

				if (currentPoints != previousPoints) {
					SwingUtilities
							.invokeLater(() -> view.setPoints(currentPoints));
				}

				if (currentCredits != previousCredits) {
					SwingUtilities
							.invokeLater(() -> view.setCredits(currentCredits));
				}

			} catch (InterruptedException e) {
			} finally {
				lock.unlock();
			}
			previousPoints = currentPoints;
			previousCredits = currentCredits;

			if (game.getGameState() == GameState.ENDED) {
				System.out.println("GAME ENDED");

				try {
					lock.lock();
					GameResult gameResult = game.getGameResult();
					int score = 1; // ska räknas ut sen
					Time time = new Time(12345); // räknas också ut sen

					SwingUtilities.invokeLater(
							() -> view.showResult(gameResult, score, time));

					isRunning = false;
				} catch (InterruptedException e) {
				} finally {
					lock.unlock();
				}

			}

		}

	}

	/**
	 * Terminates the thread.
	 */
	public synchronized void terminate() {
		isRunning = false;
	}

}
