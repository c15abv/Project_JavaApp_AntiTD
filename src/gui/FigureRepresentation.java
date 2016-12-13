package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.UIManager;

import creatures.CircleCreatureFigure;
import creatures.CreatureFigure;
import creatures.SquareCreatureFigure;
import creatures.TriangleCreatureFigure;
import creatures.CreatureFigure.Orientation;
import start.Figures;
import start.GameLevel;
import start.Position;
import utilities.ColorCreator;

/**
 * Class to represent different creature figures.
 * 
 * @author karro
 *
 */
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
	protected GameLevel level;
	
	public FigureRepresentation(int hue, float scale, boolean isTeleportCreature, Orientation orientation, int cost) {
		super();
		this.color = ColorCreator.generateColorFromHue(hue);
		this.hue = hue;
		this.scale = scale;
		this.isTeleportCreature = isTeleportCreature;
		this.orientation = orientation;
		this.cost = cost;

	}

	
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
	
	public CreatureFigure createNewCreature(Position position){
		return creatureType == Figures.CIRCLE ? 
				createNewCircleCreature(position) : 
			(creatureType == Figures.SQUARE ? 
					createNewSquareCreature(position) :
				(creatureType == Figures.TRIANGLE ? 
						createNewTriangleCreature(position) : 
							createNewRandomCreature(position)));
	}
	
	private CircleCreatureFigure createNewCircleCreature(
			Position position){
		return new CircleCreatureFigure(hue, scale, position,
				orientation, level);
	}
	
	private SquareCreatureFigure createNewSquareCreature(
			Position position){
		return new SquareCreatureFigure(hue, scale, position,
				orientation, level);
	}

	private TriangleCreatureFigure createNewTriangleCreature(
			Position position){
		return new TriangleCreatureFigure(hue, scale, position,
				orientation, level);
	}

	private CreatureFigure createNewRandomCreature(
			Position position){
		int randomInt = new Random().nextInt(3);
		
		return randomInt == 0 ? createNewCircleCreature(position) :
			(randomInt == 1 ? createNewSquareCreature(position) :
				createNewTriangleCreature(position));
	}
	
	
}
