package start;

import java.util.Random;

public enum Figures{
	
	CIRCLE, TRIANGLE, SQUARE, STAR;
	
	public static Figures getRandom(){
		int val;
		
		return (val = new Random().nextInt(4)) == 0 ? Figures.CIRCLE :
			val == 1 ? Figures.TRIANGLE : val == 2 ? Figures.SQUARE :
				Figures.STAR;
	}
}
