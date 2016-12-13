package gui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import creatures.AttackingPlayer;
import creatures.CircleCreatureFigure;
import creatures.CreatureFigure.Orientation;
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
import towers.CircleTowerFigure;
import towers.DefendingPlayer;
import towers.SquareTowerFigure;
import towers.StarTowerFigure;
import towers.TriangleTowerFigure;
import utilities.ActionTimer;
import utilities.LevelXMLReader;

public class GameViewAdapter implements GameViewModel {

	private AttackingPlayer player1;
	private DefendingPlayer player2;
	private AITowerFigures ai;
	private Game game;
	private GameRunner runner;
	private ActionTimer timer;
	private GameLevel level = new GameLevel();
	private LevelInfo levelInfo;
	
	public LevelInfo getLevelInfo() {
		return levelInfo;
	}

	private Thread thread;

	@Override
	public void pauseGame() {
		game.pauseGame();
		
	}

	@Override
	public void resumeGame() {
		game.resumeGame();
		
	}

	@Override
	public void initGame(JPanel panel) {
		readLevelMap();
		levelInfo = new LevelInfo(3, 50, 100, 500, level);

		player1 = new AttackingPlayer(100, level);
		player2 = new DefendingPlayer(100, level);
		ai = new AITowerFigures(player1, player2);
		game = new Game(level, player1, player2);
		runner = new GameRunner(game);
		timer = game.getTimer();
		
		
		panel.removeAll();
		panel.setBackground(Color.black);
		panel.add(game);
		panel.revalidate();
		panel.repaint();
		
		thread = new Thread(runner);
		thread.start();
	
	}

	@Override
	public void addCreature(FigureRepresentation figure) {
		// TODO Auto-generated method stub
		
	}
	
	private void readLevelMap()	{
		/*LevelXMLReader reader = new LevelXMLReader();
		if(reader.validateXmlAgainstXsds("XML/Levels.xml", "XML/LevelsXMLSchema.xsd")){
			
			level = reader.toLevel("XML/Levels.xml");
		}*/
		
		HashMap<AreaPosition, Tile> levelMap = new HashMap<AreaPosition, Tile>();
		TeleportTile tele1;
		TeleportTile tele2;
		
		levelMap.put(new AreaPosition(0, 0, Tile.size, Tile.size), 
				new VoidTile(new Position(0, 0)));
		levelMap.put(new AreaPosition(Tile.size, 0, Tile.size, Tile.size), 
				new VoidTile(new Position(Tile.size, 0)));
		levelMap.put(new AreaPosition(2 * Tile.size, 0, Tile.size, Tile.size), 
				new VoidTile(new Position(2 * Tile.size, 0)));
		levelMap.put(new AreaPosition(3 * Tile.size, 0, Tile.size, Tile.size), 
				new VoidTile(new Position(3 * Tile.size, 0)));
		
		levelMap.put(new AreaPosition(0, Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(0, Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, Tile.size, Tile.size, Tile.size), 
				new StartTile(new Position(Tile.size, Tile.size), ValidPath.EAST));
		levelMap.put(new AreaPosition(2 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		levelMap.put(new AreaPosition(3 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(3 * Tile.size, Tile.size)));
		
		levelMap.put(new AreaPosition(0, 2 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(0, 2 * Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(Tile.size, 2 * Tile.size)));
		levelMap.put(new AreaPosition(2 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL));
		levelMap.put(new AreaPosition(3 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(3 * Tile.size, 2 * Tile.size)));
		
		levelMap.put(new AreaPosition(0, 3 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(0, 3 * Tile.size)));
		levelMap.put(new AreaPosition(Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new VoidTile(new Position(Tile.size, 3 * Tile.size)));
		levelMap.put(new AreaPosition(2 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(2 * Tile.size, 3 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 3 * Tile.size), ValidPath.HORIZONTAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 3 * Tile.size), ValidPath.CROSSROAD));
		
		
		tele1 = new TeleportTile(new Position(5 * Tile.size, 3 * Tile.size), ValidPath.HORIZONTAL);
		levelMap.put(new AreaPosition(5 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				tele1);
		
		levelMap.put(new AreaPosition(4 * Tile.size, 4 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 4 * Tile.size), ValidPath.VERTICAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 5 * Tile.size), ValidPath.HORIZONTAL_T_NORTH));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 5 * Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
		
		levelMap.put(new AreaPosition(3 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(3 * Tile.size, 6 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_EAST));
		
		levelMap.put(new AreaPosition(5 * Tile.size, 5 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(5 * Tile.size, 5 * Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		
		levelMap.put(new AreaPosition(5 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(5 * Tile.size, 6 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 6 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 6 * Tile.size), ValidPath.HORIZONTAL_T_SOUTH));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 7 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(4 * Tile.size, 7 * Tile.size), ValidPath.NORTH));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL));
		
		levelMap.put(new AreaPosition(4 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(4 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_EAST));
		
		levelMap.put(new AreaPosition(6 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, 3 * Tile.size), ValidPath.L_TURN_HORIZONTAL_NORTH_TO_WEST));
		
		levelMap.put(new AreaPosition(6 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL_T_EAST));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(7 * Tile.size, 2 * Tile.size), ValidPath.VERTICAL_T_WEST));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 1 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(7 * Tile.size, 1 * Tile.size), ValidPath.SOUTH));
		
