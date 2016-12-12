package gui;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import creatures.CreatureFigure.Orientation;
import start.Figures;

@SuppressWarnings("serial")
class CircleRepresentation extends FigureRepresentation {
	
	public CircleRepresentation() {
		this.creatureType = Figures.CIRCLE;
	}
	
	public CircleRepresentation(int hue, float scale, boolean isTeleporterCreature, Orientation orientation, int cost) {
		super(hue, scale, isTeleporterCreature, orientation, cost);
		this.creatureType = Figures.CIRCLE;
	}

	@Override
	public Shape createShape() {
		Shape s = new Ellipse2D.Double(0, 0, TEMP_SIZE*scale, TEMP_SIZE*scale);
		
		return s;
	}

}
