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
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GetRevisionDbTest {

	
	private Connection connection;
	
	private UpdateDao updateDao;

	@Before
	public void createConnection() throws Exception {
		
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");
		updateDao = UpdateDao.createRevisionTable(new UpdateDao(connection));
	}
	
	@After
	public void closeConnection() throws Exception {
		connection.createStatement().executeUpdate("drop table revision;");
		connection.close();
	}

	@Test
	public void readWriteUpdatesToDb() throws Exception {
		this.updateDao.save(asList(new Update("Vass","30.06.2015","Bla Bla")));
		List<Update> actual = this.updateDao.loadUpdates();
		
		Update expected = new Update("Vass      ", "30.06.2015", "Bla Bla                                                                                             ");
		assertEquals(expected, actual.get(0));
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
