package gui;

import java.sql.Time;

import javax.swing.SwingUtilities;

import org.junit.runner.Runner;

import creatures.AttackingPlayer;
import start.Game;
import start.Game.GameResult;
import start.Game.GameState;
import start.GameRunner;
import towers.AIDefendingPlayer;
import utilities.Lock;

/**
 * Class that listens to changes in the game and passes information to gui.
 * 
 * @author Karolina Jonz�n and Alexander Ekstr�m
 * @version 1.0
 */
public class GameListener implements Runnable {
	private AttackingPlayer player;
	private boolean isRunning = true;
	private AIDefendingPlayer aiDef;
	private GameRunner runner;
	private Thread runnerThread, aiDefThread;
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
	public GameListener(Game game, View view, AttackingPlayer player,
			AIDefendingPlayer aiDef, GameRunner runner, Thread runnerThread,
			Thread aiDefThread) {
		this.game = game;
		this.view = view;
		this.player = player;
		this.runner = runner;
		this.aiDef = aiDef;
		this.runnerThread = runnerThread;
		this.aiDefThread = aiDefThread;
	}
	
	private void joinThreads(){
		runner.terminate();
		aiDef.terminate();
		runnerThread.interrupt();
		aiDefThread.interrupt();
		
		try{
			aiDefThread.join();
			runnerThread.join();
		}catch(InterruptedException e){
		}
	}

	@Override
	public void run() {

		while (isRunning && !Thread.interrupted()) {

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
				joinThreads();
				
				System.out.println("Threads joined");
				if(game.careAboutResult()){
					try {
						lock.lock();
						GameResult gameResult = game.getGameResult();
						int score = 1; // ska r�knas ut sen
						Time time = new Time(12345); // r�knas ocks� ut sen
	
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

	}

	/**
	 * Terminates the thread.
	 */
	public synchronized void terminate() {
		isRunning = false;
	}

}
