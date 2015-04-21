package annotation.revision;

import java.sql.Connection;
import java.sql.DriverManager;

public class UpdateDao {

	
	private Update update;

	public UpdateDao(Update update) {
		this.update = update;
	}

	public void save(Update update) throws Exception {
		
		
	}

	public void saveTo(String path, String user, String pass) throws Exception {
		DriverManager.getConnection(path,user,pass).createStatement().executeUpdate("insert into revision values ('" + update.name + "','" + update.date + "','" + update.comment + "');");
		
	}

	@Override
	public boolean equals(Object obj) {
		if(getClass().equals(obj.getClass())){
			UpdateDao dao = (UpdateDao)obj;
			if(update!=null && dao.update == null)
				return false;
		return update.name.equals(dao.update.name);
		} else { return false;}
	}

	
}
