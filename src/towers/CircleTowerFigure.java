package towers;

import java.awt.Graphics2D;
import java.util.Map;

import creatures.CreatureFigure;
import projectiles.CircleProjectileFigure;
import projectiles.ProjectileFigure;
import start.Figures;
import start.Position;

public class CircleTowerFigure extends TowerFigure{
	
	public static final Figures SHAPE = Figures.CIRCLE;
	
	public CircleTowerFigure(int baseDamage, int hue, int range,
			Position position){
		super(baseDamage, hue, range, position);
	}

	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval(this.getPosition().getX() - TowerFigure.TEMP_SIZE/2,
				this.getPosition().getY() - TowerFigure.TEMP_SIZE/2,
				TowerFigure.TEMP_SIZE,
				TowerFigure.TEMP_SIZE);
		
		for(Map.Entry<ProjectileFigure, CreatureFigure> entry : 
				projectiles.entrySet()){
			entry.getKey().render(g2d);
		}
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

	@Override
	public void attack(){
		if(this.hasTarget() && !this.isOnCooldown() &&
				this.getTarget().isAlive()){
			projectiles.put(new CircleProjectileFigure(
					this.getHue(),
					this.getBaseDamage(),
					new Position(this.getPosition())),
					this.getTarget());
		}
	}
	
	

}
