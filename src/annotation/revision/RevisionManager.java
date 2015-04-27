package annotation.revision;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class RevisionManager {

	static class UpdateTable{

		private JTable table;
		
		public UpdateTable(){
			table = new JTable();
		}
		
		public void refresh(List<Update> updates) {
			DefaultTableModel dataModel = new DefaultTableModel(
					new Object[][]{}
					,new Object[]{"#","Author","Date","Comment"});
			int i=1;
			for(Update update: updates)
				dataModel.addRow(new Object[]{i++,update.name,update.date,update.comment});
			table.setModel(dataModel);
			
		}

		public JScrollPane getTablePanel() {
		
			return  new JScrollPane(table);
		}
		
	}
	
	public static void main(String[] args) {
		JFrame mainWindow = new JFrame("Revision Manager.");
		UpdateTable updateTable = new UpdateTable();
		try {
					
			UpdateDao updateDao = UpdateDao
					.createRevisionTable(
							new UpdateDao(DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","")));
		/*	addStubData(updateDao,updateTable,new Update("Alex","11.02.2015","Did somthing")
			,new Update("Peter","18.07.2015","Fixed somthing"));
			*/
			
		
			mainWindow
			.getContentPane()
			.add(createExtractUpdatesFromJarPanel(updateTable,
					updateDao),BorderLayout.NORTH);
		
			
			mainWindow
				.getContentPane()
				.add(updateTable.getTablePanel()
							,BorderLayout.CENTER);
			
			mainWindow.setSize(300, 300);
			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWindow.setVisible(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addStubData(UpdateDao updateDao, UpdateTable updateTable,Update... updates) throws Exception {
		updateDao.save(Arrays.asList(new Update("Alex","11.02.2015","Did somthing")
		,new Update("Peter","18.07.2015","Fixed somthing")));			
		updateTable.refresh(updateDao.loadUpdates());
		
	}

	private static JPanel createExtractUpdatesFromJarPanel(
			UpdateTable updateTable, UpdateDao updateDao) {
		JPanel addJarPanel = new JPanel();
		final UpdateDao dao = updateDao;
		ActionListener addJarButtoListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(new File("./"));
				if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(addJarPanel))
					try {
						
						dao.save(Update.convertToList(new JarArchive(fileChooser.getSelectedFile().getAbsolutePath())));
						updateTable.refresh(dao.loadUpdates());
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			}
		};
		
		
		JButton addJarSubmit = new JButton("Add Jar");
		addJarPanel.add(addJarSubmit);	
		addJarSubmit.addActionListener(addJarButtoListener);
		return addJarPanel;
	}

}
