package gui;

import java.awt.Polygon;
import java.awt.Shape;
import start.Figures;

@SuppressWarnings("serial")
class SquareRepresentation extends FigureRepresentation {

	public SquareRepresentation() {
		this.creatureType = Figures.SQUARE;
	}

	public SquareRepresentation(int hue, float scale) {
		super(hue, scale);
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
