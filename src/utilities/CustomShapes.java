package utilities;

import java.awt.Shape;
import java.awt.geom.Path2D;

import start.Position;

public class CustomShapes {

	private static final int STAR_RAYS = 5;
	
	public static Shape createStar(Position position, double size){
        Path2D path = new Path2D.Double();
        double deltaRayAngleRadius = Math.PI / STAR_RAYS;
        double rayAngleRadius;
        double relX;
        double relY;
        
        for(int i=0; i < STAR_RAYS * 2; i++){
        	rayAngleRadius = Math.toRadians(-18) + i * deltaRayAngleRadius;
        	relX = Math.cos(rayAngleRadius);
        	relY = Math.sin(rayAngleRadius);
            
            if((i & 1) == 0){
                relX *= size * 2.63;
                relY *= size * 2.63;
            }else{
                relX *= size;
                relY *= size;
            }
            
            if(i == 0){
                path.moveTo(position.getX() + relX, position.getY() + relY);
            }else{
                path.lineTo(position.getX() + relX, position.getY() + relY);
            }
        }
        path.closePath();
        
        return path;
    }
	
	public static Shape createTriangle(Position position, double size){
		Path2D path = new Path2D.Double();
		double angleRadius = Math.toRadians(60);
		double relX = Math.cos(angleRadius) * size;
		double relY = Math.cos(angleRadius) * size;
		
		path.moveTo(position.getX(), position.getY() - size/2);
		path.lineTo(position.getX() + relX, position.getY() + relY);
		path.lineTo(position.getX() - relX, position.getY() + relY);
		path.lineTo(position.getX(), position.getY() - size/2);
		
		path.closePath();
		
		return path;
	}
}
