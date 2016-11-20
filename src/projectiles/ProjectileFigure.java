package projectiles;

import start.Figures;
import start.Position;

public abstract class ProjectileFigure{
	
	private int hue;
	private float functioning; //0-1, scale on damage output.
								//render with transparency.
								//lower functioning => higher transparency.
	private Position position;
	
	public ProjectileFigure(int hue, float functioning, Position position){
		this.hue = hue;
		this.functioning = functioning;
		this.position = position;
	}
	
	public void update(){
	}
	
	public abstract void render();
	public abstract Figures getShape();
	
	public Position getPosition(){
		return position;
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object object){
		return true;
	}
	
}
