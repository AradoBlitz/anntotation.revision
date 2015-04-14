package annotation.revision;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	}
	
	@Test
	public void createEmbeddedDataBase() throws Exception {
		
		assertFalse("Connection should be openned.",connection.isClosed());
	}
	
	@Test
	public void testEnvironmentsSchema() throws Exception {
		assertEquals(0,connection.createStatement().executeUpdate("create table revision (author CHAR,date CHAR,comment CHAR);"));
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
