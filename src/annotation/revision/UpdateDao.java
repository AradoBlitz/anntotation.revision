package annotation.revision;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateDao {

	private  final List<Update> updateList = new ArrayList<Update>();

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((updateList == null) ? 0 : updateList.hashCode());
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
		
		UpdateDao dao = (UpdateDao)obj;
		if(updateList.size()!=0)
			if( dao.updateList.size() == 0)
				return false;
			
		for(int i=0;i<updateList.size();i++)
			if(!updateList.get(i).equals(dao.updateList.get(i)))
				return false;
		
		return true;
	}

	public static List<Update> loadFrom(String url, String login, String password) throws Exception {
		List<Update> list = new ArrayList<Update>();
		Connection connection = DriverManager.getConnection(url,login,password);
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery("select * from revision");
		try{
			while (result.next()){
				String name = result.getString("author");		
				String date = result.getString("date");		
				String comment = result.getString("comment");
				list.add(new Update(name, date, comment));
			}
			return list;
		}finally{
			result.close();
			stmt.close();
			connection.close();
		}
	}

	
}
