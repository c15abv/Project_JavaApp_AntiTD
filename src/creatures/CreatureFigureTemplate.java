package creatures;

import java.util.Random;

import creatures.CreatureFigure.Orientation;
import start.Figures;
import start.GameLevel;
import start.Position;
import tiles.PathTile.Direction;
import utilities.ActionTimer;

public class CreatureFigureTemplate{
	
	private Figures creatureType;
	private int hue;
	private double scale;
	private int cost, creditOnGoal, creditOnKill;
	private Orientation orientation;
	private GameLevel level;
	private long time;
	private double speed;
	private ActionTimer timer;
	
	public CreatureFigureTemplate(Figures creatureType, int hue, double scale,
			int cost, Orientation orientation, GameLevel level){
		this.creatureType = creatureType;
		this.hue = hue;
		this.scale = scale;
		this.cost = cost;
		this.orientation = orientation;
		this.level = level;
		
		timer = null;
		creditOnGoal = creditOnKill = cost / 4;
		
		chooseRandomShape();
		
		time = -1;
		speed = CreatureFigure.BASE_SPEED;
	}
	
	private void chooseRandomShape(){
		int randomInt = new Random().nextInt(3);
		
		if(creatureType != null &&
				creatureType == Figures.STAR){
			creatureType = randomInt == 0 ? Figures.CIRCLE :
				randomInt == 1 ? Figures.TRIANGLE : Figures.SQUARE;
		}
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

	public double getScale() {
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
	
	public void setCreditOnGoal(int creditOnGoal){
		this.creditOnGoal = creditOnGoal;
	}
	
	public void setCreditOnKill(int creditOnKill){
		this.creditOnKill = creditOnKill;
	}
	
	public void setActionTimer(ActionTimer timer){
		this.timer = timer;
	}
	
	public int getHitPoints(){
		return (int)(CreatureFigure.BASE_HITPOINTS * scale);
	}
	
	public double getSpeed(){
		return (double) speed / scale;
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
		if(timer != null){
			creature.setActionTimer(timer);
		}
		if(time != -1){
			creature.enableTeleport(time);
		}
		if(direction != null){
			creature.setNavigation(direction);
			creature.getMemory().rememberBackTrackDirection(position,
					Direction.getOpposite(direction));
		}
		
		creature.setCreditOnGoal(creditOnGoal);
		creature.setCreditOnKill(creditOnKill);
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
