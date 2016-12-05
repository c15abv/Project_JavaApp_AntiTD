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

import towers.AITowerFigures;
import towers.DefendingPlayer;
import utilities.ActionTimer;
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
	
	private AttackingPlayer attacker;
	private DefendingPlayer defender;
	private AITowerFigures aiTower;
	
	private BufferStrategy buffer;
	private GraphicsEnvironment graphicsE;
	private GraphicsDevice device;
	private GraphicsConfiguration configuration;
	private BufferedImage bufferedImage;
	private Graphics g;
	private Graphics2D g2d;
	
	public Game(GameLevel level, AttackingPlayer attacker,
			DefendingPlayer defender){
		this.level = level;
		this.attacker = attacker;
		this.defender = defender;
		
		aiTower = new AITowerFigures(this.attacker, this.defender);
		this.defender.setTowersAI(aiTower);
		
		timer = new ActionTimer();
		timerThread = new Thread(timer);
		
		this.setIgnoreRepaint(true);
		this.setSize(SIZE_X, SIZE_Y);
		
		this.createBufferStrategy(2);
		buffer = this.getBufferStrategy();
		
		graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphicsE.getDefaultScreenDevice();
		configuration = device.getDefaultConfiguration();
		bufferedImage = configuration.createCompatibleImage(600, 600);
		
		g = null;
		g2d = null;
	}
	
	public void update(){
		if(gameState == GameState.RUNNING){
			defender.update();
			attacker.update();
			if(attacker.getPoints() >= level.getAttackingPlayerScoreGoal()){
				gameResult = GameResult.ATTACKER_WINNER;
				endGame();
			}
		}
	}
	
	public void render(){
		g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, SIZE_X, SIZE_Y);
		
		//render stuff
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
	}

	@Override
	public void receiveNotification(Integer id){
		timer.terminate();
		gameResult = GameResult.DEFENDER_WINNER;
		gameState = GameState.ENDED;
	}
	
	public void startGame(){
		timerThread.start();
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
	
	
}
