package annotation.revision;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;
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
	
	static class MyClassLoader extends ClassLoader{
	
		public Class toClass(ZipFile zipFile, ZipEntry entry) throws Exception{
			InputStream entriesInput =  zipFile.getInputStream(entry);
			byte[] buffer = new byte[1024];
			int read = entriesInput.read(buffer);
			byte[] b = Arrays.copyOf(buffer, read);
			return defineClass(entry.getName().replaceAll("/", ".").replaceAll(".class", ""), b , 0, read);
		}
	}
	
	@Test
	public void getClassObject() throws Exception {
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
		Class class1 = new MyClassLoader().toClass(zipFile,zipFile.getEntry(revisionedTestClass));
		assertNotNull(class1);
		assertEquals("annotation.revision.UnpackArchiveTest$Revisioned", class1.getName());
	}
}
