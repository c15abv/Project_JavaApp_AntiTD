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
<<<<<<< HEAD
	private View view;
	private LevelXMLReader levelXMLReader;

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
=======
	private Lock lock;
	private View view;
	private GameListener gameListener;
>>>>>>> refs/remotes/origin/master

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

<<<<<<< HEAD
		player1 = new AttackingPlayer(level.getAttackerCredit(), level);
		player2 = new DefendingPlayer(level.getDefenderCredit(), level);
=======
		game = new Game(level);
		lock = game.getLock();
		
		player1 = game.getAttacker();
		player2 = game.getDefender(); 
>>>>>>> refs/remotes/origin/master
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
			game.getLock().lock();

			Position startPosition = game.getSelectedStart();

			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());
			CreatureFigureTemplate troop = troops.get(index);
<<<<<<< HEAD

			if (start != null) {
				player1.addCreatureFigure(troop.createNewCreature(
						start.getPosition(), start.getStartingDirection()));
			} else {
				throw new NullPointerException();
			}
=======
			
			player1.addCreatureFigure(troop
					.createNewCreature(start.getPosition(), start.getStartingDirection()));
>>>>>>> refs/remotes/origin/master

		} catch (InterruptedException e1) {
		} finally {
			game.getLock().unlock();
		}

	}

	@Override
	public void buyCreature(int index, long time) {
		CreatureFigure figure;
		try {
			game.getLock().lock();

			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());
<<<<<<< HEAD

			CreatureFigureTemplate troop = troops.get(index);

			troop.enableTeleporter(time);

			if (start != null) {
				player1.addCreatureFigure(troop.createNewCreature(
						start.getPosition(), start.getStartingDirection()));
			} else {
				throw new NullPointerException();
			}
=======
			
			CreatureFigureTemplate troop = troops.get(index);
			troop.enableTeleporter((long)time);
			troop.setActionTimer(timer);
			figure = troop
					.createNewCreature(start.getPosition(), start.getStartingDirection()); 
			player1.addCreatureFigure(figure);
>>>>>>> refs/remotes/origin/master

		} catch (InterruptedException e1) {
		} finally {
			game.getLock().unlock();

		}

	}

	/**
	 * Reads the level map and sets the level info.
	 */
<<<<<<< HEAD
	private void readLevelMap(int levelIndex) {
=======
	private void readLevelMap() {

		LevelXMLReader levelXMLReader = new LevelXMLReader("XML/levels.xml");

>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
			game.getLock().lock();

=======
			lock.lock();
			
			gameListener = new GameListener(game, view, player1,
					aiDef, runner, thread, aiDefThread);
>>>>>>> refs/remotes/origin/master
			game.startGame();

			aiDefThread.start();
			thread2 = new Thread(gameListener);
			thread2.start();
			
		} catch (InterruptedException e) {
		} finally {
			game.getLock().unlock();

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
			game.getLock().lock();
			// hitPoints = player1.getHorde().get(index).getHitPoints();
			return hitPoints;
		} catch (InterruptedException e) {
		} finally {
			game.getLock().unlock();
		}
		return hitPoints;
	}

	@Override
	public int getCredits() {
		int currentCredits = 0;
		try {
			game.getLock().lock();
			currentCredits = player1.getCredits();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			game.getLock().unlock();

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
<<<<<<< HEAD

	@Override
	public void playNextLevel() {
		// TODO Auto-generated method stub

	}

=======
>>>>>>> refs/remotes/origin/master
}
