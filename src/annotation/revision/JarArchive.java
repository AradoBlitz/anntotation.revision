package annotation.revision;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class JarArchive extends ClassLoader{
	
	private String pathToJar;

	public JarArchive() {
		// TODO Auto-generated constructor stub
	}
	
	public JarArchive(String pathToJar) {
		this.pathToJar = pathToJar;
		// TODO Auto-generated constructor stub
	}

	public Class toClass(ZipFile zipFile, ZipEntry entry) throws Exception{
		return toClass(entry.getName().replaceAll("/", ".").replaceAll(".class", ""), zipFile.getInputStream(entry));
	}

	private Class toClass(String className, InputStream entriesInput)
			throws IOException, ClassFormatError {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		try{
		byte[] buffer = new byte[1024];
		int read = 0;
		int count  = 0;
		while((read = entriesInput.read(buffer))!=-1){
			byteBuffer.write(buffer, 0, read);
			count+=read;
		}
		
		return defineClass(className, byteBuffer.toByteArray() , 0, count);
		} finally{
			byteBuffer.reset();
		}
	}

	public List<Class> extractClasses(String archiveName) throws Exception{
			
		ZipFile zipFile = new ZipFile(archiveName);
		ArrayList<Class> classList = new ArrayList<Class>();
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		for(;entries.hasMoreElements();){
			ZipEntry extractedByGetNameMethod = entries.nextElement();		
			if(extractedByGetNameMethod.getName().contains(".class")){
			Class updated = new JarArchive().toClass(zipFile, extractedByGetNameMethod);
			classList.add(updated);
		}}
		return classList;
	}

		
	public Class defineClass(String name, byte[] copyOf) {		
		return defineClass(name, copyOf, 0, copyOf.length);
	}

	public UpdateDao readPackage(String pathToJar) throws Exception {
		
		return new UpdateDao(Update.convertToList(new JarArchive(pathToJar)));
	}

	public List<Class> extractClasses() throws Exception {
		// TODO Auto-generated method stub
		return extractClasses(pathToJar);
	}

	
}

