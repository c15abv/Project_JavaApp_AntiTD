package projectiles;

import java.awt.Graphics2D;

import creatures.CreatureFigure;
import start.Figures;
import start.Position;

public class CircleProjectileFigure extends ProjectileFigure{

	public static final Figures shape = Figures.CIRCLE;
	private int damage;
	
	public CircleProjectileFigure(int hue, int damage, 
			Position position){
		super(hue, position);
		this.damage = damage;
	}

	@Override
	public void update(CreatureFigure figure){
		super.update(figure);
		if(figure.isCollision(this.getPosition())){
			figure.setDamageTaken((int)(ProjectileFigure.calculateDamageMultiplier(
					CircleProjectileFigure.shape, figure.getShape(),
					this.getHue(), figure.getHue()) * this.damage));
			this.setIsDead();
		}
	}
	
	@Override
	public void render(Graphics2D g2d){
		g2d.setColor(this.getColor());
		g2d.fillOval(this.getPosition().getX() - ProjectileFigure.TEMP_SIZE/2,
				this.getPosition().getY() - ProjectileFigure.TEMP_SIZE/2,
				ProjectileFigure.TEMP_SIZE,
				ProjectileFigure.TEMP_SIZE);
	}

	@Override
	public Figures getShape(){
		return shape;
	}
	
	
}
