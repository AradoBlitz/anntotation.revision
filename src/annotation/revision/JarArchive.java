package annotation.revision;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarArchive extends ClassLoader{
	
	public Class toClass(ZipFile zipFile, ZipEntry entry) throws Exception{
		InputStream entriesInput =  zipFile.getInputStream(entry);
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int read = 0;
		int count  = 0;
		while((read = entriesInput.read(buffer))!=-1){
			byteBuffer.write(buffer, 0, read);
			count+=read;
		}

		return defineClass(entry.getName().replaceAll("/", ".").replaceAll(".class", ""), byteBuffer.toByteArray() , 0, count);
	}

	public List<Class> extractClasses(String archiveName) throws Exception{
		ZipFile zipFile = new ZipFile(archiveName);
		ArrayList<Class> classList = new ArrayList<>();
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		for(;entries.hasMoreElements();){
			ZipEntry entry = entries.nextElement();
			if(!entry.getName().contains(".class"))
				continue;
			Class class1 = toClass(zipFile,entry);			
		classList.add(class1);
		}
		return classList;
	}

	public Class toClass(byte[] copyOf) {
		
		return defineClass("annotation.revision.UpdateDao", copyOf, 0, copyOf.length);
	}

	public UpdateDao readPackage(String string) {
		// TODO Auto-generated method stub
		return new UpdateDao(null);
	}
}

