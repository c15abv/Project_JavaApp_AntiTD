package projectiles;

import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;

/**
 * A square projectile.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class SquareProjectileFigure extends ProjectileFigure {

	public static final Figures shape = Figures.SQUARE;
	private int damage;

	/**
	 * Creates a new square projectile with the specified hue and starting
	 * position.
	 * 
	 * @param hue
	 *            the hue.
	 * @param damage
	 *            the base damage.
	 * @param position
	 *            the starting position.
	 */
	public SquareProjectileFigure(int hue, int damage, Position position) {
		super(hue, position);
		this.damage = damage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projectiles.ProjectileFigure#update(creatures.CreatureFigure)
	 */
	@Override
	public void update(CreatureFigure figure) {
		boolean tempAlive;
		super.update(figure);
		if (figure.isCollision(getPosition())) {
			tempAlive = figure.isAlive();
			figure.setDamageTaken((int) (ProjectileFigure
					.calculateDamageMultiplier(getShape(), figure.getShape(),
							getHue(), figure.getHue())
					* damage));
			if (tempAlive && !figure.isAlive()) {
				killedTarget = true;
			}
			setIsDead();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projectiles.ProjectileFigure#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(getColor());
		g2d.drawRect(getPosition().getX() - ProjectileFigure.TEMP_SIZE / 2,
				getPosition().getY() - ProjectileFigure.TEMP_SIZE / 2,
				ProjectileFigure.TEMP_SIZE, ProjectileFigure.TEMP_SIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see projectiles.ProjectileFigure#getShape()
	 */
	@Override
	public Figures getShape() {
		return shape;
	}

}
