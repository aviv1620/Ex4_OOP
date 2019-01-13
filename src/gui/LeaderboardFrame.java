package gui;


import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * go to database and shoe the result with another student in the class.
 * @author Aviv Vexler
 *
 */
public class LeaderboardFrame extends JFrame {
	/** version 1 */
	private static final long serialVersionUID = 1L;	

	/**
	 * request Leaderboard from database.
	 * convert to JTable.
	 * and show on GUI.
	 * @param gameID ID for file name.
	 * @throws HeadlessException
	 */
	public LeaderboardFrame(double gameID) throws HeadlessException {		
		//table
		String[] columnNames = {"FirstID","SecondID","ThirdID","Point"};

		String[][] data = table(gameID);

		//table
		JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		setLayout(new BorderLayout());
		add(table.getTableHeader(), BorderLayout.PAGE_START);
		add(table, BorderLayout.CENTER);

		TableColumn column = null;
		for (int i = 0; i < columnNames.length; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(50);
		}

		//GUI
		setSize(600,300); 
		setVisible(true);
		add(scrollPane);

		//fix java bug(naive solution).
		Runnable runnable = ( () -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		} );
		new Thread(runnable).start();
	}

	/**
	 * request Leaderboard and convert to String 2d array.
	 * @param gameID - ID for file name.
	 * @return the leaderboard.
	 */
	private String[][] table(double gameID) {
		String[][] ans = new String[8][4];

		String jdbcUrl="jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		String jdbcUser="student";
		String jdbcPassword="student";
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);


			Statement statement = connection.createStatement();

			//select data
			String allCustomersQuery = "SELECT FirstID,SecondID,ThirdID,Point FROM logs WHERE SomeDouble="+gameID+" ORDER BY point DESC LIMIT 8;"; 
			//String allCustomersQuery = "SELECT * FROM logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int i = 0;
			while(resultSet.next())
			{
				ans[i][0] = ""+resultSet.getInt("FirstID");
				ans[i][1] = ""+resultSet.getInt("SecondID");
				ans[i][2] = ""+resultSet.getInt("ThirdID");
				ans[i][3] = ""+resultSet.getInt("Point");
				i++;
			}

			resultSet.close();		
			statement.close();		
			connection.close();	
			return ans;
		}

		catch (SQLException sqle) {
			ans[0][0] = "SQLException: " + sqle.getMessage() + "<br>";
			ans[0][1] = "Vendor Error: " + sqle.getErrorCode() + "<br>";
			return ans;
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			ans[0][0] = e.getMessage();
			return ans;
		}
	}

	//test just table
	/*public static void main(String[] args) {
		new LeaderboardFrame(1.193961129E9);/a-v-i-v-v-e-x-l-e-r
	}*/

}
