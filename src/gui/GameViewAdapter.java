package gui;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.border.EmptyBorder;

import creatures.AttackingPlayer;
import creatures.CreatureFigure;
import creatures.CreatureFigureTemplate;
import start.AreaPosition;
import start.Game;
import start.GameLevel;
import start.GameRunner;
import start.Position;
import tiles.GoalTile;
import tiles.PathTile;
import tiles.StartTile;
import tiles.TeleportTile;
import tiles.Tile;
import tiles.VoidTile;
import tiles.PathTile.Direction;
import tiles.PathTile.ValidPath;
import towers.AIDefendingPlayer;
import towers.AIMemory;
import towers.AITowerFigures;
import towers.DefendingPlayer;
import utilities.ActionTimer;
import utilities.DatabaseHandler;
import utilities.HighScoreInfo;
import utilities.LevelXMLReader;
import utilities.Lock;

/**
 * Class responsible for communicating gui events to game logic.
 * 
 * @author Karolina Jonz�n and Alexander Ekstr�m
 * @version 1.0
 */
public class GameViewAdapter implements GameViewModel {
	private AttackingPlayer player1;
	private DefendingPlayer player2;
	private AIDefendingPlayer aiDef;
	private AIMemory memory;
	private AITowerFigures ai;
	private Game game;
	private GameRunner runner;
	private ActionTimer timer;
	private GameLevel level = new GameLevel();
	private LevelInfo levelInfo;
	private ArrayList<CreatureFigureTemplate> troops = new ArrayList<>();
	private Thread thread, thread2, aiDefThread;
	private DatabaseHandler databaseHandler = new DatabaseHandler();
	private Lock lock;
	private View view;
	private GameListener gameListener;

