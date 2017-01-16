package utilities;

public class IdCounter {

	public static final int DEFAULT = 0;

	private long id;

	public IdCounter() {
		this(DEFAULT);
	}

	public IdCounter(long id) {
		this.id = id;
	}

	public void increment() {
		id++;
	}

	public long getId() {
		return id;
	}
}
