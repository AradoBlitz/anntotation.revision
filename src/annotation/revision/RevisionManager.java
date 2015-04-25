package annotation.revision;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RevisionManager {

	public static void main(String[] args) {
		JFrame mainWindow = new JFrame("Revision Manager.");
		DefaultTableModel dataModel = new DefaultTableModel(
				new Object[][]{}
				,new Object[]{"#","Author","Date","Comment"});
		
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");
			connection.createStatement().executeUpdate("create table revision (author CHAR(10),date CHAR(10),comment CHAR(100));");
			
			new UpdateDao(Arrays.asList(new Update("Alex","11.02.2015","Did somthing")
			,new Update("Peter","18.07.2015","Fixed somthing")))
				.saveTo("jdbc:hsqldb:mem:test","sa","");
			List<Update> updates = UpdateDao.loadFrom("jdbc:hsqldb:mem:test","sa","");
			int i=1;
			for(Update update: updates)
				dataModel.addRow(new Object[]{i++,update.name,update.date,update.comment});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		mainWindow
			.getContentPane()
			.add(
					new JScrollPane(
							new JTable(
									dataModel))
					,BorderLayout.NORTH);
		
		mainWindow.setSize(300, 300);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}

}
