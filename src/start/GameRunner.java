package start;

public class GameRunner implements Runnable{
	
	public static final int UPS = 60;
	public static final long MARGIN = (long) (1000000000 / UPS);
	public static final int FPS_SKIP = 10;
	
	private volatile boolean isRunning = true;
	private Game game;
	
	public GameRunner(Game game){
		this.game = game;
	}
	
	@Override
	public void run(){
		long lastUpdateTime = System.nanoTime();
		int loops;
		
		while(isRunning){
			loops = 0;
			while(System.nanoTime() > lastUpdateTime && loops < FPS_SKIP){
				game.update();
				lastUpdateTime += MARGIN;
				loops++;
			}
			game.render();
		}
	}
	
	public synchronized void terminate(){
		isRunning = false;
	}

}
