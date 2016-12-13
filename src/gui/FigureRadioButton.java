package gui;

import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JRadioButton;

import start.Figures;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Abstract radio button class to represent different creatures. 
 * 
 * @author karro
 *
 */
@SuppressWarnings("serial")
public abstract class FigureRadioButton extends JRadioButton {
	protected static final int TEMP_SIZE = 50;
	protected Shape shape;
	protected Figures figureType;

	/*public FigureRadioButton() {
		super();
		initButton();
	}*/

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
		setFigureType();

		this.setText(figureType.toString());
		this.setContentAreaFilled(false);
		this.setActionCommand(this.getText());
		this.shape = createShape();

	}

	public abstract Dimension getPreferredSize();

	public abstract Shape createShape();

	public abstract void setFigureType();

}
