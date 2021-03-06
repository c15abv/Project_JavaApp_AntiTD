package towers;

import java.awt.Graphics2D;
import java.util.Map;

import creatures.CreatureFigure;
import projectiles.CircleProjectileFigure;
import projectiles.ProjectileFigure;
import start.Figures;
import start.Position;

/**
 * CircleTowerFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class CircleTowerFigure extends TowerFigure {

	public static final Figures SHAPE = Figures.CIRCLE;

	public CircleTowerFigure(int baseDamage, int hue, int range,
			Position position) {
		this(baseDamage, hue, range, COOLDOWN, position);
	}

	public CircleTowerFigure(int baseDamage, int hue, int range, int cooldown,
			Position position) {
		super(baseDamage, hue, range, cooldown, position);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(getColor());
		g2d.drawOval(getPosition().getX() - TowerFigure.SIZE / 2,
				getPosition().getY() - TowerFigure.SIZE / 2, TowerFigure.SIZE,
				TowerFigure.SIZE);

		for (Map.Entry<ProjectileFigure, CreatureFigure> entry : projectiles
				.entrySet()) {
			entry.getKey().render(g2d);
		}
	}

	@Override
	public Figures getShape() {
		return SHAPE;
	}

	@Override
	public void attack() {
		if (hasTarget() && !isOnCooldown() && getTarget().isAlive()) {
			projectiles.put(new CircleProjectileFigure(getHue(),
					getBaseDamage(), new Position(getPosition())), getTarget());
		}
	}
}
