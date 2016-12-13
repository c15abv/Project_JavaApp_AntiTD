package gui;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import start.Figures;

import java.awt.Dimension;

/**
 * Radio button to represent circle creatures.
 * 
 * @author karro
 *
 */
@SuppressWarnings("serial")
class CircleRadioButton extends FigureRadioButton {

	/**
	 * Constructor that initiates the button.
	 */
	public CircleRadioButton() {
		initButton();
	}

	@Override
	public Shape createShape() {

		Shape s = new Ellipse2D.Double(0, 0, TEMP_SIZE, TEMP_SIZE);

		return s;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TEMP_SIZE, TEMP_SIZE);
	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.CIRCLE;
	}

}
