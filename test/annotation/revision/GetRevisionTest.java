package annotation.revision;

import static org.junit.Assert.*;

import org.junit.Test;


public class GetRevisionTest {

	@Revision(name = "Vass",date="30.10.2015",comment="Some comment about update")
	static class Updated {}
	
	@Test
	public void getDataFromAnnotation() throws Exception {
		assertNotNull(Updated.class.getAnnotation(Revision.class)); 
		Update updateFromClass = new Update(Updated.class); 
		assertEquals("Vass",updateFromClass.name);
		assertEquals("30.10.2015", updateFromClass.date);
		assertEquals("Some comment about update",updateFromClass.comment);
		
		assertNull("No update object should be returned for NoAnnotated class",Update.extract(NoAnotated.class));
		assertNotNull("Update object should be returned for Annotated class.",Update.extract(Updated.class));
		
	}
	
	static class NoAnotated{}
	
	@Test
	public void noAnnotated(){
		Class noAnnotatedClass = NoAnotated.class;
		assertNull("getAnnotation should return null.",noAnnotatedClass.getAnnotation(Revision.class));
	}
	
}
