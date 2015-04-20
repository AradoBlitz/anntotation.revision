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
		byte[] buffer = new byte[1024*2];
		InputStream input = new FileInputStream("/home/dmitriy/Projects/Java/portfolio/annotation.revision/bin/annotation/revision/UpdateDao.class");
		int read = input.read(buffer);
		assertNotNull(new JarArchive().toClass(Arrays.copyOf(buffer , read)));
	}
	
	@Test
	public void getClassObject() throws Exception {
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
		Class class1 = new JarArchive().toClass(zipFile,zipFile.getEntry(revisionedTestClass));
		assertNotNull(class1);
		assertEquals("annotation.revision.UnpackArchiveTest$Revisioned", class1.getName());
	}
	
	@Test
	public void getClassListFromArchive() throws Exception {
		
		assertEquals(9, new JarArchive().extractClasses("./etc/Untitled.jar").size());
		assertEquals(new UpdateDao(new Update("Vass", "30.10.2015", "Some comment about about.")), new JarArchive().readPackage("./etc/Untitled.jar"));
	}
}
