package gui;

import java.sql.Time;
import javax.swing.SwingUtilities;
import creatures.AttackingPlayer;
import start.Game;
import start.Game.GameResult;
import start.Game.GameState;
import start.GameRunner;
import towers.AIDefendingPlayer;
import utilities.ActionTimer;
import utilities.Lock;

/**
 * Class that listens to changes in the game and passes information to gui.
 * 
 * @author Karolina Jonzen and Alexander Ekstrom
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
	private long score;
	private long totalTime;
	private ViewModel viewModel;

	/**
	 * Constructor that initiates the object based on the given parameters.
	 * 
	 * @param game
	 * @param view
	 * @param player
	 */
	public GameListener(Game game, View view, AttackingPlayer player,
			AIDefendingPlayer aiDef, GameRunner runner, Thread runnerThread,
			Thread aiDefThread, ViewModel viewModel) {
		this.game = game;
		this.view = view;
		this.player = player;
		this.runner = runner;
		this.aiDef = aiDef;
		this.runnerThread = runnerThread;
		this.aiDefThread = aiDefThread;
		this.viewModel = viewModel;
	}

	private void joinThreads() {
		runner.terminate();
		aiDef.terminate();
		runnerThread.interrupt();
		aiDefThread.interrupt();

		try {
			aiDefThread.join();
			runnerThread.join();
		} catch (InterruptedException e) {
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
				if (game.careAboutResult()) {
					try {
						lock.lock();
						GameResult gameResult = game.getGameResult();
						
						score = calculateScore();
						
						long totalScore = viewModel.getTotalScore() + score;
						viewModel.setTotalScore(totalScore);
						
						long totalTime = viewModel.getTotalTime() + game.getTimer().timeElapsed();
						viewModel.setTotalTime(totalTime/1000);
												
						SwingUtilities.invokeLater(
								() -> view.showResult(gameResult, (int)totalScore, getTime(totalTime/1000)));

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
	
	private int calculateScore()	{
		ActionTimer gameTimer = game.getTimer();
		long timeElapsed = gameTimer.timeElapsed();
		long timeLeft = gameTimer.timeLeft(game.getGameTimeTimerId());
		long totalTime = timeElapsed + timeLeft;
				
		double bonus = ((double)timeLeft/totalTime);
		
		int score = (int)(bonus * 1000);
		
		return score;
	}
	
	private Time getTime(long time)	{
		return new Time(time);
	}
	
	public long getLevelScore()	{
		return score;
	}
	
	public long getLevelTime()	{
		return totalTime;
	}

}
