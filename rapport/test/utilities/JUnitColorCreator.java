package utilities;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * JUnitColorCreator.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class JUnitColorCreator{
	
	@Test
	public void testHueToColorLessThan360(){
		int hue = 155;
		double hueR = (double)hue / (double)360;
		Color color = Color.getHSBColor((float)hueR, (float)ColorCreator.SATURATION,
				(float)ColorCreator.VALUE);
		Color colorTest = ColorCreator.generateColorFromHue(hue);
		
		assertTrue(color.equals(colorTest));
	}
	
	@Test
	public void testHueToColorGreaterThan360(){
		int hue = 744;
		double hueR = (double)24 / (double)360;
		Color color = Color.getHSBColor((float)hueR, (float)ColorCreator.SATURATION,
				(float)ColorCreator.VALUE);
		Color colorTest = ColorCreator.generateColorFromHue(hue);
		
		assertTrue(color.equals(colorTest));
	}
	
	@Test
	public void testHueDiffFirstQuadrant(){
		int hue1, hue2, diff;
		
		hue1 = 0;
		hue2 = 89;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffSecondQuadrant(){
		int hue1, hue2, diff;
		
		hue1 = 159;
		hue2 = 179;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffThirdQuadrant(){
		int hue1, hue2, diff;
		
		hue1 = 181;
		hue2 = 250;

		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffFourthQuadrant(){
		int hue1, hue2, diff;
		
		hue1 = 300;
		hue2 = 333;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrantLessThan180_1(){
		int hue1, hue2, diff;
		
		hue1 = 181;
		hue2 = 300;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrantLessThan180_2(){
		int hue1, hue2, diff;
		
		hue1 = 122;
		hue2 = 250;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrantLessThan180_3(){
		int hue1, hue2, diff;
		
		hue1 = 82;
		hue2 = 151;
		
		diff = hue2 - hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrantLessThan180_4(){
		int hue1, hue2, diff;
		
		hue1 = 15;
		hue2 = 305;
		
		diff = (360 - hue2) + hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrantLessThan180_5(){
		int hue1, hue2, diff;
		
		hue1 = 17;
		hue2 = 266;
		
		diff = (360 - hue2) + hue1;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrant180_1(){
		int hue1, hue2, diff;
		
		hue1 = 0;
		hue2 = 180;
		
		diff = 180;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
	
	@Test
	public void testHueDiffDifferentQuadrant180_2(){
		int hue1, hue2, diff;
		
		hue1 = 180;
		hue2 = 360;
		
		diff = 180;
		
		assertEquals(ColorCreator.getHueDiff(hue1, hue2), diff);
		assertEquals(ColorCreator.getHueDiff(hue2, hue1), diff);
	}
}
