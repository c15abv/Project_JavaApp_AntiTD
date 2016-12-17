package gui;

import java.awt.Polygon;
import java.awt.Shape;

import creatures.CreatureFigure.Orientation;
import start.Figures;

/**
 * FigureRepresentation that represents square figures.
 * 
 * @author Karolina Jonz�n
 * @version 1.0
 */
@SuppressWarnings("serial")
class SquareRepresentation extends FigureRepresentation {

	/**
	 * Constructor that sets the creature type of the representation.
	 */
	public SquareRepresentation() {
		this.creatureType = Figures.SQUARE;
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
	public SquareRepresentation(int hue, float scale, boolean isTeleporterCreature, Orientation orientation, int cost) {
		super(hue, scale, isTeleporterCreature, orientation, cost);
		this.creatureType = Figures.SQUARE;
	}

	@Override
	public Shape createShape() {
		Polygon p = new Polygon();
		p.addPoint(0, 0); // use this.getWidth() method if you want to
							// create based on screen size
		p.addPoint(0, (int) (TEMP_SIZE * scale));
		p.addPoint((int) (TEMP_SIZE * scale), (int) (TEMP_SIZE * scale));
		p.addPoint((int) (TEMP_SIZE * scale), 0);

		return p;
	}

}
