package start;

import gui.GameView;
import gui.GameViewAdapter;
import gui.LevelInfo;

public class Test6 {
	
	public static void main(String[] args){
		GameViewAdapter gameViewAdapter = new GameViewAdapter();
		
		GameView gv = new GameView(gameViewAdapter);
		gv.show();
	}

}
