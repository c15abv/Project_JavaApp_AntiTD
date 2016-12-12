package towers;

import creatures.CreatureFigureTemplate;
import start.GameLevel;

/**
 * AIDefendingPlayer.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class AIDefendingPlayer implements Runnable{
	
	private GameLevel level;
	
	public AIDefendingPlayer(GameLevel level){
		this.level = level;
	}
	
	public void chooseTowers(CreatureFigureTemplate ... templates){
		
	}

	@Override
	public void run(){
		
	}
}
