package annotation.revision;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

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
		assertEquals(asList(new Update("Vass", "30.10.2015", "Some comment about update"))
				, Update.convertToList(asList(Updated.class)));
	}
	
	@Test
	public void updateInstanceCreation(){
		assertTrue(Update.convertToList(asList(NoAnotated.class)).isEmpty());
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
	
	public static class DoubleMethodUpdated{
		@Revision(comment = "Bla Bla method foo", date = "07.12.2015", name = "Updater foo")
		public void foo(){}
		
		public void doo(){}
		
		@Revision(comment = "Bla Bla method goo", date = "29.06.2015", name = "Updater goo")
		public void goo(){}
	}
	
	@Test
	public void extractedAnnotatedMethods() throws Exception {
		
		assertEquals(
				asList(new Update("Updater", "06.29.2015", "Bla Bla method"))
				, Update.convertToList(asList(UpdatedMethod.class)));
		assertTrue("When no annotated method NULL returns."
				, Update.convertToList(asList(NoUpdatedMethod.class)).isEmpty());
		List<Update> convertToList = Update.convertToList(asList(DoubleMethodUpdated.class));
		//TODO Order is unpredictable, but very often its backwards against the order of annotated methods in class.
		assertEquals(new Update("Updater foo", "07.12.2015", "Bla Bla method foo"),convertToList.get(1));
		assertEquals(new Update("Updater goo", "29.06.2015", "Bla Bla method goo"),convertToList.get(0));
		
	}
	
}
