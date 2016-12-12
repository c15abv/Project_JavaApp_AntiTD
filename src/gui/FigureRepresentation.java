package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JPanel;
import javax.swing.UIManager;

import creatures.CreatureFigure.Orientation;
import start.Direction;
import start.Figures;
import utilities.ColorCreator;

@SuppressWarnings("serial")
public abstract class FigureRepresentation extends JPanel {
	protected static final int TEMP_SIZE = 70;
	protected Figures creatureType;
	protected Color color = UIManager.getColor("control");
	protected float scale = 1;
	protected Shape shape = createShape();
	protected int hue;
	protected boolean isTeleportCreature = false;
	protected Orientation orientation;
	protected int index;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	protected int cost;

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public FigureRepresentation() {
		super();
	}

	public boolean isTeleportCreature() {
		return isTeleportCreature;
	}

	public void setIsTeleportCreature(boolean isTeleportCreature) {
		this.isTeleportCreature = isTeleportCreature;
	}

	public Orientation getOrientation() {
		return orientation;
	}
	

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public FigureRepresentation(int hue, float scale, boolean isTeleportCreature, Orientation orientation, int cost) {
		super();
		this.color = ColorCreator.generateColorFromHue(hue);
		this.hue = hue;
		this.scale = scale;
		this.isTeleportCreature = isTeleportCreature;
		this.orientation = orientation;
		this.cost = cost;

	}

	public abstract Shape createShape();

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		g.setColor(color);
		this.shape = createShape();
		((Graphics2D) g).fill(shape);
	}

	@Override
	public void repaint() {
		super.repaint();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (TEMP_SIZE * scale),
				(int) (TEMP_SIZE * scale));
	}

	public Figures getCreatureType() {
		return creatureType;
	}

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
		this.color = ColorCreator.generateColorFromHue(hue);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Shape getShape() {
		return shape;
	}

	public Color getColor() {
		return color;
	}
	
	
}
