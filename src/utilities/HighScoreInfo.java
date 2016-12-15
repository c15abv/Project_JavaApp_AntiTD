package utilities;

import java.sql.Time;

/**
 * Class that holds high score information.
 * 
 * @author Karolina Jonzén and Alexander Ekström
 * @version 1.0
 *
 */
public class HighScoreInfo {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	private String name;
	private int score;
	private Time time;
	private String level;

}
