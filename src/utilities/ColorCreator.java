package utilities;

import java.awt.Color;
import java.util.Random;

/**
 * ColorCreator contains methods used to generate
 * colors or hues based on set constants for the
 * value and saturation. The value used is 0.5, while
 * the saturation is 1.0.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class ColorCreator{
	
	public static final double VALUE = 0.5;
	public static final double SATURATION = 1.0;
	
	/**
	 * Generates a Color object based on the given hue.
	 * 
	 * @param hue the hue.
	 * @return the color object.
	 */
	public static Color generateColorFromHue(int hue){
		float hue_rad = (float)(hue) / (float)(360);
		return Color.getHSBColor(hue_rad, (float)SATURATION, (float)VALUE);
	}
	
	/**
	 * Returns a random hue on the range 0-360, end points
	 * inclusive.
	 * @returna random hue on the range 0-360, end points
	 * inclusive.
	 */
	public static int getRandomHue(){
		return new Random().nextInt(361);
	}
	
	/**	
	 * Returns the hue difference between two hues.
	 * The order of the input hues does not matter.
	 * 
	 * @param hue1 first hue.
	 * @param hue2 second hue.
	 * @return the hue difference between two hues.
	 */
	public static int getHueDiff(int hue1, int hue2){
		int diff = 0;
		
		if(hue1 >= 180 && hue2 >= 180){
			diff = Math.abs(hue1 - hue2);
		}else if(hue1 < 180 && hue2 < 180){
			diff = Math.abs(hue1 - hue2);
		}else if(hue1 < 90 && hue2 >= 270){
			diff = Math.abs(hue1 + (360 - hue2));
		}else if(hue1 >= 270 && hue2 < 90){
			diff = Math.abs(hue2 + (360 - hue1));
		}else if(hue1 >= 90 && hue1 < 180 &&
				hue2 >= 180 && hue2 < 270){
			diff = Math.abs((180 - hue1) + (hue2 - 180));
		}else if(hue2 >= 90 && hue2 < 180 &&
				hue1 >= 180 && hue1 < 270){
			diff = Math.abs((hue1 - 180) + (180 - hue2));
		}else if(hue1 < 90 && hue1 >= 0 &&
				hue2 >= 180 && hue2 < 270){
			if(hue2 - hue1 > 180){
				diff = (360 - hue2) + hue1;
			}else{
				diff = hue2 - hue1;
			}
		}else if(hue1 >= 180 && hue1 < 270 &&
				 hue2 < 90 && hue2 >= 0){
			if(hue1 - hue2 > 180){
				diff = (360 - hue1) + hue2;
			}else{
				diff = hue1 - hue2;
			}
		}else if(hue1 < 180 && hue1 >= 90 &&
				hue2 < 360 && hue2 >= 270){
			if(hue2 - hue1 > 180){
				diff = (360 - hue2) + hue1;
			}else{
				diff = hue2 - hue1;
			}
		}else if(hue1 < 360 && hue1 >= 270 &&
				hue2 < 180 && hue2 >= 90){
			if(hue1 - hue2 > 180){
				diff = (360 - hue1) + hue2;
			}else{
				diff = hue1 - hue2;
			}
		}
		
		return diff;
	}
}
