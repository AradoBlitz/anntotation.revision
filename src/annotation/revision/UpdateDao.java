package annotation.revision;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateDao {

	private  final List<Update> updateList = new ArrayList<Update>();
	private Update update;

	public UpdateDao(Update update) {
		this.update = update;
		updateList.add(update);
	}

	public UpdateDao(List<Update> updateList){
		this.updateList.addAll(updateList);
	}
	
	public void saveTo(String path, String user, String pass) throws Exception {
		Connection connection = DriverManager.getConnection(path,user,pass);
		Statement stmt = connection.createStatement();
		try{
			for(Update update:updateList)		
				stmt.executeUpdate("insert into revision values ('" + update.name + "','" + update.date + "','" + update.comment + "');");
		}finally{
			stmt.close();
			connection.close();
		}
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
