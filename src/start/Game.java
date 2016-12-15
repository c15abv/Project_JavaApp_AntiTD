package start;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import creatures.AttackingPlayer;
import towers.AITowerFigures;
import towers.DefendingPlayer;
import utilities.ActionTimer;
import utilities.Lock;
import utilities.TimerListener;

public class Game extends Canvas implements TimerListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int SIZE_X = 800;
	public static final int SIZE_Y = 600;
	
	public enum GameState{
		RUNNING, PAUSED, ENDED, NA;
	}
	
	public enum GameResult{
		ATTACKER_WINNER, DEFENDER_WINNER, NA;
	}
	
	private GameLevel level;
	private volatile GameState gameState = GameState.NA;
	private GameResult gameResult = GameResult.NA;
	private volatile ActionTimer timer;
	private Thread timerThread;
	private volatile long gameTimeTimerId;
	
	private AttackingPlayer attacker;
	private DefendingPlayer defender;
	private AITowerFigures aiTower;
	private volatile Lock lock;
	
	private BufferStrategy buffer;
	private GraphicsEnvironment graphicsE;
	private GraphicsDevice device;
	private GraphicsConfiguration configuration;
	private BufferedImage bufferedImage;
	private Graphics g;
	private Graphics2D g2d;
	
	public Game(GameLevel level, AttackingPlayer attacker,
			DefendingPlayer defender){
		this(level, attacker, defender, new Lock());
	}
	
	public Game(GameLevel level, AttackingPlayer attacker,
			DefendingPlayer defender, Lock lock){
		this.level = level;
		this.attacker = attacker;
		this.defender = defender;
		this.lock = lock;
		
		aiTower = new AITowerFigures(this.attacker, this.defender);
		this.defender.setTowersAI(aiTower);
		
		timer = new ActionTimer();
		timerThread = new Thread(timer);
		gameTimeTimerId = timer.getNewUniqueId();
		
		this.setIgnoreRepaint(true);
		this.setSize(SIZE_X, SIZE_Y);
		
		graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphicsE.getDefaultScreenDevice();
		configuration = device.getDefaultConfiguration();
		bufferedImage = configuration.createCompatibleImage(SIZE_X, SIZE_Y);
		
		g = null;
		g2d = null;
	}
	
	public void update(){
		try{
			lock.lock();
			level.update();
			if(gameState == GameState.RUNNING){
				defender.update();
				attacker.update();
				if(attacker.getPoints() >= level.getAttackingPlayerScoreGoal()){
					gameResult = GameResult.ATTACKER_WINNER;
					endGame();
				}
			}
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}
	
	public void render(){
		try{
			lock.lock();
			buffer = this.getBufferStrategy();
			if(buffer == null){
				this.createBufferStrategy(2);
				return;
			}
			
			g2d = bufferedImage.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, SIZE_X, SIZE_Y);
			
			//render stuff
			level.render(g2d);
			defender.render(g2d);
			attacker.render(g2d);
			
			g = buffer.getDrawGraphics();
			g.drawImage(bufferedImage, 0, 0, null);
			
			if(!buffer.contentsLost()){
				buffer.show();
			}
			
			if(g != null){
				g.dispose();
			}
			
			if(g2d != null){
				g2d.dispose();
			}
		}catch(InterruptedException e){
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void receiveNotification(Long id){
		timer.terminate();
		gameResult = GameResult.DEFENDER_WINNER;
		gameState = GameState.ENDED;
	}
	
	public void startGame(){
		timerThread.start();
		timer.setTimer(gameTimeTimerId, this, 
				level.getTimeToFinish() * 1000);
		gameState = GameState.RUNNING;
	}
	
	public synchronized void pauseGame(){
		timer.pause();
		gameState = GameState.PAUSED;
	}
	
	public synchronized void resumeGame(){
		timer.resume();
		gameState = GameState.RUNNING;
	}
	
	public synchronized void quitGame(){
		timer.terminate();
		gameResult = GameResult.DEFENDER_WINNER;
		gameState = GameState.ENDED;
	}
	
	public synchronized void endGame(){
		timer.terminate();
		gameState = GameState.ENDED;
	}

	public synchronized GameState getGameState(){
		return gameState;
	}

	public synchronized GameResult getGameResult(){
		return gameResult;
	}
	
	public synchronized ActionTimer getTimer(){
		return timer;
	}
	
	public synchronized Lock getLock(){
		return lock;
	}
	
	public synchronized long getGameTimeTimerId(){
		return gameTimeTimerId;
	}
	
}
