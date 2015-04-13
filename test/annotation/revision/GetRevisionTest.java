package annotation.revision;

import static org.junit.Assert.*;

import org.junit.Test;


public class GetRevisionTest {

	
	@Test
	public void getDataFromAnnotation() throws Exception {
		assertNotNull(Updated.class.getAnnotation(Revision.class)); 
		Update updateFromClass = new Update(Updated.class); 
		assertEquals("Vass",updateFromClass.name);
		assertEquals("30.10.2015", updateFromClass.date());
		assertEquals("Some comment about update", Updated.class.getAnnotation(Revision.class).comment());
		
	}
	
	
}
