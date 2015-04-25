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
			
		ZipFile zipFile = new ZipFile("./etc/Untitled.jar");
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

	public Class toClass(byte[] copyOf) {
		
		return defineClass("annotation.revision.UpdateDao", copyOf, 0, copyOf.length);
	}
	
	public Class toClass(String name, byte[] copyOf) {
		
		return defineClass(name, copyOf, 0, copyOf.length);
	}

	public UpdateDao readPackage(String string) throws Exception {
		
		List<Class> extractClasses = extractClasses(string);
		System.out.println(extractClasses.get(4).getName());
		System.out.println(extractClasses.get(4).getAnnotation(Revision.class));
		return new UpdateDao(Update.extract(extractClasses.get(4)));
	}

	private Collection<? extends Class> extractZipClasses(String string) throws Exception {
		ZipInputStream input = new ZipInputStream(new FileInputStream(string));
		List<Class> classList = new ArrayList<Class>();
		
		ZipEntry entry = null;
		while((entry = input.getNextEntry())!=null){
			byte[] buff = new byte[1024];
			int read = 0;
			if(entry.getName().contains(".class")) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
				while(read!=-1){
					input.read(buff);
					out.write(buff);
				}
				
				classList.add(toClass(out.toByteArray()));
				out.close();
			}
			input.closeEntry();
		}
		return classList;
	}
}

