package gui;

import java.awt.Polygon;
import java.awt.Shape;

import creatures.CreatureFigure.Orientation;
import start.Figures;

/**
 * FigureRepresentation that represents triangle figures.
 * 
 * @author Karolina Jonzén
 * @version 1.0
 */
@SuppressWarnings("serial")
class TriangleRepresentation extends FigureRepresentation {

	/**
	 * Constructor that sets the creature type of the representation.
	 */
	public TriangleRepresentation() {
		this.creatureType = Figures.TRIANGLE;
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
	public TriangleRepresentation(int hue, float scale, boolean isTeleporterCreature, Orientation orientation, int cost) {
		super(hue, scale, isTeleporterCreature, orientation, cost);
		this.creatureType = Figures.TRIANGLE;
	}

	@Override
	public Shape createShape() {
		Polygon p = new Polygon(
				new int[] { (int) ((TEMP_SIZE * scale) / 2),
						(int) (TEMP_SIZE * scale), 0 },
				new int[] { 0, (int) (TEMP_SIZE * scale),
						(int) (TEMP_SIZE * scale) },
				3);

		return p;
	}

}
