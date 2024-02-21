package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Person;

public interface PersonDao {
	
	public void connect() throws SQLException ;

	public void disconnect() throws SQLException;

	public ArrayList<Person> getPeople() throws SQLException;
	
	public Person getPerson(String dni) throws SQLException;

}
