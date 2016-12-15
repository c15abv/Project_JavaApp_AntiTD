package gui;

import javax.swing.SwingUtilities;

import creatures.AttackingPlayer;
import start.Game;
import start.Game.GameState;

public class GameListener implements Runnable {

	private AttackingPlayer player;
	private boolean isRunning = true;
	private Game game;
	private GameView gameView;


	public GameListener(Game game, GameView gameView, AttackingPlayer player) {
		this.game = game;
		this.gameView = gameView;
		this.player = player;
	}

	@Override
	public void run() {

		while(isRunning){
			if(game.getGameState() == GameState.ENDED)	{
			}
		}

		
	}

	/*
	private boolean gameStateChanged() {
		return true;
		
	}*/

}
