package annotation.revision;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GetRevisionDbTest {

	
	private Connection connection;

	@Ignore
	@Test
	public void putRevisionDataFromClassToDb() throws Exception {
		
		
	}
	
	@Before
	public void createConnection() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");
		connection.createStatement().executeUpdate("create table revision (author CHAR(10),date CHAR(10),comment CHAR(100));");
	}
	
	@After
	public void closeConnection() throws Exception {
		connection.createStatement().executeUpdate("drop table revision;");
		connection.close();
	}

	@Test
	public void addToDataBaseRevisionDataFromClass() throws Exception {
		connection.createStatement().executeUpdate("insert into revision values ('Vass','30.06.2015','Bla Bla');");
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("select * from revision");
		assertTrue(result.next());
	}
	
	@Test
	public void createEmbeddedDataBase() throws Exception {
		
		assertFalse("Connection should be openned.",connection.isClosed());
	}
	
	@Test
	public void testEnvironmentsSchema() throws Exception {
		
		assertTrue(isTableExists("REVISION"));
	}

	private boolean isTableExists(String tableNamePattern) throws SQLException {
		String catalog = null;
		String schemaPattern = null;
		String[] types = {"TABLE"};
		boolean next = connection.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types ).next();
		return next;
	}
}
