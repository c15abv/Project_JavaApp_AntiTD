package towers;

import java.awt.Graphics2D;
import java.util.Map;

import creatures.CreatureFigure;
import projectiles.ProjectileFigure;
import projectiles.TriangleProjectileFigure;
import start.Figures;
import start.Position;
import utilities.CustomShapes;

/**
 * TriangleTowerFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class TriangleTowerFigure extends TowerFigure {

	public static final Figures SHAPE = Figures.TRIANGLE;

	public TriangleTowerFigure(int baseDamage, int hue, int range,
			Position position) {
		super(baseDamage, hue, range, COOLDOWN, position);
	}

	public TriangleTowerFigure(int baseDamage, int hue, int range, int cooldown,
			Position position) {
		super(baseDamage, hue, range, cooldown, position);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(this.getColor());
		g2d.draw(CustomShapes.createTriangle(this.getPosition(),
				TowerFigure.SIZE));

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
		if (this.hasTarget() && !this.isOnCooldown()
				&& this.getTarget().isAlive()) {
			projectiles.put(new TriangleProjectileFigure(this.getHue(),
					this.getBaseDamage(), new Position(this.getPosition())),
					this.getTarget());
		}
	}

}
