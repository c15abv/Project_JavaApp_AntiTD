package towers;

import java.awt.Graphics2D;

import projectiles.StarProjectileFigure;
import start.Figures;
import start.Position;
import utilities.CustomShapes;

/**
 * StarTowerFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class StarTowerFigure extends TowerFigure{

	public static final Figures SHAPE = Figures.STAR;
	
	public StarTowerFigure(int baseDamage, int hue, int range, 
			Position position){
		super(baseDamage, hue, range, COOLDOWN, position);
	}

	public StarTowerFigure(int baseDamage, int hue, int range, 
			int cooldown, Position position){
		super(baseDamage, hue, range, cooldown, position);
	}
	
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.draw(CustomShapes.createStar(this.getPosition(),
				TowerFigure.TEMP_SIZE));
	}

	@Override
	public Figures getShape(){
		return SHAPE;
	}

	@Override
	public void attack() {
		if(this.hasTarget() && !this.isOnCooldown() &&
				this.getTarget().isAlive()){
			projectiles.put(new StarProjectileFigure(
					this.getHue(),
					this.getBaseDamage(),
					new Position(this.getPosition())),
					this.getTarget());
		}
	}

}
