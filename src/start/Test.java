package start;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import creatures.CircleCreatureFigure;
import creatures.CreatureFigure.Orientation;
import creatures.SquareCreatureFigure;
import creatures.TriangleCreatureFigure;
import towers.AITowerFigures;
import towers.CircleTowerFigure;
import towers.DefendingPlayer;
import towers.SquareTowerFigure;
import utilities.ActionTimer;

public class Test{
	
	class TestWindow extends JFrame implements Runnable{ 
		
		private static final long serialVersionUID = 1L;
		private volatile boolean running = true;
		
		private static final int UPS = 60;
		private static final long MARGIN = (long) (1000000000 / UPS);
		private static final int FPS_SKIP = 10;
		
		private Canvas canvas;
		private BufferStrategy buffer;
		private GraphicsEnvironment graphicsE;
		private GraphicsDevice device;
		private GraphicsConfiguration configuration;
		private BufferedImage bufferedImage;
		private Graphics g;
		private Graphics2D g2d;
		private AttackingPlayer player1;
		private DefendingPlayer player2;
		private ActionTimer timer;
		
		TestWindow(AttackingPlayer player1, DefendingPlayer player2,
				ActionTimer timer){
			this.player1 = player1;
			this.player2 = player2;
			this.timer = timer;
			this.setIgnoreRepaint(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			canvas = new Canvas();
			canvas.setIgnoreRepaint(true);
			canvas.setSize(600, 600);
			
			this.add(canvas);
			this.pack();
			this.setVisible(true);
			
			canvas.createBufferStrategy(2);
			buffer = canvas.getBufferStrategy();
			
			graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
			device = graphicsE.getDefaultScreenDevice();
			configuration = device.getDefaultConfiguration();
			bufferedImage = configuration.createCompatibleImage(600, 600);
			
			g = null;
			g2d = null;
			
		}

		@Override
		public void run(){
			
			long lastUpdateTime = System.nanoTime();
			int loops;
			
			while(running){
				loops = 0;
				while(System.nanoTime() > lastUpdateTime && loops < FPS_SKIP){
					player1.update();
					player2.update();
					lastUpdateTime += MARGIN;
					loops++;
				}
				
				g2d = bufferedImage.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, 599, 599);
				
				player1.render(g2d);
				player2.render(g2d);
				
				g = buffer.getDrawGraphics();
				g.drawImage(bufferedImage, 0, 0, null);
				
				if(!buffer.contentsLost()){
					buffer.show();
				}
				
				if(g != null){
					g.dispose();
				}
				
				if(g2d != null){
					g2d.dispose();
				}
				
			}
		}
	}
	
	public static void main(String[] args){
		ActionTimer timer = new ActionTimer();
		Thread thread = new Thread(timer);
		AttackingPlayer player1 = new AttackingPlayer(100);
		DefendingPlayer player2 = new DefendingPlayer(100);
		AITowerFigures ai = new AITowerFigures(player1, player2);
		player2.setTowersAI(ai);
		
		CircleCreatureFigure fig = new CircleCreatureFigure(100,1,
				new Position(100,100), Orientation.RANDOM, null);
		
		TriangleCreatureFigure fig2 = new TriangleCreatureFigure(100,1,
				new Position(100,200), Orientation.RANDOM, null);
		
		SquareCreatureFigure fig3 = new SquareCreatureFigure(30,1,
				new Position(400,300), Orientation.RANDOM, null);
		
		fig.addActiveAction(() -> {
			if(fig.getPosition().getX() < 500 &&
					fig.getPosition().getY() == 100){
				fig.setPosition(new Position(fig.getPosition().getX() + 2,
						fig.getPosition().getY()));
			}else if(fig.getPosition().getX() == 500 &&
					fig.getPosition().getY() < 500){
				fig.setPosition(new Position(fig.getPosition().getX(),
						fig.getPosition().getY() + 2));
			}else if(fig.getPosition().getX() > 100 &&
					fig.getPosition().getY() == 500){
				fig.setPosition(new Position(fig.getPosition().getX() - 2,
						fig.getPosition().getY()));
			}else if(fig.getPosition().getX() == 100 &&
					fig.getPosition().getY() > 100){
				fig.setPosition(new Position(fig.getPosition().getX(),
						fig.getPosition().getY() - 2));
			}
		});
		
		CircleTowerFigure tFig = new CircleTowerFigure(30,180,300,
				new Position(200,300));
		
		SquareTowerFigure tFig2 = new SquareTowerFigure(10,0,200,
				new Position(300, 300));
		
		tFig.setActionTimer(timer);
		tFig.setTowerAction(() -> {
			tFig.attack();
			tFig.setIsOnCooldown(true);
			tFig.getActionTimer().setTimer(0, tFig, 1500);
		});
		tFig.setOnNotification(id -> {
			tFig.setIsOnCooldown(false);
		});
		
		tFig2.setActionTimer(timer);
		tFig2.setTowerAction(() -> {
			tFig2.attack();
			tFig2.setIsOnCooldown(true);
			tFig2.getActionTimer().setTimer(1, tFig2, 1000);
		});
		tFig2.setOnNotification(id -> {
			tFig2.setIsOnCooldown(false);
		});
		
		player1.addCreatureFigure(fig);
		player1.addCreatureFigure(fig2);
		player1.addCreatureFigure(fig3);
		player2.addTowerFigure(tFig);
		player2.addTowerFigure(tFig2);
		
		thread.start();
		
		TestWindow window = new Test().new TestWindow(player1,player2,timer);
		Thread thread2 = new Thread(window);
		thread2.start();
	}
}