		levelMap.put(new AreaPosition(7 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(7 * Tile.size, 3 * Tile.size), ValidPath.NORTH));
		
		tele2 = new TeleportTile(new Position(5 * Tile.size, Tile.size), ValidPath.HORIZONTAL);
		tele2.setConnection(tele1);
		tele1.setConnection(tele2);
		levelMap.put(new AreaPosition(5 * Tile.size, Tile.size, Tile.size, Tile.size), 
				tele2);
		
		levelMap.put(new AreaPosition(6 * Tile.size, Tile.size, Tile.size, Tile.size), 
				new PathTile(new Position(6 * Tile.size, Tile.size), ValidPath.L_TURN_HORIZONTAL_SOUTH_TO_WEST));
		
		level.setLevelMap(levelMap);

		
	}

	@Override
	public void startGame() {
		game.startGame();
		
		StartTile start = new StartTile(new Position(Tile.size, Tile.size), ValidPath.EAST);

		CircleTowerFigure tFig = new CircleTowerFigure(1,180,300,
				new Position(Tile.size, 2 * Tile.size));
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(timer.getNewUniqueId(), tFig, 200);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		
		SquareTowerFigure tFig2 = new SquareTowerFigure(1,46,300,
				new Position(Tile.size * 3, 2 * Tile.size));
		
		tFig2.setActionTimer(timer);
		tFig2.setTowerAction(() -> {
			tFig2.attack();
			tFig2.setIsOnCooldown(true);
			tFig2.getActionTimer().setTimer(timer.getNewUniqueId(), tFig2, 300);
		});
		tFig2.setOnNotification(id -> {
			tFig2.setIsOnCooldown(false);
		});
		
		StarTowerFigure tFig3 = new StarTowerFigure(1,120,300,
				new Position(Tile.size * 5, 2 * Tile.size));
		
		tFig3.setActionTimer(timer);
		tFig3.setTowerAction(() -> {
			tFig3.attack();
			tFig3.setIsOnCooldown(true);
			tFig3.getActionTimer().setTimer(timer.getNewUniqueId(), tFig3, 250);
		});
		tFig3.setOnNotification(id -> {
			tFig3.setIsOnCooldown(false);
		});
		
		TriangleTowerFigure tFig4 = new TriangleTowerFigure(1,1,300,
				new Position(Tile.size * 5, 4 * Tile.size));
		
		tFig4.setActionTimer(timer);
		tFig4.setTowerAction(() -> {
			tFig4.attack();
			tFig4.setIsOnCooldown(true);
			tFig4.getActionTimer().setTimer(timer.getNewUniqueId(), tFig4, 500);
		});
		tFig4.setOnNotification(id -> {
			tFig4.setIsOnCooldown(false);
		});
		
		TriangleTowerFigure tFig5 = new TriangleTowerFigure(1,300,300,
				new Position(Tile.size * 3, 4 * Tile.size));
		
		tFig5.setActionTimer(timer);
		tFig5.setTowerAction(() -> {
			tFig5.attack();
			tFig5.setIsOnCooldown(true);
			tFig5.getActionTimer().setTimer(timer.getNewUniqueId(), tFig5, 400);
		});
		tFig5.setOnNotification(id -> {
			tFig5.setIsOnCooldown(false);
		});
		
		player2.addTowerFigure(tFig);
		player2.addTowerFigure(tFig2);
		player2.addTowerFigure(tFig3);
		player2.addTowerFigure(tFig4);
		player2.addTowerFigure(tFig5);
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100, 0.5f,
				new Position(start.getPosition().getX(), start.getPosition().getY(), Tile.size), Orientation.FORWARD, null);
		
		fig.setNavigation(Direction.EAST);
		fig.getMemory().rememberBackTrackDirection(fig.getPosition(), Direction.WEST);
		player1.addCreatureFigure(fig);
		
		
	}



}