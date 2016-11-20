package start;

public class GameRunner implements Runnable{
	
	public static final int UPS = 30;
	public static final long UPS_DELTA = (long) (10e9 / UPS);
	
	private volatile boolean isRunning;
	private Game game;
	
	public GameRunner(Game game){
		this.game = game;
	}
	
	@Override
	public void run(){
		isRunning = true;
		
		while(isRunning){
			game.update();
			game.render();
		}
	}
	
	public synchronized void terminate(){
		isRunning = false;
	}

}
