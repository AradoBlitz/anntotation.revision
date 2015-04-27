package annotation.revision;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateDao {

	static{
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Connection connection;

	public UpdateDao(Connection connection) throws SQLException{
		this.connection = connection;	
		
		
	}

	public UpdateDao save(List<Update> updateList) throws SQLException {
		
		Statement stmt = connection.createStatement();
		try{
			for(Update update:updateList)		
				stmt.executeUpdate("insert into revision values ('" + update.name + "','" + update.date + "','" + update.comment + "');");
		}finally{
			stmt.close();
		
		}
		return null;
	}

	public List<Update> loadUpdates() throws Exception {
		List<Update> list = new ArrayList<Update>();
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
		}
	}

	public static UpdateDao createRevisionTable(UpdateDao updateDao) throws SQLException {
		updateDao.connection.createStatement().executeUpdate("create table revision (author CHAR(10),date CHAR(10),comment CHAR(100));");
		return updateDao;
	}

	public static void close(UpdateDao updateDao) throws SQLException {
		 updateDao.connection.close();
		
	}
	
	
}
