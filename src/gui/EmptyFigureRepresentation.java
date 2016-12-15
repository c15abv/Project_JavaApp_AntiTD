package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * FigureRepresentation that represent figures that has not yet been set.
 * 
 * @author Karolina Jonzén
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EmptyFigureRepresentation extends FigureRepresentation {

	@Override
	public Shape createShape() {
		JTextField questionMark = new JTextField("?") {

			@Override
			public void setBorder(Border border) {
				// No!
			}
		};
		questionMark.setBackground(UIManager.getColor("control"));
		questionMark.setForeground(Color.LIGHT_GRAY.darker());
		questionMark.setFont(new Font("Arial", Font.BOLD, 60));
		questionMark.setEditable(false);
		this.add(questionMark);
		return null;
	}

	@Override
	public void paintComponent(Graphics g) {

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (FigureRepresentation.TEMP_SIZE * 2),
				(int) (FigureRepresentation.TEMP_SIZE) * 2);
	}
}
