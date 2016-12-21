package projectiles;

import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;
import utilities.CustomShapes;

/**
 * TriangleProjectileFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class TriangleProjectileFigure extends ProjectileFigure{
	
	public static final Figures shape = Figures.TRIANGLE;
	private int damage;
	
	public TriangleProjectileFigure(int hue, int damage,
			Position position){
		super(hue, position);
		this.damage = damage;
	}

	@Override
	public void update(CreatureFigure figure){
		boolean tempAlive;
		super.update(figure);
		if(figure.isCollision(this.getPosition())){
			tempAlive = figure.isAlive();
			figure.setDamageTaken((int)(ProjectileFigure.calculateDamageMultiplier(
					this.getShape(), figure.getShape(),
					this.getHue(), figure.getHue()) * this.damage));
			if(tempAlive && !figure.isAlive()){
				killedTarget = true;
			}
			this.setIsDead();
		}
	}
	
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.draw(CustomShapes.createTriangle(this.getPosition(),
				ProjectileFigure.TEMP_SIZE));
	}

	@Override
	public Figures getShape(){
		return shape;
	}
}
