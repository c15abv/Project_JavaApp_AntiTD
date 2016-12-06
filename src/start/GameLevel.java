package start;

import java.util.HashMap;

import creatures.CreatureFigure;
import tiles.Tile;

public class GameLevel{

	private HashMap<Position, Tile> levelMap;
	private int attackingPlayerScoreGoal;
	
	public GameLevel(int attackingPlayerScoreGoal){
		this.attackingPlayerScoreGoal = attackingPlayerScoreGoal;
	}
	
	public Position getNextPosition(Position position, int steps, 
			CreatureFigure.Orientation orientation){
		
		return new Position(1,0);
	}
	
	public Position getPreviousPosition(Position position, int steps,
			CreatureFigure.Orientation orientation){
		return new Position(0,0);
	}
	
	public boolean positionHasLandOnEffect(Position position){
		return true;
	}
	
	public void enableTileEffect(CreatureFigure creature){
		//if tile has effect
		//get tile with creature position from levelMap
		//if previous position was not on the same tile
		//cast effect on creature
	}
	
	public boolean isGoalTile(Position position){
		return false;
	}
	
	public boolean isStartTile(Position position){
		return false;
	}
	
	public int getAttackingPlayerScoreGoal() {
		return attackingPlayerScoreGoal;
	}
}
