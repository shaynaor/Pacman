package mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
/**
 * This class creates a connection to msql server and gets end game relevant
 * data from it.
 * @author Alex vaisman, Shay naor.
 */
public class EndGameStatistics {
	private int mapId;
	private double myScore;
	private int myPosition;
	private double firstPlace;
	private double secondPlace;
	private double thirdPlace;

	public EndGameStatistics(String playerStat, int mapId) {
		this.mapId = mapId;
		String line[] = {};
		String line2[] = {};
		line = playerStat.split(",");
		String score = line[2];
		line2 = score.split(":");
		this.myScore = Double.parseDouble(line2[1]);

		ConnectToServer();
	}

	private void ConnectToServer() {
		String jdbcUrl = "jdbc:mysql://ariel-oop.xyz:3306/oop"; 
		String jdbcUser = "student";
		String jdbcPassword = "student";
		ArrayList<Double> scores = new ArrayList<Double>();
		int currentmap = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

			Statement statement = connection.createStatement();

			// select data
			String allCustomersQuery = "SELECT * FROM logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			;
			while (resultSet.next()) {
				currentmap = resultSet.getInt("SomeDouble");
				if (currentmap == this.mapId) {
					scores.add(resultSet.getDouble("Point"));
				}

			}

			resultSet.close();
			statement.close();
			connection.close();
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Collections.sort(scores);
		Collections.reverse(scores);

		this.firstPlace = scores.get(0);
		this.secondPlace = scores.get(1);
		this.thirdPlace = scores.get(2);
		
		int counter = 1;
		for(int i = 0 ; i < scores.size() ; i++) {
			if(scores.get(i) > myScore) {
				counter++;
			}
		}
		
		this.myPosition = counter;

	}
	/* Getters */
	public int getMapId() {
		return mapId;
	}

	public double getMyScore() {
		return myScore;
	}

	public int getMyPosition() {
		return myPosition;
	}

	public double getFirstPlace() {
		return firstPlace;
	}

	public double getSecondPlace() {
		return secondPlace;
	}

	public double getThirdPlace() {
		return thirdPlace;
	}

}
