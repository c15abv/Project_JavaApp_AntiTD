package utilities;

import java.awt.Color;

public class ColorCreator{
	
	private static final double CONST = 0.33333;
	private static final double VALUE = 0.5;
	private static final double SATURATION = 1.0;
	private static final double LS_MULTIPLIER = 
			(VALUE + SATURATION) - VALUE * SATURATION;
	private static final double LL_MULTIPLIER =
			VALUE - LS_MULTIPLIER;
	
	//8-bit color generator based on given hue.
	//broken...
	public static Color generateColorFromHueBROKEN(int hue){
		double hue_rad = (double)(hue) / (double)(360);
		double red_temp = hue_rad + CONST;
		double green_temp = hue_rad;
		double blue_temp = hue_rad - CONST;
		
		red_temp = adjustRange(red_temp);
		green_temp = adjustRange(green_temp);
		blue_temp = adjustRange(blue_temp);
		
		red_temp = runCorrectionTests(red_temp);
		red_temp = runCorrectionTests(green_temp);
		red_temp = runCorrectionTests(blue_temp);
		
		return new Color((float)red_temp, (float)green_temp, (float)blue_temp);
	}
	
	private static double adjustRange(double color){
		if(color < 0){
			color += 1;
		}else if(color > 1){
			color -= 1;
		}
		
		return color;
	}
	
	private static double runCorrectionTests(double color){
		double color_temp = color;
		
		if(color_temp == correctionTest1(color) && color_temp == correctionTest2(color)
				&& color_temp == correctionTest3(color)){
			color = LL_MULTIPLIER;
		}
		
		return color;
	}
	
	private static double correctionTest1(double color){
		return (6 * color < 1 ? LL_MULTIPLIER + 
				((LS_MULTIPLIER - LL_MULTIPLIER) * 6 * color) : color);
	}
	
	private static double correctionTest2(double color){
		return (2 * color < 1 ? LS_MULTIPLIER : color);
	}

	private static double correctionTest3(double color){
		return (3 * color < 2 ? LL_MULTIPLIER + ((LS_MULTIPLIER - LL_MULTIPLIER)
				 * (2 * CONST - color) * 6) : color);
	}
	
	public static Color generateColorFromHue(int hue){
		float hue_rad = (float)(hue) / (float)(360);
		return Color.getHSBColor(hue_rad, (float)SATURATION, (float)VALUE);
	}
	
}
