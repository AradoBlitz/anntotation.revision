package annotation.revision;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Update {

	public final String name;
	
	public final String date;
	
	public final String comment;

	private Update(Class updated) {
		Revision revision = (Revision) updated.getAnnotation(Revision.class);
		name =  revision.name();
		date =   revision.date();
		comment = revision.comment();
	}

	public Update(String name, String date, String comment) {
		this.name = name; 
		this.date = date;
		this.comment = comment;
	}

	public Update(Method method) {
		Revision revision = (Revision) method.getAnnotation(Revision.class);
		name =  revision.name();
		date =   revision.date();
		comment = revision.comment();
	}

	public static Update extract(Class updated) {
		if(updated.getAnnotation(Revision.class)!=null)
			return new Update(updated);
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Update other = (Update) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static List<Update> convertToList(List<Class> extractClasses) {
		List<Update> updateList = new ArrayList<>();
		for (Class extractedClasses : extractClasses){		
			Update extract = Update.extract(extractedClasses);
			if(extract!=null)
				updateList.add(extract);
			updateList.addAll(Update.findUpdatesInMethods(extractedClasses.getMethods()));
			
		}
		return updateList;
	}

	private static Collection<? extends Update> findUpdatesInMethods(
			Method[] methods) {
		List<Update> updateList = new ArrayList<Update>();
		for(Method method:methods)
			if(method.getAnnotation(Revision.class)!=null)
				updateList.add(new Update(method));
		return updateList;
	}

	public static List<Update> convertToList(JarArchive jarArchive) throws Exception {
		return convertToList(jarArchive.extractClasses());
	}

	@Override
	public String toString() {
		return "Update [name=" + name + ", date=" + date + ", comment="
				+ comment + "]";
	}

	
}
