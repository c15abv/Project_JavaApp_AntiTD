package towers;

import static org.junit.Assert.*;

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

import org.junit.Test;

import creatures.CircleCreatureFigure;
import start.Position;
import utilities.ActionTimer;

public class JUnitCircleTower {
	
	class TestWindow extends JFrame implements Runnable{ 
		
		private static final long serialVersionUID = 1L;
		
		private Canvas canvas;
		private BufferStrategy buffer;
		private GraphicsEnvironment graphicsE;
		private GraphicsDevice device;
		private GraphicsConfiguration configuration;
		private BufferedImage bufferedImage;
		private Graphics g;
		private Graphics2D g2d;
		private CircleTowerFigure fig;
		
		TestWindow(CircleTowerFigure fig){
			this.fig = fig;
			
			this.setIgnoreRepaint(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			canvas = new Canvas();
			canvas.setIgnoreRepaint(true);
			canvas.setSize(400, 400);
			
			this.add(canvas);
			this.pack();
			this.setVisible(true);
			
			canvas.createBufferStrategy(2);
			buffer = canvas.getBufferStrategy();
			
			graphicsE = GraphicsEnvironment.getLocalGraphicsEnvironment();
			device = graphicsE.getDefaultScreenDevice();
			configuration = device.getDefaultConfiguration();
			bufferedImage = configuration.createCompatibleImage(400, 400);
			
			g = null;
			g2d = null;
			
		}

		@Override
		public void run(){
			while(true){
				g2d = bufferedImage.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0, 0, 399, 399);
				
				fig.render(g2d);
				
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
	
	@Test
	public void testRender(){
		CircleTowerFigure fig = new CircleTowerFigure(0,0,0,new Position(150, 150));
		TestWindow window = new TestWindow(fig);
		Thread thread = new Thread(window);
		thread.start();
		
		try{
			Thread.sleep(10000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/*@Test
	public void testNotificationFunctionality(){
		CircleTowerFigure fig = new CircleTowerFigure(0,0,0,new Position(150, 150));
		CircleCreatureFigure creature = new CircleCreatureFigure(0,0,new Position(0,0));
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
		
		try{
			Thread.sleep(2000);
			timer.terminate();
		}catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	

}
