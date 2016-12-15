package start;

import javax.swing.SwingUtilities;

import gui.GameView;
import gui.GameViewAdapter;

public class Test6 {

	public static void main(String[] args){
		GameViewAdapter gameViewAdapter = new GameViewAdapter();
		GameView gui = new GameView(gameViewAdapter);

		SwingUtilities.invokeLater(() -> gui.show());
	}

}
