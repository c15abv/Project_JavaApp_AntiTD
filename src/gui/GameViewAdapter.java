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
import start.Game.GameState;
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
public class GameViewAdapter implements ViewModel {
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
	private ArrayList<CreatureFigureTemplate> troops;
	private Thread thread, thread2, aiDefThread;
	private DatabaseHandler databaseHandler = new DatabaseHandler();
	private View view;
	private LevelXMLReader levelXMLReader;
	private Lock lock;
	private GameListener gameListener;
	private int currentLevelIndex;
	private long totalScore = 0;
	private long totalTime = 0;

	/**
	 * Constructor that sets the levelXMLReader based on user input.
	 * 
	 * @param xmlLevel
	 */
	public GameViewAdapter(String xmlLevel) {
		levelXMLReader = new LevelXMLReader(xmlLevel);
	}

	/**
	 * Constructor that sets the levelXMLReader to
	 */
	public GameViewAdapter() {
		levelXMLReader = new LevelXMLReader("XML/Levels.xml");
	}

	@Override
	public void pauseGame() {
		try {
			game.getLock().lock();
			game.pauseGame();
		} catch (InterruptedException e) {
		} finally {
			game.getLock().unlock();
		}

	}

	@Override
	public void resumeGame() {
		try {
			game.getLock().lock();
			game.resumeGame();
		} catch (InterruptedException e) {
		} finally {
			game.getLock().unlock();
		}
	}

	@Override
	public void initGame(View view, int levelIndex) {
		readLevelMap(levelIndex);
		this.currentLevelIndex = levelIndex;
		troops = new ArrayList<>();

		game = new Game(level);
		lock = game.getLock();

		player1 = game.getAttacker();
		player2 = game.getDefender();

		ai = new AITowerFigures(player1, player2);

		try {
			memory = new AIMemory.AIMemoryLoader(level.getInitialLevelMapHash())
					.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		aiDef = new AIDefendingPlayer.Builder(player2, player1, level,
				game, memory)
						.enableLearnFromExperience()
						.setGameTimer(game.getTimer(),
								game.getGameTimeTimerId())
						.setGameLock(game.getLock())
						.setTowerMutationTimeChance(5)
						.setTowerMutationTimeRange(2000)
						.setBuildTowerChance(500).build();

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
		CreatureFigureTemplate creature = troops.get(index);
		addCreatureToPlayerTroop(creature);
	}

	@Override
	public void buyCreature(int index, long time) {
		CreatureFigureTemplate creature = troops.get(index);

		creature.enableTeleporter((long) time);
		creature.setActionTimer(timer);

		addCreatureToPlayerTroop(creature);

	}

	private void addCreatureToPlayerTroop(CreatureFigureTemplate creature) {
		try {
			lock.lock();

			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());

			if (start != null) {
				player1.addCreatureFigure(creature.createNewCreature(
						start.getPosition(), start.getStartingDirection()));
				player1.setCredits(player1.getCredits() - creature.getCost());
			} else {
				throw new NullPointerException();
			}

		} catch (InterruptedException e1) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Reads the level map and sets the level info.
	 */

	private void readLevelMap(int levelIndex) {

		levelXMLReader = new LevelXMLReader("XML/levels.xml");

		ArrayList<String> lvlNames = levelXMLReader.getLvlNames();

		GameLevel l = levelXMLReader.getLevelByName(lvlNames.get(levelIndex));

		level.setLevelMap(l.getLevelMap());
		level.setAttackerCredit(l.getAttackerCredit());
		level.setAttackingPlayerScoreGoal(l.getAttackingPlayerScoreGoal());
		level.setDefenderCredit(l.getDefenderCredit());
		level.setLandOnFiles(l.getLandOnFiles());
		level.setRules(l.getRules());
		level.setTimeToFinish(l.getTimeToFinish());

		levelInfo = new LevelInfo(level.getNrOfTemplates(),
				CreatureFigure.DEFAULT_CREDIT,
				CreatureFigure.DEFAULT_CREDIT * 2, level.getAttackerCredit(),
				level.getLevelName());

	}

	@Override
	public void startGame() {
		try {
			lock.lock();

			gameListener = new GameListener(game, view, player1, aiDef, runner,
					thread, aiDefThread, this);

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
			hitPoints = troops.get(index).getHitPoints();
			return hitPoints;
		} catch (InterruptedException e) {
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
		} finally {
			lock.unlock();
		}
		return currentCredits;
	}

	@Override
	public int getScoreGoal() {
		int scoreGoal = 1000;
		try {
			game.getLock().lock();

			scoreGoal = player1.getLevel().getAttackingPlayerScoreGoal();
		} catch (InterruptedException e) {
		} finally {
			game.getLock().unlock();
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
		game.changeSize(view.getLevelMapPanel().getSize().width,
				view.getLevelMapPanel().getSize().height);
	}

	@Override
	public void changeSizeOfGameCanvas(int width) {
		game.changeSize(game.getWidth() - width, game.getHeight());
	}

	@Override
	public void quitGame() {
		try {
			lock.lock();
			//storeResult();
			game.quitGame();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}

		joinThreads();
	}

/*	public void storeResult() {
		totalScore += gameListener.getLevelScore();
		totalTime += gameListener.getLevelTime();
	}*/
	
	@Override
	public void setTotalScore(long totalScore) {
		this.totalScore = totalScore;
	}
	
	@Override
	public void setTotalTime(long time) {
		this.totalTime = time;
	}
	
	@Override
	public long getTotalScore()	{
		return totalScore;
	}
	
	@Override
	public long getTotalTime()	{
		return totalTime;
	}

	@Override
	public void quitBeforeStart() {
		runner.terminate();
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void joinThreads() {
		runner.terminate();
		aiDef.terminate();
		gameListener.terminate();
		aiDefThread.interrupt();
		thread.interrupt();
		thread2.interrupt();

		try {
			aiDefThread.join();
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playNextLevel() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean gameIsRunning() {
		if (game != null) {
			return (game.getGameState() != GameState.NA);
		} else {
			return false;
		}
	}

	@Override
	public boolean gameIsInitiated() {
		if (game != null) {
			System.out.println(game.getGameState());
			return (game.getGameState() == GameState.NA);
		} else {
			return false;
		}
	}

	@Override
	public int getCurrentLevel() {

		return currentLevelIndex;
	}

	@Override
	public double getSpeed(int index) {
		double speed = 0;
		try {
			lock.lock();
			speed = troops.get(index).getSpeed();
			return speed;
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
		return speed;
	}
}
