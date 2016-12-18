package projectiles;

import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;
import utilities.CustomShapes;

/**
 * A star projectile.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class StarProjectileFigure extends ProjectileFigure{

	public static final Figures shape = Figures.STAR;
	private int damage;
	
	/**
	 * Creates a new star projectile with the specified hue and
	 * starting position.
	 * 
	 * @param hue the hue.
	 * @param damage the base damage.
	 * @param position the starting position.
	 */
	public StarProjectileFigure(int hue, int damage,
			Position position){
		super(hue, position);
		this.damage = damage;
	}

	/* (non-Javadoc)
	 * @see projectiles.ProjectileFigure#update(creatures.CreatureFigure)
	 */
	@Override
	public void update(CreatureFigure figure){
		boolean tempAlive;
		super.update(figure);
		if(figure.isCollision(this.getPosition())){
			tempAlive = figure.isAlive();
			figure.setDamageTaken((int)(ProjectileFigure.calculateDamageMultiplier(
					this.getShape(), figure.getShape(),
					this.getHue(), figure.getHue()) * this.damage));
			if(tempAlive && !figure.isAlive()){
				killedTarget = true;
			}
			this.setIsDead();
		}
	}
	
	/* (non-Javadoc)
	 * @see projectiles.ProjectileFigure#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.draw(CustomShapes.createStar(this.getPosition(),
				ProjectileFigure.TEMP_SIZE));
	}

	/* (non-Javadoc)
	 * @see projectiles.ProjectileFigure#getShape()
	 */
	@Override
	public Figures getShape(){
		return shape;
	}
}
