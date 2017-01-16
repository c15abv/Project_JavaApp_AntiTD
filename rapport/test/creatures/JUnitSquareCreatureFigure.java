package creatures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import creatures.CreatureFigure.Orientation;
import start.Figures;
import start.Position;
import utilities.ActionTimer;

/**
 * JUnitSquareCreatureFigure.
 * 
 * @author Alexander Beliaev
 * @version 1.0
 */
public class JUnitSquareCreatureFigure{

	@Test
	public void testTemplate(){
		CreatureFigureTemplate template = new CreatureFigureTemplate(Figures.SQUARE, 100,
				1, 100, CreatureFigure.Orientation.RANDOM, null);
		SquareCreatureFigure fig = (SquareCreatureFigure)template.createNewCreature(
				new Position(250,250), null);
		
		assertTrue(fig.getHue() == 100);
		assertTrue(fig.isAlive() == true);
		assertTrue(fig.isFinished() == false);
		assertTrue(fig.getPosition().equals(new Position(250,250)));
		assertTrue(fig.getScale() == 1);
		assertTrue(fig.getOrientation() == Orientation.RANDOM);
		assertTrue(fig.getShape() == Figures.SQUARE);
	}
	
	@Test 
	public void testIfDiesOnFatalDamage(){
		SquareCreatureFigure fig = new SquareCreatureFigure(100, 1,
				new Position(250,250), Orientation.RANDOM, null);
		
		fig.setDamageTaken(CircleCreatureFigure.BASE_HITPOINTS * 1 + 1);
		
		assertTrue(!fig.isAlive());
	}
	
	@Test
	public void testOnSpawnAction(){
		SquareCreatureFigure fig = new SquareCreatureFigure(100, 1,
				new Position(250,250), Orientation.RANDOM, null);
		
		fig.addOnSpawnAction(() -> {
			fig.setDamageTaken(25);
		});
		
		fig.update();
		assertTrue(fig.getHitPoints() == 75);
	}
	
	@Test
	public void testOnSpawnTimedAction(){
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		SquareCreatureFigure fig = new SquareCreatureFigure(100, 1,
				new Position(250,250), Orientation.RANDOM, null);
		
		thread.start();
		
		fig.setActionTimer(timer);
		
		fig.addOnSpawnTimedAction((long)1000, () -> {
			fig.setDamageTaken(25);
		});
		
		fig.update();
		
		assertTrue(fig.getHitPoints() == 100);
		
		try{
			Thread.sleep(1001);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		fig.update();
		assertEquals(fig.getHitPoints(), 75);
		
		timer.terminate();
	}
	
	@Test
	public void testActiveAction(){
		SquareCreatureFigure fig = new SquareCreatureFigure(100, 1,
				new Position(250,250), Orientation.RANDOM, null);
		Position pos = new Position(fig.getPosition().getX() + 100,
				fig.getPosition().getY() + 100);
		
		fig.addActiveAction(() -> {
			fig.setPosition(new Position(fig.getPosition().getX() + 1,
					fig.getPosition().getY() + 1));
		});
		
		for(int i=0; i < 100; i++){
			fig.update();
		}
		
		assertEquals(pos, fig.getPosition());
	}
	
	@Test
	public void testOnDeathAction(){
		SquareCreatureFigure fig = new SquareCreatureFigure(100, 1,
				new Position(250,250), Orientation.RANDOM, null);
		
		fig.addOnDeathAction(() -> {
			fig.setPosition(new Position(fig.getPosition().getX() + 1,
					fig.getPosition().getY() + 1));
		});
		
		fig.setDamageTaken(101);
		
		fig.update();
		assertTrue(new Position(251, 251).equals(fig.getPosition()));
		
		fig.update();
		assertEquals(new Position(251, 251), fig.getPosition());
		
	}
}
