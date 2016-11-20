package start;

public class Main {

	public static void main(String args[]){
		new Thread(new GameRunner(new Game())).start();
	}
}
