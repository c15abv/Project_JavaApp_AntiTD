package creatures;

import java.util.Random;

import creatures.CreatureFigure.Orientation;
import start.Figures;
import start.GameLevel;
import start.Position;
import tiles.PathTile.Direction;

public class CreatureFigureTemplate{
	
	private Figures creatureType;
	private int hue;
	private float scale;
	private int cost;
	private Orientation orientation;
	private GameLevel level;
	private long time;
	private double speed;
	
	public CreatureFigureTemplate(Figures creatureType, int hue, float scale,
			int cost, Orientation orientation, GameLevel level){
		this.creatureType = creatureType;
		this.hue = hue;
		this.scale = scale;
		this.cost = cost;
		this.orientation = orientation;
		this.level = level;
		
		time = -1;
		speed = CreatureFigure.BASE_SPEED;
	}
	
	public int getCost(){
		return cost;
	}
	
	public Figures getCreatureType() {
		return creatureType;
	}

	public int getHue() {
		return hue;
	}

	public float getScale() {
		return scale;
	}

	public Orientation getOrientation() {
		return orientation;
	}
	
	public void enableTeleporter(long time){
		this.time = time;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}

	public CreatureFigure createNewCreature(Position position,
			Direction direction){
		CreatureFigure creature = creatureType == Figures.CIRCLE ? 
				createNewCircleCreature(position, speed) : 
			(creatureType == Figures.SQUARE ? 
					createNewSquareCreature(position, speed) :
				(creatureType == Figures.TRIANGLE ? 
						createNewTriangleCreature(position, speed) : 
							createNewRandomCreature(position, speed)));
		if(time != -1){
			creature.enableTeleport(time);
		}
		if(direction != null){
			creature.setNavigation(direction);
			creature.getMemory().rememberBackTrackDirection(position,
					Direction.getOpposite(direction));
		}
		return creature;
	}
	
	private CircleCreatureFigure createNewCircleCreature(
			Position position, double speed){
		return new CircleCreatureFigure(hue, scale, position,
				orientation, level, speed);
	}
	
	private SquareCreatureFigure createNewSquareCreature(
			Position position, double speed){
		return new SquareCreatureFigure(hue, scale, position,
				orientation, level);
	}

	private TriangleCreatureFigure createNewTriangleCreature(
			Position position, double speed){
		return new TriangleCreatureFigure(hue, scale, position,
				orientation, level);
	}

	private CreatureFigure createNewRandomCreature(
			Position position, double speed){
		int randomInt = new Random().nextInt(3);
		
		return randomInt == 0 ? createNewCircleCreature(position, speed) :
			(randomInt == 1 ? createNewSquareCreature(position, speed) :
				createNewTriangleCreature(position, speed));
	}

}
