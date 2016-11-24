package utilities;

import java.awt.Color;

public class ColorCreator{
	
	private static final float LUMINATION = 0.5f;
	private static final float SATURATION = 1.0f;
	private static final float LS_MULTIPLIER = 
			(LUMINATION + SATURATION) - LUMINATION * SATURATION;
	private static final float LL_MULTIPLIER =
			LUMINATION - LS_MULTIPLIER;
	
	//8-bit color generator based on given hue.
	public static Color generateColorFromHue(int hue){
		float hue_rad = hue / 360;
		float red_temp = hue_rad + (float)(1/3);
		float green_temp = hue_rad;
		float blue_temp = hue_rad - (float)(1/3);
		
		adjustRange(red_temp);
		adjustRange(green_temp);
		adjustRange(blue_temp);
		
		runCorrectionTests(red_temp);
		runCorrectionTests(green_temp);
		runCorrectionTests(blue_temp);
		
		return new Color(Math.round(red_temp * 255),
				Math.round(green_temp * 255),
				Math.round(blue_temp * 255));
	}
	
	private static void adjustRange(float color){
		if(color < 0){
			color += 1;
		}else if(color > 1){
			color -= 1;
		}
	}
	
	private static void runCorrectionTests(float color){
		if(!correctionTest1(color) && !correctionTest2(color)
				&& !correctionTest3(color)){
			color = LL_MULTIPLIER;
		}
	}
	
	private static boolean correctionTest1(float color){
		if(6 * color < 1){
			color = LL_MULTIPLIER + 
					((LS_MULTIPLIER - LL_MULTIPLIER) * 6 * color);
			return true;
		}
		return false;
	}
	
	private static boolean correctionTest2(float color){
		if(2 * color < 2){
			color = LS_MULTIPLIER;
			return true;
		}
		return false;
	}

	private static boolean correctionTest3(float color){
		if(3 * color < 2){
			 color = LL_MULTIPLIER + ((LS_MULTIPLIER - LL_MULTIPLIER)
					 * ((float)(2/3) - color) * 6);
			 return true;
		}
		return false;
	}
	
}
