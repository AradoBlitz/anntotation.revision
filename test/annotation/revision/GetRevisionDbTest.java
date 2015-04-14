package annotation.revision;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Ignore;
import org.junit.Test;

public class GetRevisionDbTest {

	
	@Ignore
	@Test
	public void putRevisionDataFromClassToDb() throws Exception {
		
		
	}
	
	@Test
	public void createEmbeddedDataBase() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");
		assertFalse("Connection should be openned.",connection.isClosed());
	}
}
