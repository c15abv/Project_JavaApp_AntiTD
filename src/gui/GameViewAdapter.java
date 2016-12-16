package gui;

import java.awt.Color;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

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

		player1 = new AttackingPlayer(level.getAttackerCredit(), level);
		player2 = new DefendingPlayer(level.getDefenderCredit(), level);
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
			game.getLock().lock();

			Position startPosition = game.getSelectedStart();

			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());
			CreatureFigureTemplate troop = troops.get(index);

			if (start != null) {
				player1.addCreatureFigure(troop.createNewCreature(
						start.getPosition(), start.getStartingDirection()));
			} else {
				throw new NullPointerException();
			}

		} catch (InterruptedException e1) {
		} finally {
			game.getLock().unlock();
		}

	}

	@Override
	public void buyCreature(int index, long time) {

		try {
			game.getLock().lock();

			Position startPosition = game.getSelectedStart();
			StartTile start = (StartTile) level.getLevelMap()
					.get(startPosition.toArea());

			CreatureFigureTemplate troop = troops.get(index);

			troop.enableTeleporter(time);

			if (start != null) {
				player1.addCreatureFigure(troop.createNewCreature(
						start.getPosition(), start.getStartingDirection()));
			} else {
				throw new NullPointerException();
			}

		} catch (InterruptedException e1) {
		} finally {
			game.getLock().unlock();

		}

	}

	/**
	 * Reads the level map and sets the level info.
	 */
	private void readLevelMap(int levelIndex) {
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
			game.getLock().lock();

			game.startGame();

			GameListener gameListener = new GameListener(game, view, player1);

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
		view.getLevelMapPanel().setBackground(Color.RED);
		view.getLevelMapPanel().add(game);
		view.getLevelMapPanel().revalidate();
		view.getLevelMapPanel().repaint();
	}

	@Override
	public void playNextLevel() {
		// TODO Auto-generated method stub

	}

}
