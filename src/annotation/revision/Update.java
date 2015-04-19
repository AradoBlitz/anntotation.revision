package annotation.revision;

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

	public static Update extract(Class updated) {
		if(updated.getAnnotation(Revision.class)!=null)
			return new Update(updated);
		return null;
	}

}
