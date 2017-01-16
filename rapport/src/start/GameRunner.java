package start;

/**
 * GameRunner performs the update and render calls to the game class. This is
 * the game thread.<br>
 * <br>
 * The game is updated 60 times per second, with an undefined upper limit of
 * renders per second.
 * 
 * @author Alexander Believ
 * @version 1.0
 */
public class GameRunner implements Runnable {

	public static final int UPS = 60;
	public static final long MARGIN = (long) (1000000000 / UPS);
	public static final int FPS_SKIP = 10;

	private volatile boolean isRunning = true;
	private Game game;

	/**
	 * Creates a new GameRunner with the specified game.
	 * 
	 * @param game
	 *            the game.
	 */
	public GameRunner(Game game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long lastUpdateTime = System.nanoTime();
		int loops;

		while (isRunning && !Thread.interrupted()) {
			loops = 0;
			while (System.nanoTime() > lastUpdateTime && loops < FPS_SKIP) {
				game.update();
				lastUpdateTime += MARGIN;
				loops++;
			}
			game.render();
		}
	}

	/**
	 * Ends the game-loop.
	 */
	public synchronized void terminate() {
		isRunning = false;
	}
}
