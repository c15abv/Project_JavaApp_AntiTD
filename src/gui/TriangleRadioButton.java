package gui;

import java.awt.Polygon;
import java.awt.Shape;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;
import utilities.CustomShapes;

import java.awt.Dimension;

class TriangleRadioButton extends FigureRadioButton {

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(80, 80);
	}

	@Override
	public Shape createShape() {

		Polygon p = new Polygon(new int[] { 40, 80, 0 },
				new int[] { 0, 80, 80 }, 3);

		return p;

	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.TRIANGLE;
	}

}
