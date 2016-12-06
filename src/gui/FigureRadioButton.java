package gui;

import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JRadioButton;

import start.Figures;

import java.awt.Color;
import java.awt.Dimension;

public abstract class FigureRadioButton extends JRadioButton {
	protected static final int TEMP_SIZE = 70;
	private Shape shape = createShape();
	protected Figures figureType;

	public FigureRadioButton() {
		super();
		setFigureType();
		initButton();
	}

	public void paintBorder(Graphics g) {
		((Graphics2D) g).draw(shape);
	}

	public void paintComponent(Graphics g) {
		if (getModel().isSelected()) {
			g.setColor(Color.DARK_GRAY.darker().darker());
		} else if (getModel().isRollover()) {
			g.setColor(Color.DARK_GRAY);
		} else {
			g.setColor(Color.GRAY);
		}

		((Graphics2D) g).fill(shape);
	}

	public boolean contains(int x, int y) {
		return shape.contains(x, y);
	}

	public void initButton() {
		this.setText(this.figureType.toString());
		this.setContentAreaFilled(false);
		this.setActionCommand(this.getText());

	}

	public abstract Dimension getPreferredSize();

	public abstract Shape createShape();

	public abstract void setFigureType();

}
