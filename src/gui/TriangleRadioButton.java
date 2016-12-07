package gui;

import java.awt.Polygon;
import java.awt.Shape;
import start.Figures;
import java.awt.Dimension;

@SuppressWarnings("serial")
class TriangleRadioButton extends FigureRadioButton {
	
	public TriangleRadioButton() {
		initButton();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TEMP_SIZE, TEMP_SIZE);
	}

	@Override
	public Shape createShape() {

		Polygon p = new Polygon(new int[] { TEMP_SIZE / 2, TEMP_SIZE, 0 },
				new int[] { 0, TEMP_SIZE, TEMP_SIZE }, 3);

		return p;

	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.TRIANGLE;
	}

}
