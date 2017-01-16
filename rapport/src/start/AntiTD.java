package start;

import javax.swing.SwingUtilities;

import gui.GameView;
import gui.GameViewAdapter;

/**
 * Main thread.
 * 
 * @author Karolina Jonzen and Alexander Ekstrom
 *
 */
public class AntiTD {

	public static void main(String[] args) {
		GameViewAdapter gameViewAdapter;
		
		if(args.length > 0)	{
			gameViewAdapter = new GameViewAdapter(args[0]);

		}	else	{
			gameViewAdapter = new GameViewAdapter();

		}
		GameView gui = new GameView(gameViewAdapter);

		SwingUtilities.invokeLater(() -> gui.show());
	}


}
