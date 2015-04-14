package annotation.revision;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Ignore;
import org.junit.Test;

public class GetRevisionDbTest {

	
	@Ignore
	@Test
	public void putRevisionDataFromClassToDb() throws Exception {
		
		
	}
	
	@Test
	public void createEmbeddedDataBase() throws Exception {
		Connection connection = null;
		assertFalse("Connection should be openned.",connection.isClosed());
	}
}
