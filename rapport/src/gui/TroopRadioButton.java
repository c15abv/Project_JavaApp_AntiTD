package gui;

import java.awt.Shape;

import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Radio button for troop types being displayed in the troop panel.
 * 
 * @author Karolina Jonzen
 * @version 1.0
 */
@SuppressWarnings("serial")
class TroopRadioButton extends FigureRadioButton {
	private FigureRepresentation figure;
	private int index;

	/**
	 * Constructor that initiates the button based on the given parameters.
	 */
	public TroopRadioButton(FigureRepresentation figure, int index) {
		this.figure = figure;
		this.index = index;
		// this.figureType = figure.getCreatureType();
		initButton();
	}

	/**
	 * Returns the buttons index in the troop panel.
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	@Override
	public Shape createShape() {

		Shape s = figure.getShape();

		return s;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (getModel().isSelected()) {
			// g.setColor(Color.DARK_GRAY.darker().darker());
			// this.setForeground(Color.DARK_GRAY.darker().darker());
			// figure.setHue(0);
			this.getParent().setBackground(Color.GRAY);
		} else if (getModel().isRollover()) {
			// g.setColor(Color.DARK_GRAY);
			// figure.setHue(100);

			this.getParent().setBackground(Color.GRAY.brighter());
		} else {
			// g.setColor(Color.GRAY);
			// figure.setHue(200);
			this.getParent().setBackground(UIManager.getColor("control"));
		}

		g.setColor(figure.getColor());
		((Graphics2D) g).fill(shape);
	}

	@Override
	public void initButton() {
		setFigureType();

		this.setText(figureType.toString());
		this.setContentAreaFilled(false);
		this.setActionCommand(String.valueOf(index));
		this.shape = createShape();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (FigureRepresentation.TEMP_SIZE * 2),
				(int) (FigureRepresentation.TEMP_SIZE * 2));
	}

	@Override
	public void setFigureType() {
		this.figureType = figure.getCreatureType();
	}

}
