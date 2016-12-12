package start;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import creatures.CircleCreatureFigure;
import creatures.SquareCreatureFigure;
import creatures.TriangleCreatureFigure;
import creatures.CreatureFigure.Orientation;
import gui.GameView;
import gui.LevelInfo;
import towers.AITowerFigures;
import towers.CircleTowerFigure;
import towers.DefendingPlayer;
import towers.SquareTowerFigure;
import utilities.ActionTimer;
import utilities.LevelXMLReader;

public class Test4{
	
	public static void main(String[] args){
		ActionTimer timer;
		//JFrame frame = new JFrame();
		Game game;
		GameRunner runner;
		Thread thread;
		AttackingPlayer player1;
		DefendingPlayer player2;
		AITowerFigures ai;
		GameLevel level = new GameLevel();
		
		LevelXMLReader reader = new LevelXMLReader();
		if(reader.validateXmlAgainstXsds("XML/Levels.xml", "XML/LevelsXMLSchema.xsd")){
			
			level = reader.toLevel("XML/Levels.xml");
		}

		
		player1 = new AttackingPlayer(100);
		player2 = new DefendingPlayer(100);
		ai = new AITowerFigures(player1, player2);
		game = new Game(level, player1, player2);
		

		
		runner = new GameRunner(game);
		timer = game.getTimer();
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100,1,
				new Position(100,100), Orientation.RANDOM, null);
		
		TriangleCreatureFigure fig2 = new TriangleCreatureFigure(100,1,
				new Position(100,200), Orientation.RANDOM, null);
		
		SquareCreatureFigure fig3 = new SquareCreatureFigure(30,1,
				new Position(400,300), Orientation.RANDOM, null);
		
		fig.addActiveAction(() -> {
			if(fig.getPosition().getX() < 500 &&
					fig.getPosition().getY() == 100){
				fig.setPosition(new Position(fig.getPosition().getX() + 2,
						fig.getPosition().getY()));
			}else if(fig.getPosition().getX() == 500 &&
					fig.getPosition().getY() < 500){
				fig.setPosition(new Position(fig.getPosition().getX(),
						fig.getPosition().getY() + 2));
			}else if(fig.getPosition().getX() > 100 &&
					fig.getPosition().getY() == 500){
				fig.setPosition(new Position(fig.getPosition().getX() - 2,
						fig.getPosition().getY()));
			}else if(fig.getPosition().getX() == 100 &&
					fig.getPosition().getY() > 100){
				fig.setPosition(new Position(fig.getPosition().getX(),
						fig.getPosition().getY() - 2));
			}
		});
		
		CircleTowerFigure tFig = new CircleTowerFigure(30,180,300,
				new Position(200,300));
		
		SquareTowerFigure tFig2 = new SquareTowerFigure(10,0,200,
				new Position(300, 300));
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(0, tFig, 1500);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		tFig2.setActionTimer(timer);
		tFig2.setTowerAction(() -> {
			tFig2.attack();
			tFig2.setIsOnCooldown(true);
			tFig2.getActionTimer().setTimer(1, tFig2, 1000);
		});
		tFig2.setOnNotification(id -> {
			tFig2.setIsOnCooldown(false);
		});
		
		player1.addCreatureFigure(fig);
		player1.addCreatureFigure(fig2);
		player1.addCreatureFigure(fig3);
		player2.addTowerFigure(tFig);
		player2.addTowerFigure(tFig2);
		/*
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
		*/
		
		LevelInfo levelInfo = new LevelInfo(3, 50, 100, 500);
		GameView gv = new GameView(levelInfo, (Canvas)game);
		gv.show();
		
		thread = new Thread(runner);
		thread.start();
		
		game.startGame();
	}

}
