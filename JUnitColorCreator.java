package utilities;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Test;

public class JUnitColorCreator{
	
	class TestWindow extends JFrame{
		
		private static final long serialVersionUID = 1L;
		
		TestCanvas canvas;
		
		TestWindow(){
			canvas = new TestCanvas();
			setTitle("test");
	        setSize(400, 400);
	        setLayout(new BorderLayout());
	        add(canvas, BorderLayout.CENTER);
		}
		
		void showWindow(){
			setVisible(true);
		}
		
		void update(Color color){
			canvas.update(color);
			repaint();
		}
		
	}
	
	class TestCanvas extends Canvas{
		
		private static final long serialVersionUID = 1L;
		
		Color color;
		
		TestCanvas(){
			color = new Color(0,0,0);
			setBackground(Color.WHITE);
		}
		
		@Override
	    public void paint(Graphics g) {
	        super.paint(g);
	        Graphics2D drawImage = (Graphics2D) g;
            drawImage.setColor(color);
            drawImage.fillRect(100, 100, 150, 150);
	    }
		
		void update(Color color){
			this.color = color;
			repaint();
		}
		
	}

	@Test
	public void testGenerateColorFromHueWithFullRange(){
		TestWindow window = new TestWindow();
		SwingUtilities.invokeLater(() -> window.showWindow());
		
		for(int i=0; i <= 720; i++){
			final int hue = i;
			SwingUtilities.invokeLater(() -> window.update(ColorCreator.generateColorFromHue(hue)));
			try{
				Thread.sleep(50);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
