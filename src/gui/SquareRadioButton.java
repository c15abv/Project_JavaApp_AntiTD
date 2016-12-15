package gui;

import java.awt.Polygon;
import java.awt.Shape;
import start.Figures;
import java.awt.Dimension;

/**
 * Radio button to represent square creatures.
 * 
 * @author karro
 *
 */
@SuppressWarnings("serial")
class SquareRadioButton extends FigureRadioButton {

	/**
	 * Constructor that initiates the button.
	 */
	public SquareRadioButton() {
		initButton();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TEMP_SIZE, TEMP_SIZE);
	}

	@Override
	public Shape createShape() {
		Polygon p = new Polygon();
		p.addPoint(0, 0); // use this.getWidth() method if you want to
							// create based on screen size
		p.addPoint(0, TEMP_SIZE);
		p.addPoint(TEMP_SIZE, TEMP_SIZE);
		p.addPoint(TEMP_SIZE, 0);

		return p;
	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.SQUARE;
		
	}

}
