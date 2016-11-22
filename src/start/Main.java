package start;

public class Main {

	public static void main(String args[]){
		//temporary;
		//DO NOT RUN APPLICATION AS IS.
		new Thread(new GameRunner(new Game(new GameLevel()))).start();
	}
}
