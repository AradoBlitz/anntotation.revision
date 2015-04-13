package annotation.revision;

import static org.junit.Assert.*;

import org.junit.Test;


public class GetRevisionTest {

	
	@Test
	public void getDataFromAnnotation() throws Exception {
		assertNotNull(Updated.class.getAnnotation(Revision.class)); 
		assertEquals("Vass",Updated.class.getAnnotation(Revision.class).name());
	}
}
