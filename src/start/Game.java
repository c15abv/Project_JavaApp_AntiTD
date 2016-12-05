package start;

import towers.AITowerFigures;
import towers.DefendingPlayer;
import utilities.TimerListener;

public class Game implements TimerListener{
	
	private static final int TEMP_CREDIT_HOLDER = 0;
	
	public enum GameState{
		RUNNING, PAUSED;
	}
	
	private GameLevel level;
	private GameState gameState;
	
	private AttackingPlayer attacker;
	private DefendingPlayer defender;
	private AITowerFigures aiTower;
	
	public Game(GameLevel level){
		this.level = level;
		
		attacker = new AttackingPlayer(TEMP_CREDIT_HOLDER);
		aiTower = new AITowerFigures(attacker, defender);
		defender = new DefendingPlayer(TEMP_CREDIT_HOLDER);
	}
	
	public void update(){
		defender.update();
		attacker.update();
	}
	
	public void render(){
		
	}

	@Override
	public void receiveNotification(Integer id){
		//game time is out
	}
}
