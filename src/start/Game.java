package start;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

/**
 * The Game class 
 * 
 * @author Alexander Beliaev
 *
 */
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
	
	private volatile boolean careAboutResult = true;
	
	/**
	 * Creates a new game with the specified level.
	 * Default width and height are used. 
	 * @param level
	 */
	public Game(GameLevel level){
		this(level, SIZE_X, SIZE_Y);
	}
	
	/**
	 * Creates a new game with the specified level,
	 * with the specified width and height. 
	 * 
	 * @param level
	 * @param canvasWidth
	 * @param canvasHeight
	 */
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
		bufferedImage = configuration.createCompatibleImage(level.getWidth(),
				level.getHeight());
		
		g = null;
		g2d = null;
		
		mouseCoordinateX = mouseCoordinateY = cameraOffsetX = 
				cameraOffsetY = 0;
		currentSelectedStartPosition = null;
		
		addMouseMotionListener(this);
	    addMouseListener(this);
	    
	}
	
	/**
	 * Changes the size of the game canvas.
	 * 
	 * @param width new width.
	 * @param height new height.
	 */
	public void changeSize(int width, int height){
		setSize(width, height);
		canvasWidth = width;
		canvasHeight = height;
	}
	
	/**
	 * Updates the logic of any object in the game,
	 * as well as updating the game camera/view.
	 */
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
	
	/**
	 * Renders any objects in the game with
	 * a double buffering strategy.
	 */
	public void render(){
		if(gameState != GameState.ENDED){
			buffer = this.getBufferStrategy();
			if(buffer == null){
				this.createBufferStrategy(2);
				return;
			}
			
			g2d = bufferedImage.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0,
					level.getWidth() + Tile.size + cameraOffsetX,
					level.getHeight() + Tile.size + cameraOffsetY);
				
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
			g.drawImage(bufferedImage, -cameraOffsetX, -cameraOffsetY,
					level.getWidth(), level.getHeight(), null);
			
			
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

	/* (non-Javadoc)
	 * @see utilities.TimerListener#receiveNotification(java.lang.Long)
	 */
	@Override
	public void receiveNotification(Long id){
		timer.terminate();
		gameResult = GameResult.DEFENDER_WINNER;
		gameState = GameState.ENDED;
	}
	
	/* 
	 * The width of the level which is hidden
	 * due to the canvas being shorter in width
	 * than the level width.
	 * */
	private int widthDelta(){
		if(level.getWidth() > canvasWidth){
			return level.getWidth() - canvasWidth;
		}
		
		return 0;
	}
	
	/* 
	 * The height of the level which is hidden
	 * due to the canvas being shorter in height
	 * than the level height.
	 * */
	private int heightDelta(){
		if(level.getHeight() > canvasHeight){
			return level.getHeight() - canvasHeight;
		}
		
		return 0;
	}
	
	/*
	 * Updates the game camera/view.
	 * */
	private void updateCanvasCamera(){
		if(mouseCoordinateY < canvasHeight / 2 + canvasHeight / 6 &&
				mouseCoordinateY > canvasHeight / 2 - canvasHeight / 6){
			if(mouseCoordinateX > canvasWidth - 40 &&
					cameraOffsetX < widthDelta()){
				cameraOffsetX += 4;
			}else if(mouseCoordinateX < 40 &&
					cameraOffsetX > 0){
				cameraOffsetX -= 4;
			}
			
		}else if(mouseCoordinateX < canvasWidth / 2 + canvasWidth / 10 &&
				mouseCoordinateX > canvasWidth / 2 - canvasWidth / 10){
			if(mouseCoordinateY > canvasHeight - 40 &&
					cameraOffsetY < heightDelta()){
				cameraOffsetY += 4;
			}else if(mouseCoordinateY < 40 &&
					cameraOffsetY > 0){
				cameraOffsetY -= 4;
			}
		}
		
		if(cameraOffsetY < 0){
			cameraOffsetY = 0;
		}else if(cameraOffsetY > heightDelta()){
			cameraOffsetY = heightDelta();
		}
		
		if(cameraOffsetX < 0){
			cameraOffsetX = 0;
		}else if(cameraOffsetX > widthDelta()){
			cameraOffsetX = widthDelta();
		}
	}
	
	//Setters and getters.
	
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
		careAboutResult = false;
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
	
	public synchronized boolean careAboutResult(){
		return careAboutResult;
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

	public synchronized AttackingPlayer getAttacker(){
		return attacker;
	}
	
	public synchronized DefendingPlayer getDefender(){
		return defender;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent arg0){
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0){
		mouseCoordinateX = arg0.getX();
		mouseCoordinateY = arg0.getY();
	}

	/* 
	 * Checks if the position clicked is on a start position.
	 * 
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0){
		Position position = level.getAdjacentStartPosition(arg0.getX() +
				cameraOffsetX + Tile.size / 2,
				arg0.getY() + cameraOffsetY + Tile.size / 2);
		if(position != null){
			level.selectTile(position.getX() + Tile.size / 2,
					position.getY() + Tile.size / 2);
			if(currentSelectedStartPosition != null){
				level.deselectTile(currentSelectedStartPosition.toArea());
				currentSelectedStartPosition = null;
			}
			currentSelectedStartPosition = position;
		}else{
			if(currentSelectedStartPosition != null){
				level.deselectTile(currentSelectedStartPosition.toArea());
				currentSelectedStartPosition = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0){}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0){
		mouseCoordinateX = canvasWidth / 2;
		mouseCoordinateY = canvasHeight / 2;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0){}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0){}
}
