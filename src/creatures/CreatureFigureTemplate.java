package creatures;

import java.util.Random;

import start.Figures;
import start.Position;

public class CreatureFigureTemplate{
	
	private Figures creatureType;
	private int hue;
	private float scale;
	private int cost;
	
	public CreatureFigureTemplate(Figures creatureType, int hue, float scale,
			int cost){
		this.creatureType = creatureType;
		this.hue = hue;
		this.scale = scale;
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}
	
	public CreatureFigure createNewCreature(Position position){
		return creatureType == Figures.CIRCLE ? 
				createNewCircleCreature(position) : 
			(creatureType == Figures.SQUARE ? 
					createNewSquareCreature(position) :
				(creatureType == Figures.TRIANGLE ? 
						createNewTriangleCreature(position) : 
							createNewRandomCreature(position)));
	}
	
	private CircleCreatureFigure createNewCircleCreature(
			Position position){
		return new CircleCreatureFigure(hue, scale, position);
	}
	
	private SquareCreatureFigure createNewSquareCreature(
			Position position){
		return new SquareCreatureFigure(hue, scale, position);
	}

	private TriangleCreatureFigure createNewTriangleCreature(
			Position position){
		return new TriangleCreatureFigure(hue, scale, position);
	}

	private CreatureFigure createNewRandomCreature(
			Position position){
		int randomInt = new Random().nextInt(3);
		
		return randomInt == 0 ? createNewCircleCreature(position) :
			(randomInt == 1 ? createNewSquareCreature(position) :
				createNewTriangleCreature(position));
	}

}