	@Override
	public void pauseGame() {
		try {
			lock.lock();
			game.pauseGame();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void resumeGame() {
		try {
			lock.lock();
			game.resumeGame();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void initGame(View view) {
		readLevelMap();
		levelInfo = new LevelInfo(3, 10, 20, 500, level);

		game = new Game(level);
		lock = game.getLock();
		
		player1 = game.getAttacker();
		player2 = game.getDefender(); 
		ai = new AITowerFigures(player1, player2);
		
		try{
			memory = new AIMemory
					.AIMemoryLoader(level.getInitialLevelMapHash()).load();
		}catch(IOException e1){
			e1.printStackTrace();
		}
		
		
		aiDef = new AIDefendingPlayer.Builder(player2,
				player1, level, game, memory)
				.enableLearnFromExperience()
				.setGameTimer(game.getTimer(), game.getGameTimeTimerId())
				.setGameLock(game.getLock())
				.setTowerMutationTimeChance(5)
				.setTowerMutationTimeRange(2000)
				.setBuildTowerChance(500)
				.build();
		
		aiDefThread = new Thread(aiDef);
		
		runner = new GameRunner(game);
		timer = game.getTimer();

		this.view = view;

		initiateMap();

		thread = new Thread(runner);
		thread.start();
	}

	@Override
	public void addTroop(FigureRepresentation figure) {
		CreatureFigureTemplate creature = new CreatureFigureTemplate(
				figure.creatureType, figure.hue, figure.scale, figure.cost,
				figure.orientation, level);
		troops.add(creature);
	}

	@Override
	public void buyCreature(int index) {

		try {
			lock.lock();
			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile)level.getLevelMap().get(startPosition.toArea());
			CreatureFigureTemplate troop = troops.get(index);
			
			player1.addCreatureFigure(troop
					.createNewCreature(start.getPosition(), start.getStartingDirection()));

		} catch (InterruptedException e1) {
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void buyCreature(int index, long time) {
		CreatureFigure figure;
		try {
			lock.lock();
			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());
			
			CreatureFigureTemplate troop = troops.get(index);
			troop.enableTeleporter((long)time);
			troop.setActionTimer(timer);
			figure = troop
					.createNewCreature(start.getPosition(), start.getStartingDirection()); 
			player1.addCreatureFigure(figure);

		} catch (InterruptedException e1) {
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Reads the level map and sets the level info.
	 */
	private void readLevelMap() {

		LevelXMLReader levelXMLReader = new LevelXMLReader("XML/levels.xml");

		ArrayList<String> lvlNames = levelXMLReader.getLvlNames();

		GameLevel gameLevel = levelXMLReader.getLevelByName(lvlNames.get(0));

		level.setLevelMap(gameLevel.getLevelMap());
	}
	/*
	 * 
	 * HashMap<AreaPosition, Tile> levelMap = new HashMap<AreaPosition, Tile>();
	 * TeleportTile tele1; TeleportTile tele2;
	 * 
	 * levelMap.put(new AreaPosition(0, 0, Tile.size, Tile.size), new
	 * VoidTile(new Position(0, 0))); levelMap.put(new AreaPosition(Tile.size,
	 * 0, Tile.size, Tile.size), new VoidTile(new Position(Tile.size, 0)));
	 * levelMap.put(new AreaPosition(2 * Tile.size, 0, Tile.size, Tile.size),
	 * new VoidTile(new Position(2 * Tile.size, 0))); levelMap.put(new
	 * AreaPosition(3 * Tile.size, 0, Tile.size, Tile.size), new VoidTile(new
	 * Position(3 * Tile.size, 0)));
	 * 
	 * levelMap.put(new AreaPosition(0, Tile.size, Tile.size, Tile.size), new
	 * VoidTile(new Position(0, Tile.size))); levelMap.put( new
	 * AreaPosition(Tile.size, Tile.size, Tile.size, Tile.size), new
	 * StartTile(new Position(Tile.size, Tile.size), ValidPath.EAST));
	 * levelMap.put( new AreaPosition(2 * Tile.size, Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(2 * Tile.size, Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST)); levelMap.put( new
	 * AreaPosition(3 * Tile.size, Tile.size, Tile.size, Tile.size), new
	 * VoidTile(new Position(3 * Tile.size, Tile.size)));
	 * 
	 * levelMap.put(new AreaPosition(0, 2 * Tile.size, Tile.size, Tile.size),
	 * new VoidTile(new Position(0, 2 * Tile.size))); levelMap.put( new
	 * AreaPosition(Tile.size, 2 * Tile.size, Tile.size, Tile.size), new
	 * VoidTile(new Position(Tile.size, 2 * Tile.size))); levelMap.put( new
	 * AreaPosition(2 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), new
	 * PathTile(new Position(2 * Tile.size, 2 * Tile.size),
	 * ValidPath.VERTICAL)); levelMap.put( new AreaPosition(3 * Tile.size, 2 *
	 * Tile.size, Tile.size, Tile.size), new VoidTile(new Position(3 *
	 * Tile.size, 2 * Tile.size)));
	 * 
	 * levelMap.put(new AreaPosition(0, 3 * Tile.size, Tile.size, Tile.size),
	 * new VoidTile(new Position(0, 3 * Tile.size))); levelMap.put( new
	 * AreaPosition(Tile.size, 3 * Tile.size, Tile.size, Tile.size), new
	 * VoidTile(new Position(Tile.size, 3 * Tile.size))); levelMap.put( new
	 * AreaPosition(2 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), new
	 * PathTile(new Position(2 * Tile.size, 3 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
	 * 
	 * levelMap.put( new AreaPosition(3 * Tile.size, 3 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(3 * Tile.size, 3 * Tile.size),
	 * ValidPath.HORIZONTAL));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 3 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, 3 * Tile.size),
	 * ValidPath.CROSSROAD));
	 * 
	 * tele1 = new TeleportTile(new Position(5 * Tile.size, 3 * Tile.size),
	 * ValidPath.HORIZONTAL); levelMap.put(new AreaPosition(5 * Tile.size, 3 *
	 * Tile.size, Tile.size, Tile.size), tele1);
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 4 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, 4 * Tile.size),
	 * ValidPath.VERTICAL));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 5 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, 5 * Tile.size),
	 * ValidPath.HORIZONTAL_T_NORTH));
	 * 
	 * levelMap.put( new AreaPosition(3 * Tile.size, 5 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(3 * Tile.size, 5 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
	 * 
	 * levelMap.put( new AreaPosition(3 * Tile.size, 6 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(3 * Tile.size, 6 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
	 * 
	 * levelMap.put( new AreaPosition(5 * Tile.size, 5 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(5 * Tile.size, 5 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
	 * 
	 * levelMap.put( new AreaPosition(5 * Tile.size, 6 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(5 * Tile.size, 6 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 6 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, 6 * Tile.size),
	 * ValidPath.HORIZONTAL_T_SOUTH));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 7 * Tile.size, Tile.size,
	 * Tile.size), new GoalTile(new Position(4 * Tile.size, 7 * Tile.size),
	 * ValidPath.NORTH));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, 2 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, 2 * Tile.size),
	 * ValidPath.VERTICAL));
	 * 
	 * levelMap.put( new AreaPosition(4 * Tile.size, Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(4 * Tile.size, Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
	 * 
	 * levelMap.put( new AreaPosition(6 * Tile.size, 3 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(6 * Tile.size, 3 * Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
	 * 
	 * levelMap.put( new AreaPosition(6 * Tile.size, 2 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(6 * Tile.size, 2 * Tile.size),
	 * ValidPath.VERTICAL_T_EAST));
	 * 
	 * levelMap.put( new AreaPosition(7 * Tile.size, 2 * Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(7 * Tile.size, 2 * Tile.size),
	 * ValidPath.VERTICAL_T_WEST));
	 * 
	 * levelMap.put( new AreaPosition(7 * Tile.size, 1 * Tile.size, Tile.size,
	 * Tile.size), new GoalTile(new Position(7 * Tile.size, 1 * Tile.size),
	 * ValidPath.SOUTH));
	 * 
	 * levelMap.put( new AreaPosition(7 * Tile.size, 3 * Tile.size, Tile.size,
	 * Tile.size), new GoalTile(new Position(7 * Tile.size, 3 * Tile.size),
	 * ValidPath.NORTH));
	 * 
	 * tele2 = new TeleportTile(new Position(5 * Tile.size, Tile.size),
	 * ValidPath.HORIZONTAL); tele2.setConnection(tele1);
	 * tele1.setConnection(tele2); levelMap.put(new AreaPosition(5 * Tile.size,
	 * Tile.size, Tile.size, Tile.size), tele2);
	 * 
	 * levelMap.put( new AreaPosition(6 * Tile.size, Tile.size, Tile.size,
	 * Tile.size), new PathTile(new Position(6 * Tile.size, Tile.size),
	 * ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
	 * 
	 * level.setLevelMap(levelMap); }
	 */

	@Override
	public void startGame() {
		try {
			lock.lock();
			
			gameListener = new GameListener(game, view, player1,
					aiDef, runner, thread, aiDefThread);
			game.startGame();

			aiDefThread.start();
			thread2 = new Thread(gameListener);
			thread2.start();
			
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public LevelInfo getLevelInfo() {
		return levelInfo;
	}

	@Override
	public ArrayList<HighScoreInfo> getFromDataBase() throws SQLException {
		return databaseHandler.getFromDatabase();
	}

	@Override
	public void insertIntoDataBase(String name, int score, Time time,
			String level) throws SQLException {
		databaseHandler.insertToDatabase(name, score, time, level);

	}

	@Override
	public int getHitpoints(int index) {
		int hitPoints = 0;
		try {
			lock.lock();
			// hitPoints = player1.getHorde().get(index).getHitPoints();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();

		}
		return hitPoints;
	}

	@Override
	public int getCredits() {
		int currentCredits = 0;
		try {
			lock.lock();
			currentCredits = player1.getCredits();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return currentCredits;

	}

	@Override
	public int getScoreGoal() {
		int scoreGoal = 1000;
		try {
			lock.lock();
			scoreGoal = player1.getLevel().getAttackingPlayerScoreGoal();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}

		return scoreGoal;
	}

	/**
	 * Initiates the map displayed in the gui.
	 */
	private void initiateMap() {
		
		view.getLevelMapPanel().removeAll();
		view.getLevelMapPanel().setBackground(Color.BLACK);
		view.getLevelMapPanel().add(game);
		view.getLevelMapPanel().revalidate();
		view.getLevelMapPanel().repaint();
		game.changeSize(view.getLevelMapPanel().getSize().width, view.getLevelMapPanel().getSize().height);
	}
	
	@Override
	public void changeSizeOfGameCanvas(int width){
		game.changeSize(game.getWidth() - width,
				game.getHeight());
	}
	
	@Override
	public void quitGame(){
		try {
			lock.lock();
			game.quitGame();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
		
		joinThreads();
	}
	
	private void joinThreads(){
		runner.terminate();
		aiDef.terminate();
		gameListener.terminate();
		aiDefThread.interrupt();
		thread.interrupt();
		thread2.interrupt();
		
		try{
			aiDefThread.join();
			thread.join();
			thread2.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
