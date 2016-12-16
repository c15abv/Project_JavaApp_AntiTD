package gui;

import java.awt.Color;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import creatures.AttackingPlayer;
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
 * @author Karolina Jonzén and Alexander Ekström
 * @version 1.0
 */
public class GameViewAdapter implements GameViewModel {
	private AttackingPlayer player1;
	private DefendingPlayer player2;
	private AITowerFigures ai;
	private Game game;
	private GameRunner runner;
	private ActionTimer timer;
	private GameLevel level = new GameLevel();
	private LevelInfo levelInfo;
	private ArrayList<CreatureFigureTemplate> troops = new ArrayList<>();
	private Thread thread;
	private Thread thread2;
	private DatabaseHandler databaseHandler = new DatabaseHandler();
	private Lock lock = new Lock();
	private View view;

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

		player1 = new AttackingPlayer(100, level);
		player2 = new DefendingPlayer(100, level);
		ai = new AITowerFigures(player1, player2);
		game = new Game(level, player1, player2);
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

		try {
			lock.lock();
			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());
			
			
			
			CreatureFigureTemplate troop = troops.get(index);

			troop.enableTeleporter(time);
			

			player1.addCreatureFigure(troop
					.createNewCreature(start.getPosition(), start.getStartingDirection()));

		} catch (InterruptedException e1) {
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Reads the level map and sets the level info.
	 */
	private void readLevelMap() {

		LevelXMLReader levelXMLReader = new LevelXMLReader("XML/Levels.xml");

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
			game.startGame();

			GameListener gameListener = new GameListener(game, view, player1);

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
		view.getLevelMapPanel().setBackground(Color.RED);
		view.getLevelMapPanel().add(game);
		view.getLevelMapPanel().revalidate();
		view.getLevelMapPanel().repaint();
	}

}
