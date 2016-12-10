package start;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JFrame;

import creatures.CircleCreatureFigure;
import creatures.SquareCreatureFigure;
import creatures.TriangleCreatureFigure;
import tiles.GoalTile;
import tiles.PathTile;
import tiles.PathTile.Direction;
import tiles.PathTile.ValidPath;
import tiles.StartTile;
import tiles.Tile;
import tiles.VoidTile;
import creatures.CreatureFigure.Orientation;
import towers.AITowerFigures;
import towers.CircleTowerFigure;
import towers.DefendingPlayer;
import towers.SquareTowerFigure;
import utilities.ActionTimer;

public class Test3 {

	public static void main(String[] args){
		ActionTimer timer;
		JFrame frame = new JFrame();
		Game game;
		GameRunner runner;
		Thread thread;
		AttackingPlayer player1;
		DefendingPlayer player2;
		AITowerFigures ai;
		GameLevel level = new GameLevel();
		HashMap<AreaPosition, Tile> levelMap = new HashMap<AreaPosition, Tile>();
		StartTile start = new StartTile(new Position(Tile.size, Tile.size), ValidPath.EAST);
		
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
		
		
		levelMap.put(new AreaPosition(5 * Tile.size, 3 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(5 * Tile.size, 3 * Tile.size), ValidPath.WEST));
		
		
		levelMap.put(new AreaPosition(4 * Tile.size, 4 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(4 * Tile.size, 4 * Tile.size), ValidPath.NORTH));
		
		levelMap.put(new AreaPosition(4 * Tile.size, 2 * Tile.size, Tile.size, Tile.size), 
				new GoalTile(new Position(4 * Tile.size, 2 * Tile.size), ValidPath.SOUTH));
		
		//new GoalTile(new Position(2 * Tile.size, 3 * Tile.size), ValidPath.NORTH)
		
		
		level.setLevelMap(levelMap);
		
		player1 = new AttackingPlayer(100, level);
		player2 = new DefendingPlayer(100, level);
		ai = new AITowerFigures(player1, player2);
		game = new Game(level, player1, player2);
		runner = new GameRunner(game);
		timer = game.getTimer();
		
		CircleTowerFigure tFig = new CircleTowerFigure(1,180,300,
				new Position(Tile.size, 2 * Tile.size));
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(0, tFig, 200);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		player2.addTowerFigure(tFig);
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100, 0.5f,
				new Position(start.getPosition().getX(), start.getPosition().getY(), Tile.size), Orientation.FORWARD, null);
		
		fig.setNavigation(Direction.EAST);
		player1.addCreatureFigure(fig);
		
		frame.setSize(new Dimension(800, 600));
	    frame.setMinimumSize(new Dimension(800, 600));
	    frame.setMaximumSize(new Dimension(800, 600));
	    frame.setResizable(false);
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
		frame.add(game);
		frame.pack();
		
		frame.setVisible(true);
		
		thread = new Thread(runner);
		thread.start();
		
		game.startGame();
	}
}
