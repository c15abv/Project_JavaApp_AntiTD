package creatures;

import java.awt.Color;
import java.awt.Graphics2D;

import start.Figures;
import start.GameLevel;
import start.Position;
import utilities.CustomShapes;

/**
 * A triangle CreatureFigure.
 * 
 * @author Alexander Believ
 * @version 1.0
 */
public class TriangleCreatureFigure extends CreatureFigure {

	public static final Figures shape = Figures.TRIANGLE;

	/**
	 * See CreatureFigure constructor.
	 */
	public TriangleCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level) {
		this(hue, scale, position, orientation, level, BASE_SPEED);
	}

	/**
	 * See CreatureFigure constructor.
	 */
	public TriangleCreatureFigure(int hue, double scale, Position position,
			Orientation orientation, GameLevel level, double speed) {
		super(hue, scale, position, orientation, level, speed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see creatures.CreatureFigure#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(getColor());
		g2d.fill(CustomShapes.createTriangle(this.getPosition(),
				(int) (getScale() * CreatureFigure.BASE_SIZE)));
		g2d.setColor(Color.WHITE);
		g2d.drawString("" + percentLife(),
				(int) (getPosition().getX()
						- (getScale() * CreatureFigure.BASE_SIZE) / 2),
				getPosition().getY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see creatures.CreatureFigure#getShape()
	 */
	@Override
	public Figures getShape() {
		return shape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see creatures.CreatureFigure#isCollision(start.Position)
	 */
	@Override
	public boolean isCollision(Position position) {
		int dx = Math.abs(position.getX() - this.getPosition().getX());
		int dy = Math.abs(position.getY() - this.getPosition().getY());
		double outerRadius = this.getScale() * CreatureFigure.BASE_SIZE / 2;
		double innerRadius;
		double collPointAngle;

		if (dx > outerRadius || dy > outerRadius) {
			return false;
		}

		if (dx > 0) {
			collPointAngle = Math.atan(dy / dx);
		} else {
			collPointAngle = Math.PI / 2;
		}

		innerRadius = getInnerRadius(collPointAngle);

		if (innerRadius >= Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2))) {
			return true;
		}

		return false;
	}

	private double getInnerRadius(double angle) {
		return this.getScale() * CreatureFigure.BASE_SIZE / 2
				* Math.sin(Math.PI / 6) / Math.sin(2 * Math.PI / 6 + angle);
	}

}
