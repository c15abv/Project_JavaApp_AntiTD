package start;

import java.util.Random;

/**
 * Figures holds the various shapes which are available
 * in the AntiTD application.
 * 
 * @author Alexander Believ
 * @version 1.0
 *
 */
public enum Figures{
	
	CIRCLE, TRIANGLE, SQUARE, STAR;
	
	/**
	 * Gets a random Figures value.
	 * @return a random Figures value.
	 */
	public static Figures getRandom(){
		int val;
		
		return (val = new Random().nextInt(4)) == 0 ? Figures.CIRCLE :
			val == 1 ? Figures.TRIANGLE : val == 2 ? Figures.SQUARE :
				Figures.STAR;
	}
	
	/**
	 * Gets a random Figures value excluding the
	 * given Figures value.
	 * @param figure a Figures value.
	 * @return a random Figures value excluding the
	 * given Figures value.
	 */
	public static Figures getRandom(Figures figure){
		Figures figure1, figure2, figure3;
		int val;
		
		switch(figure){
		case CIRCLE:
			figure1 = Figures.SQUARE;
			figure2 = Figures.STAR;
			figure3 = Figures.TRIANGLE;
			break;
		case SQUARE:
			figure1 = Figures.CIRCLE;
			figure2 = Figures.STAR;
			figure3 = Figures.TRIANGLE;
			break;
		case STAR:
			figure1 = Figures.SQUARE;
			figure2 = Figures.CIRCLE;
			figure3 = Figures.TRIANGLE;
			break;
		case TRIANGLE:
			figure1 = Figures.SQUARE;
			figure2 = Figures.STAR;
			figure3 = Figures.CIRCLE;
			break;
		default:
			figure1 = figure2 = figure3 = null;
			break;
		}
		
		return (val = new Random().nextInt(3)) == 0 ? figure1:
			val == 1 ? figure2 : figure3;
	}
}
