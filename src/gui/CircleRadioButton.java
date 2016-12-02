package gui;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import start.Figures;

import java.awt.Dimension;

class CircleRadioButton extends FigureRadioButton {

	@Override
	public Shape createShape() {

		Shape s = new Ellipse2D.Double(0, 0, 80, 80);

		return s;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(80, 80);
	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.CIRCLE;
	}

}
