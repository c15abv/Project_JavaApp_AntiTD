package creatures;

import java.awt.Graphics2D;

import start.Figures;
import start.Position;
import towers.TowerFigure;

public class CircleCreatureFigure extends CreatureFigure{

	public static final Figures shape = Figures.CIRCLE;
	
	public CircleCreatureFigure(int hue, float scale, Position position){
		super(hue, scale, position);
	}

	@Override
	public void update() {
		if (this.hasSpawned == 0) {
			for (Action action : this.onSpawnActionList) {
				action.executeAction();
			}
			this.hasSpawned++;
		}

		for (Action action : this.onActiveActionList) {
			action.executeAction();
		}
	}

	public void moveForward() {
		if (this.isAlive()) {
			Position currentPosition = this.getPosition();

			Position newPosition = new Position(currentPosition.getX() + 1,
					currentPosition.getY() + 1);

			this.setPosition(newPosition);
		}
	}


	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval(this.getPosition().getX(),
				this.getPosition().getY(),
				CreatureFigure.TEMP_SIZE,
				CreatureFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return shape;
	}
}
