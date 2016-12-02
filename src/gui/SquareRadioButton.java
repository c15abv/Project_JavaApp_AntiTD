package gui;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JRadioButton;

import start.Figures;

import java.awt.Dimension;

class SquareRadioButton extends FigureRadioButton {

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(80, 80);
	}

	@Override
	public Shape createShape() {
		Polygon p = new Polygon();
		p.addPoint(0, 0); // use this.getWidth() method if you want to
							// create based on screen size
		p.addPoint(0, 80);
		p.addPoint(80, 80);
		p.addPoint(80, 0);

		int y1Points[] = { 0, 0, 80, 80 };

		return p;
	}

	@Override
	public void setFigureType() {
		this.figureType = Figures.SQUARE;
		
	}

}
