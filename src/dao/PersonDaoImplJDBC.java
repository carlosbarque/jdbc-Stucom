package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Person;

public class PersonDaoImplJDBC implements PersonDao {

	static final String DB_URL = "jdbc:mysql://localhost:3306/jdbc?serverTimezone=UTC&useSSL=false";
	static final String USER = "root";
	static final String PASS = "";

	public static final String GET_PEOPLE = "select * from person";
	public static final String GET_PERSON = "select * from person where dni = ?";

	private Connection conn;

	public void connect() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	public void disconnect() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	public ArrayList<Person> getPeople() throws SQLException {
		ArrayList<Person> people = new ArrayList<>();
		try (Statement ps = conn.createStatement()) {
			System.out.println(ps.toString());

			try (ResultSet rs = ps.executeQuery(GET_PEOPLE)) {
				// for each result add to list
				while (rs.next()) {
					// get data for each person from column
					people.add(
							new Person(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
				}
			}
		}
		return people;

	}

	@Override
	public Person getPerson(String dni) throws SQLException {
		try (PreparedStatement preparedStatement = conn.prepareStatement(GET_PERSON)) {
			preparedStatement.setString(1, dni);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					// Map the result set to a Person object
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					String surname = resultSet.getString("surname");
					int age = resultSet.getInt("age");

					// Create and return a Person object
					return new Person(id,dni,name,surname,age);
				}
			}
		}

		// Return null if no person is found with the given ID
		return null;
	}

}
