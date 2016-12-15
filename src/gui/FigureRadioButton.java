package gui;

import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JRadioButton;

import start.Figures;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Abstract radio button class to represent different creatures the user can
 * choose from.
 * 
 * @author Karolina Jonzén
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class FigureRadioButton extends JRadioButton {
	protected static final int TEMP_SIZE = 50;
	protected Shape shape;
	protected Figures figureType;

	/**
	 * Initiates the radio button.
	 */
	public void initButton() {
		setFigureType();

		this.setText(figureType.toString());
		this.setContentAreaFilled(false);
		this.setActionCommand(this.getText());
		this.shape = createShape();

	}

	@Override
	public void paintBorder(Graphics g) {
		((Graphics2D) g).draw(shape);
	}

	@Override
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

	@Override
	public boolean contains(int x, int y) {
		return shape.contains(x, y);
	}

	@Override
	public abstract Dimension getPreferredSize();

	/**
	 * Creates and returns the shape of the radio button.
	 * 
	 * @return
	 */
	public abstract Shape createShape();

	/**
	 * Sets the figure type of the radio button.
	 */
	public abstract void setFigureType();

}
