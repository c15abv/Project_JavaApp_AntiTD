package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Shape;

import javax.swing.JButton;
import javax.swing.JPanel;

import start.Figures;
import utilities.ColorCreator;

public class FigureRepresentation extends JPanel {
	protected static final int TEMP_SIZE = 70;
	private Figures creatureType;
	private int hue;
	private float scale;
	private Shape shape = createShape();

	public FigureRepresentation(Figures creatureType, int hue, float scale) {
		super();
		this.creatureType = creatureType;
		this.hue = hue;
		this.scale = scale;

	}

	private Shape createShape() {
		Polygon p = new Polygon();
		p.addPoint(0, 0); // use this.getWidth() method if you want to
							// create based on screen size
		p.addPoint(0, (int)(TEMP_SIZE*scale));
		p.addPoint((int)(TEMP_SIZE*scale), (int)(TEMP_SIZE*scale));
		p.addPoint((int)(TEMP_SIZE*scale), 0);	

		return p;
	}

	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		g.setColor(ColorCreator.generateColorFromHue(this.hue));
		this.shape = createShape();
		((Graphics2D) g).fill(shape);
	}
	
	@Override
	public void repaint(){
		super.repaint();
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int)(TEMP_SIZE*scale), (int)(TEMP_SIZE*scale));
	}

	public Figures getCreatureType() {
		return creatureType;
	}
	

	public void setCreatureType(Figures creatureType) {
		this.creatureType = creatureType;
	}
	

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
