package annotation.revision;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.InputSource;

public class UnpackArchiveTest {

	
	private static final String revisionedTestClass = "annotation/revision/UnpackArchiveTest$Revisioned.class";

	@Test
	public void fromStreamToClass() throws Exception {
		ZipFile jar = new ZipFile("./etc/Untitled.jar");
		Enumeration<? extends ZipEntry> entries = jar.entries();
		assertNotNull(entries);
		assertEquals("META-INF/MANIFEST.MF",entries.nextElement().getName());
		assertEquals("annotation/revision/UpdateDao.class",entries.nextElement().getName());
		assertNotNull(jar.getEntry("annotation/revision/UnpackArchiveTest.class"));
		assertNotNull(jar.getEntry(revisionedTestClass));
	
	}
	
		
	@Test
	public void truncatedClassExample() throws Exception {
		byte[] buffer = new byte[1024*6];
		InputStream input = new FileInputStream("/home/dmitriy/Projects/Java/portfolio/annotation.revision/bin/annotation/revision/UpdateDao.class");
		int read = input.read(buffer);
		assertNotNull(new JarArchive().defineClass("annotation.revision.UpdateDao",Arrays.copyOf(buffer , read)));
	}
	
	@Test
	public void getClassObject() throws Exception {
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
		Class class1 = new JarArchive().toClass(zipFile,zipFile.getEntry(revisionedTestClass));
		assertNotNull(class1);
		assertEquals("annotation.revision.UnpackArchiveTest$Revisioned", class1.getName());
		class1 = new JarArchive().toClass(zipFile,zipFile.getEntry("annotation/revision/GetRevisionTest$Updated.class"));
		assertNotNull(class1);
		assertNotNull(Update.extract(class1));
	}
	
	@Test
	public void getClassListFromArchive() throws Exception {
		
		List<Class> classList = new JarArchive().extractClasses("./etc/Untitled.jar");
		assertEquals(9, classList.size());
		assertTrue("annotation/revision/GetRevisionTest$Updated.class".contains(".class"));
		assertNotNull(classList.get(4).getAnnotation(Revision.class));
		assertArrayEquals(new Update[]{new Update("Vass", "30.10.2015", "Some comment about update")}
				, Update.convertToList(new JarArchive("./etc/Untitled.jar")).toArray());
	}
	
	@Test
	public void checkIsZipEntryChangedAfterGetingItsName() throws Exception {
		
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
		ZipEntry extractedByName = zipFile.getEntry("annotation/revision/GetRevisionTest$Updated.class");
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		entries.nextElement();entries.nextElement();entries.nextElement();
		entries.nextElement();entries.nextElement();
		ZipEntry extractedByGetNameMethod = entries.nextElement();
		assertEquals(extractedByName.getSize() ,extractedByGetNameMethod.getSize());
		entries.hasMoreElements();
		assertTrue(extractedByGetNameMethod.getName().contains(".class"));
		assertNotNull(new JarArchive().toClass(zipFile, extractedByGetNameMethod).getAnnotation(Revision.class));
		
	}
	
	@Test
	public void revisionIsNotLoadedWhenToClassMethodReused() throws Exception {
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
		final int updatedInJar = 4;
		ArrayList<Class> classList = new ArrayList<Class>();
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		JarArchive jarArchive = new JarArchive();
		for(;entries.hasMoreElements();){
			ZipEntry extractedByGetNameMethod = entries.nextElement();		
			if(extractedByGetNameMethod.getName().contains(".class")){		
			classList.add(jarArchive.toClass(zipFile, extractedByGetNameMethod));
		}}		
		assertNull(classList.get(updatedInJar).getAnnotation(Revision.class));
		
	}
}
