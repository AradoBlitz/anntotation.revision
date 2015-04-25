package annotation.revision;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GetRevisionDbTest {

	
	private Connection connection;
	

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
		UpdateDao updateDao = new UpdateDao(asList(new Update("Vass","30.06.2015","Bla Bla")));
		
		updateDao.saveTo("jdbc:hsqldb:mem:test","sa","");
		
		ResultSet result = connection.createStatement().executeQuery("select * from revision");
		assertTrue(result.next());
	}
	
	@Test
	public void UpdateDaoequality() throws Exception {
		assertEquals(new UpdateDao(asList(new Update("Vass","30.06.2015","Bla Bla"))), new UpdateDao(asList(new Update("Vass","30.06.2015","Bla Bla"))));
		try{
		assertFalse(
				new UpdateDao(asList(new Update("Peter","27.09.2015","Bla Bla")))
					.equals(new UpdateDao(new ArrayList<Update>())));
		}catch (NullPointerException e){
			e.printStackTrace();
			fail();
		}
		assertFalse(
				new UpdateDao(asList(new Update("Peter","27.09.2015","Bla Bla")))
					.equals(new UpdateDao(asList(new Update("Vass","30.06.2015","Bla Bla")))));
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
