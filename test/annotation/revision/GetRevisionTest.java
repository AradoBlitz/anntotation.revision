package annotation.revision;

import static org.junit.Assert.*;

import org.junit.Test;


public class GetRevisionTest {

	@Revision(name = "Vass",date="30.10.2015",comment="Some comment about update")
	static class Updated {}
	
	static class NoAnotated{}
	
	@Test
	public void getRevisionAnnotationFromUpdatedClass(){
		assertNotNull(Updated.class.getAnnotation(Revision.class));
	}
	
	@Test
	public void getDataFromAnnotation() throws Exception {
		
		Update updateFromClass = Update.extract(Updated.class); 
		assertEquals("Vass",updateFromClass.name);
		assertEquals("30.10.2015", updateFromClass.date);
		assertEquals("Some comment about update",updateFromClass.comment);		
	}
	
	@Test
	public void updateInstanceCreation(){
		assertNull("No update object should be returned for NoAnnotated class",Update.extract(NoAnotated.class));
		assertNotNull("Update object should be returned for Annotated class.",Update.extract(Updated.class));		
	}
	
	@Test
	public void noAnnotated(){
		final Class noAnnotatedClass = NoAnotated.class;
		assertNull("getAnnotation should return null.",noAnnotatedClass.getAnnotation(Revision.class));
	}
	
	public static class UpdatedMethod{
		@Revision(comment = "Bla Bla method", date = "06.29.2015", name = "Updater")
		public void foo(){}
	}
	
	public static class NoUpdatedMethod{
		public void foo(){};
	}
	@Test
	public void extractedAnnotatedMethods() throws Exception {
		
		assertEquals(new Update("Updater", "06.29.2015", "Bla Bla method"), Update.extract(UpdatedMethod.class));
		try{
		assertNull("When no annotated method NULL returns.", Update.extract(NoUpdatedMethod.class));
		} catch (Exception e){
			fail();
		}
	}
	
}
