package creatures;

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
import start.Figures;
import start.Position;

public class JUnitCircleCreatureFigure {
	private CircleCreatureFigure figure = new CircleCreatureFigure(0, 0,
			new Position(0, 0));

	class TestWindow extends JFrame implements Runnable {

		private static final long serialVersionUID = 1L;

		private Canvas canvas;

		private BufferStrategy buffer;

		private GraphicsEnvironment graphicsE;

		private GraphicsDevice device;

		private GraphicsConfiguration configuration;

		private BufferedImage bufferedImage;

		private Graphics g;

		private Graphics2D g2d;

		private CircleCreatureFigure fig;

		TestWindow(CircleCreatureFigure fig) {

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

		public void run() {

			while (true) {

				g2d = bufferedImage.createGraphics();

				g2d.setColor(Color.BLACK);

				g2d.fillRect(0, 0, 399, 399);

				fig.render(g2d);

				g = buffer.getDrawGraphics();

				g.drawImage(bufferedImage, 0, 0, null);

				if (!buffer.contentsLost()) {

					buffer.show();

				}

				if (g != null) {

					g.dispose();

				}

				if (g2d != null) {

					g2d.dispose();

				}

			}

		}

	}

	@Test

	public void testRender() {

		CircleCreatureFigure fig = new CircleCreatureFigure(150,0, new Position(150,150));
		TestWindow window = new TestWindow(fig);

		Thread thread = new Thread(window);

		thread.start();

		try {

			Thread.sleep(10000);

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

	}

	@Test
	public void testShapeIsCircle() {
		assertEquals(Figures.CIRCLE, figure.getShape());
	}

	@Test
	public void testIsAlive() {
		assertTrue(figure.isAlive());
	}

	@Test
	public void testIsNotAlive() {
		int currentHitPoints = figure.getHitPoints();
		figure.setDamageTaken(currentHitPoints);

		assertFalse(figure.isAlive());
	}

	@Test
	public void testMoveAction() {
		figure.addActiveAction(() -> {
			figure.moveForward();
		});

		figure.update();
		assertEquals(figure.getPosition(), new Position(1, 1));
	}

	@Test
	public void testSpawnAction() {
		figure.addOnSpawnAction(() -> {
			int currentHitPoints = figure.getHitPoints();
			figure.setDamageTaken(currentHitPoints);
		});

		figure.update();

		assertFalse(figure.isAlive());
	}

	@Test
	public void testUpdates() {
		figure.addOnSpawnAction(() -> {
			int currentHitPoints = figure.getHitPoints();
			figure.setDamageTaken(currentHitPoints);
		});

		for (int i = 0; i < 10; i++) {

			figure.update();
		}

		assertEquals(figure.hasSpawned, 1);
	}

	@Test
	public void testUpdates2() {
		figure.addActiveAction(() -> {
			figure.moveForward();
		});

		for (int i = 0; i < 10; i++) {

			figure.update();
		}

		assertEquals(figure.getPosition(), new Position(10, 10));
	}

}
