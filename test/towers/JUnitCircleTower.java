package towers;

import static org.junit.Assert.*;

import org.junit.Test;

import creatures.CircleCreatureFigure;
import creatures.CreatureFigure.Orientation;
import creatures.SquareCreatureFigure;
import creatures.TriangleCreatureFigure;
import start.AttackingPlayer;
import start.Figures;
import start.Position;
import utilities.ActionTimer;

public class JUnitCircleTower{
	
	@Test
	public void testTemplate(){
		TowerFigureTemplate template = new TowerFigureTemplate(
				Figures.CIRCLE, 100, 200, 20, 50);
		CircleTowerFigure fig = (CircleTowerFigure)template.createNewTower(
				new Position(250,250));
		
		assertTrue(fig.getPosition().equals(new Position(250,250)));
		assertTrue(fig.getHue() == 100);
		assertTrue(fig.getRange() == 200);
		assertTrue(fig.getBaseDamage() == 20);
		assertTrue(fig.getShape() == Figures.CIRCLE);
	}
	
	@Test
	public void testNotificationFunctionality(){
		CircleTowerFigure fig = new CircleTowerFigure(0,0,0,new Position(150, 150));
		CircleCreatureFigure creature = new CircleCreatureFigure(0,0,new Position(0,0),
				Orientation.RANDOM, null);
		ActionTimer timer = new ActionTimer();
		Thread timerThread = new Thread(timer);
		
		fig.setActionTimer(timer);
		timerThread.start();
		
		fig.setIsOnCooldown(false);
		fig.setTarget(creature);
		fig.setTowerAction(() -> {
			fig.attack();
			fig.setIsOnCooldown(true);
			fig.getActionTimer().setTimer(0, fig, 1000);
		});
		fig.setOnNotification(id -> {
			fig.setIsOnCooldown(false);
		});
		
		fig.update();
		assertTrue(fig.isOnCooldown());
		
		try{
			Thread.sleep(1100);
			timer.terminate();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		assertFalse(fig.isOnCooldown());
		
	}
	
	@Test
	public void testTowerEquals(){
		CircleTowerFigure fig1 = new CircleTowerFigure(0,0,0,new Position(150, 150));
		CircleTowerFigure fig2 = new CircleTowerFigure(0,0,0,new Position(100, 100));
		CircleTowerFigure fig3 = new CircleTowerFigure(0,0,0,new Position(150, 150));
		
		assertFalse(fig1.equals(fig2));
		assertTrue(fig1.equals(fig1));
		assertTrue(fig1.equals(fig3));
		
		assertTrue(fig1.hashCode() == fig3.hashCode());
	}
	
	@Test
	public void testFindsTargetWithinRange(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,200,
				new Position(200,200));
		
		player1.addCreatureFigure(fig);
		player2.addTowerFigure(tFig);
		
		assertTrue(tFig.getTarget() == null);
		
		player2.update();
		
		assertTrue(tFig.getTarget().equals(fig));
	}
	
	@Test
	public void testDoNotFindTarget(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,100,
				new Position(200,200));
		
		player1.addCreatureFigure(fig);
		player2.addTowerFigure(tFig);
		
		assertTrue(tFig.getTarget() == null);
		
		player2.update();
		
		assertTrue(tFig.getTarget() == null);
	}
	
	@Test
	public void testFindsBestTargetAmongShapes(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig1 = new CircleCreatureFigure(100,1,
				new Position(100,100), Orientation.RANDOM, null);
		SquareCreatureFigure fig2 = new SquareCreatureFigure(100,1,
				new Position(200,100), Orientation.RANDOM, null);
		TriangleCreatureFigure fig3 = new TriangleCreatureFigure(100,1,
				new Position(300,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player1.addCreatureFigure(fig2);
		player1.addCreatureFigure(fig3);
		player2.addTowerFigure(tFig);
		
		player2.update();
		
		assertTrue(tFig.getTarget().equals(fig1));
	}
	
	@Test
	public void testFindBestTargetOnHue(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		TriangleCreatureFigure fig1 = new TriangleCreatureFigure(200,1,
				new Position(100,100), Orientation.RANDOM, null);
		SquareCreatureFigure fig2 = new SquareCreatureFigure(150,1,
				new Position(200,100), Orientation.RANDOM, null);
		TriangleCreatureFigure fig3 = new TriangleCreatureFigure(100,1,
				new Position(300,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player1.addCreatureFigure(fig2);
		player1.addCreatureFigure(fig3);
		player2.addTowerFigure(tFig);
		
		player2.update();
		
		assertTrue(tFig.getTarget().equals(fig1));
	}
	
	@Test
	public void testFindBestTargetOnLife(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		TriangleCreatureFigure fig1 = new TriangleCreatureFigure(200,1,
				new Position(100,100), Orientation.RANDOM, null);
		SquareCreatureFigure fig2 = new SquareCreatureFigure(199,1,
				new Position(200,100), Orientation.RANDOM, null);
		TriangleCreatureFigure fig3 = new TriangleCreatureFigure(195,1,
				new Position(300,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player1.addCreatureFigure(fig2);
		player1.addCreatureFigure(fig3);
		player2.addTowerFigure(tFig);
		
		fig2.setDamageTaken(41);
		
		player2.update();
		
		assertTrue(tFig.getTarget().equals(fig2));
	}
	
	@Test
	public void testTargetDies(){
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		TriangleCreatureFigure fig1 = new TriangleCreatureFigure(200,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,180,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player2.addTowerFigure(tFig);
		
		player2.update();
		
		assertTrue(tFig.getTarget().equals(fig1));
		
		fig1.setDamageTaken(101);
		
		player1.update();
		player2.update();
		
		assertTrue(tFig.getTarget() == null);
	}
	
	@Test
	public void testDoesNotAttackOnCooldown(){
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig1 = new CircleCreatureFigure(200,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(5,200,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player2.addTowerFigure(tFig);
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(0, tFig, 1500);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		thread.start();
		
		assertTrue(fig1.getHitPoints() == 100);
		
		long limit = (long) (System.nanoTime() + 1490 * 10e5);
		
		while(limit > System.nanoTime()){
			player1.update();
			player2.update();
		}
		
		assertTrue(fig1.getHitPoints() == 95);
		timer.terminate();
	}
	
	@Test
	public void testDamageAgaintBadTarget(){
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		TriangleCreatureFigure fig1 = new TriangleCreatureFigure(20,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(20,200,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player2.addTowerFigure(tFig);
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(0, tFig, 1500);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		thread.start();
		
		assertTrue(fig1.getHitPoints() == 100);
		
		long limit = (long) (System.nanoTime() + 1490 * 10e5);
		
		while(limit > System.nanoTime()){
			player1.update();
			player2.update();
		}
		
		assertTrue(fig1.getHitPoints() == 98);
		
		timer.terminate();
	}
	
	@Test
	public void testDefaultAction(){
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig1 = new CircleCreatureFigure(200,1,
				new Position(100,100), Orientation.RANDOM, null);
		CircleTowerFigure tFig = new CircleTowerFigure(20,200,500,
				new Position(200,200));
		
		player1.addCreatureFigure(fig1);
		player2.addTowerFigure(tFig);
		
		tFig.setActionTimer(timer);
		
		thread.start();
		
		assertTrue(fig1.getHitPoints() == 100);
		
		long limit = (long) (System.nanoTime() + 1999 * 10e5);
		
		while(limit > System.nanoTime()){
			player1.update();
			player2.update();
		}
		
		assertTrue(fig1.getHitPoints() == 60);
		
		timer.terminate();
	}
}
