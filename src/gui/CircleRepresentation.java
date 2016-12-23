package gui;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import creatures.CreatureFigure.Orientation;
import start.Figures;

/**
 * FigureRepresentation that represents circle figures.
 * 
 * @author Karolina Jonzen
 * @version 1.0
 */
@SuppressWarnings("serial")
class CircleRepresentation extends FigureRepresentation {

	/**
	 * Constructor that sets the creature type of the representation.
	 */
	public CircleRepresentation() {
		this.creatureType = Figures.CIRCLE;
	}

	/**
	 * Constructor that initiates the representation with the given parameters
	 * and sets the creature type.
	 * 
	 * @param hue
	 * @param scale
	 * @param isTeleportCreature
	 * @param orientation
	 * @param cost
	 */
	public CircleRepresentation(int hue, float scale,
			boolean isTeleporterCreature, Orientation orientation, int cost) {
		super(hue, scale, isTeleporterCreature, orientation, cost);
		this.creatureType = Figures.CIRCLE;
	}

	@Override
	public Shape createShape() {
		Shape s = new Ellipse2D.Double(0, 0, TEMP_SIZE * scale,
				TEMP_SIZE * scale);

		return s;
	}

}
