package creatures;

import static org.junit.Assert.*;
import org.junit.Test;
import start.Figures;
import start.Position;

public class JUnitTriangleCreatureFigure {
	private TriangleCreatureFigure figure = new TriangleCreatureFigure(0,0, new Position(0,0));
	
	
	@Test
	public void testShapeIsTriangle() {
		assertEquals(Figures.TRIANGLE, figure.getShape());
	}
	
	@Test
	public void testIsAlive () {
		assertTrue(figure.isAlive());
	}
	
	@Test
	public void testIsNotAlive () {
		int currentHitPoints = figure.getHitPoints();
		figure.setDamageTaken(currentHitPoints);
		
		assertFalse(figure.isAlive());
	}
	
	@Test
	public void testMoveAction ()	{
		figure.addActiveAction(() ->	{
			figure.moveForward();
		});
		
		figure.update();
		assertEquals(figure.getPosition(), new Position(1,1));
	}
	
	@Test
	public void testSpawnAction ()	{
		figure.addOnSpawnAction(() ->	{
			int currentHitPoints = figure.getHitPoints();
			figure.setDamageTaken(currentHitPoints);
		});
		
		figure.update();
		
		assertFalse(figure.isAlive());
	}
	
	@Test
	public void testUpdates ()	{
		figure.addOnSpawnAction(() ->	{
			int currentHitPoints = figure.getHitPoints();
			figure.setDamageTaken(currentHitPoints);
		});
		
		for(int i = 0; i < 10; i++)	{

			figure.update();
		}
		
		assertEquals(figure.hasSpawned, 1);
	}
	
	@Test
	public void testUpdates2 ()	{
		figure.addActiveAction(() ->	{
			figure.moveForward();
		});
		
		for(int i = 0; i < 10; i++)	{

			figure.update();
		}
		
		assertEquals(figure.getPosition(), new Position(10,10));
	}
	
	
	
	

}
