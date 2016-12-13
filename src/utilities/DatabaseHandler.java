package utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseHandler {
	private static String jdbcDriver = "com.mysql.jdbc.Driver";
	private final static String url = "mysql.cs.umu.se";
	private final static String id = "v135ht16g9";
	private final static String password = "Oosh8ing";
	private final MysqlDataSource dataSource;
	private Connection conn;
	private Statement stmt;
	// private final static String dbName = "HIGHSCORES";

	public static void main(String[] args) {
		DatabaseHandler db = new DatabaseHandler();

	/*	try {
			db.insertToDatabase("Karro", 10, new Time(123456), "Level 1");
			db.insertToDatabase("Karro1", 5, new Time(123456), "Level 2");
			db.insertToDatabase("Karro2", 100, new Time(123456), "Level 1");
			db.insertToDatabase("Karro3", 50, new Time(123456), "Level 1");
			db.insertToDatabase("Karro4", 40, new Time(123456), "Level 5");
			db.insertToDatabase("Karro5", 3000, new Time(123456), "Level 1");

		} catch (SQLException e) {
			e.printStackTrace();
		}*/

		try {
			ArrayList<HighScoreInfo> highScoreList = db.getFromDatabase();

			for (int i = 0; i < highScoreList.size(); i++) {
				HighScoreInfo h = highScoreList.get(i);
				System.out.println(h.getName());
				System.out.println(h.getScore());
				System.out.println(h.getTime());
				System.out.println(h.getLevel());
				System.out.println("*****************************");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public DatabaseHandler() {
		dataSource = new MysqlDataSource();
		dataSource.setUser(id);
		dataSource.setPassword(password);
		dataSource.setServerName(url);
	}

	/**
	 * Gets 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<HighScoreInfo> getFromDatabase() throws SQLException {

		ArrayList<HighScoreInfo> highScoreList = new ArrayList<>();
		HighScoreInfo highScore;

		String query = "SELECT * FROM " + id
				+ ". Highscore ORDER BY SCORE DESC LIMIT 5";

		setUpConnection();

		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			highScore = new HighScoreInfo();
			highScore.setName(rs.getString("NAME"));
			highScore.setScore(rs.getInt("SCORE"));
			highScore.setTime(rs.getTime("TIME"));
			highScore.setLevel(rs.getString("LEVEL"));
			highScoreList.add(highScore);
		}

		closeConnection();

		return highScoreList;

	}

	/**
	 * Inserts the given parameters into database.
	 * 
	 * @param name
	 * @param score
	 * @param time
	 * @param level
	 * @throws SQLException
	 */
	public void insertToDatabase(String name, int score, Time time, String level)
			throws SQLException {
		setUpConnection();

		String query2 = "INSERT INTO " + id
				+ ". Highscore (NAME, SCORE, TIME, LEVEL) VALUES (?, ?, ?, ?)";

		java.sql.PreparedStatement preparedStmt = conn.prepareStatement(query2);
		preparedStmt.setString(1, name);
		preparedStmt.setInt(2, score);
		preparedStmt.setTime(3, time);
		preparedStmt.setString(4, level);

		preparedStmt.execute();

		closeConnection();
	}

	/**
	 * Sets up connection to database.
	 * 
	 * @throws SQLException
	 */
	public void setUpConnection() throws SQLException {
		conn = dataSource.getConnection();
		stmt = conn.createStatement();
	}

	/**
	 * Closes connection to database.
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		conn.close();
	}

}
