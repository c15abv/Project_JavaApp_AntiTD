package start;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import creatures.AttackingPlayer;
import tiles.Tile;
import towers.AITowerFigures;
import towers.DefendingPlayer;
import utilities.ActionTimer;
import utilities.Lock;
import utilities.TimerListener;

public class Game extends Canvas implements TimerListener, MouseListener,
		MouseMotionListener{
	
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
	private int mouseCoordinateX, mouseCoordinateY,
		cameraOffsetX, cameraOffsetY, canvasWidth, canvasHeight;
	private volatile Position currentSelectedStartPosition;
	
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
	
	public Game(GameLevel level){
		this(level, SIZE_X, SIZE_Y);
	}
	
	public Game(GameLevel level, int canvasWidth, int canvasHeight){
		this.level = level;
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		
		attacker = new AttackingPlayer(this.level);
		defender = new DefendingPlayer(this.level);
		lock = new Lock();
		aiTower = new AITowerFigures(attacker, defender);
		defender.setTowersAI(aiTower);
		timer = new ActionTimer();
		timerThread = new Thread(timer);
		gameTimeTimerId = timer.getNewUniqueId();
		
		this.setIgnoreRepaint(true);
		this.setSize(canvasWidth, canvasHeight);
		
		graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphicsE.getDefaultScreenDevice();
		configuration = device.getDefaultConfiguration();
		bufferedImage = configuration.createCompatibleImage(canvasWidth,
				canvasHeight);
		
		g = null;
		g2d = null;
		
		mouseCoordinateX = mouseCoordinateY = cameraOffsetX = 
				cameraOffsetY = 0;
		currentSelectedStartPosition = null;
		
		addMouseMotionListener(this);
	    addMouseListener(this);
	}
	
	public void update(){
		if(gameState != GameState.ENDED){
			try{
				updateCanvasCamera();
				lock.lock();
				level.update();
				if(gameState == GameState.RUNNING){
					defender.update();
					attacker.update();
					if(attacker.getPoints() >= level.getAttackingPlayerScoreGoal()){
						gameResult = GameResult.ATTACKER_WINNER;
						endGame();
					}else if(attacker.getCredits() < attacker.getLowestCost() &&
							attacker.noHorde()){
						gameResult = GameResult.DEFENDER_WINNER;
						endGame();
					}
				}
			}catch(InterruptedException e){
			}finally{
				lock.unlock();
			}
		}
	}
	
	public synchronized AttackingPlayer getAttacker(){
		return attacker;
	}
	
	public synchronized DefendingPlayer getDefender(){
		return defender;
	}
	
	private int widthDelta(){
		if(level.getWidth() > canvasWidth){
			return level.getWidth() - canvasWidth;
		}
		
		return 0;
	}
	
	private int heightDelta(){
		if(level.getWidth() > canvasWidth){
			return level.getHeight() - canvasHeight;
		}
		
		return 0;
	}
	
	private void updateCanvasCamera(){
		if(mouseCoordinateX > SIZE_X - 40){
			if(cameraOffsetX < widthDelta())
				cameraOffsetX += 4;
		}else if(mouseCoordinateX < 40){
			if(cameraOffsetX > 0)
				cameraOffsetX -= 4;
		}
		
		if(mouseCoordinateY > SIZE_Y - 40){
			if(cameraOffsetY < heightDelta())
				cameraOffsetY += 4;
		}else if(mouseCoordinateY < 40){
			if(cameraOffsetY > 0)
				cameraOffsetY -= 4;
		}
	}
	
	public void render(){
		if(gameState != GameState.ENDED){
			buffer = this.getBufferStrategy();
			if(buffer == null){
				this.createBufferStrategy(2);
				return;
			}
			
			g2d = bufferedImage.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(-Tile.size, -Tile.size,
					level.getWidth() + Tile.size, level.getHeight() + Tile.size);
				
			try{
				lock.lock();
				//render stuff
				level.render(g2d);
				defender.render(g2d);
				attacker.render(g2d);
			}catch(InterruptedException e){
			}finally{
				lock.unlock();
			}
				
			g = buffer.getDrawGraphics();
			g.drawImage(bufferedImage, -cameraOffsetX,
					-cameraOffsetY, null);
			
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
		timerThread.interrupt();
		gameResult = GameResult.DEFENDER_WINNER;
		gameState = GameState.ENDED;
	}
	
	public synchronized void endGame(){
		timer.terminate();
		timerThread.interrupt();
		gameState = GameState.ENDED;
	}
	
	public synchronized void newLevel(GameLevel level){
		if(gameState == GameState.ENDED){
			this.level = level;
			this.attacker = new AttackingPlayer(this.level);
			this.defender = new DefendingPlayer(this.level);
			this.aiTower = new AITowerFigures(attacker, defender);
			this.defender.setTowersAI(aiTower);
			this.timer = new ActionTimer();
			this.timerThread = new Thread(timer);
			this.gameTimeTimerId = timer.getNewUniqueId();
			this.currentSelectedStartPosition = null;
		}
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
	
	public synchronized Position getSelectedStart(){
		return currentSelectedStartPosition;
	}

	@Override
	public void mouseDragged(MouseEvent arg0){
	}

	@Override
	public void mouseMoved(MouseEvent arg0){
		mouseCoordinateX = arg0.getX();
		mouseCoordinateY = arg0.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0){
		Position position = level.getAdjacentStartPosition(arg0.getX() +
				Tile.size / 2, arg0.getY() + Tile.size / 2);
		if(position != null){
			level.selectTile(position.getX() + Tile.size / 2,
					position.getY() + Tile.size / 2);
			currentSelectedStartPosition = position;
		}else{
			if(currentSelectedStartPosition != null){
				level.deselectTile(currentSelectedStartPosition.toArea());
				currentSelectedStartPosition = null;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0){
	}

	@Override
	public void mouseExited(MouseEvent arg0){
		/*mouseCoordinateX = 100;
		mouseCoordinateY = 100;*/
	}

	@Override
	public void mousePressed(MouseEvent arg0){
	}

	@Override
	public void mouseReleased(MouseEvent arg0){
	}
}
