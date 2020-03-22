package me.atticuszambrana.atticus.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.atticuszambrana.atticus.util.LogUtil;

public class Database {
	
	public static Database me;
	public static Database get() {
		if(me == null) {
			me = new Database();
		}
		return me;
	}
	
	/*
	 * Database Object
	 * used to access data storage in MySQL
	 * Author: Atticus Zambrana
	 */
	
	private String username = "[removed for source release]";
	private String database = "[removed for source release]";
	private String password = "[removed for source release]";
	private String host = "[removed for source release]";
	
	private Connection connection;
	
	public Database() {
		// When the object gets created, lets connect!
		LogUtil.info("Database", "Connecting to SQL Server...");
		try {
			if(connection != null && !connection.isClosed()) {
				return;
			}
			
			synchronized(this) {
				if(connection != null && !connection.isClosed()) {
					return;
				}
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + 3306 + "/" + this.database, this.username, this.password);
			}
		} catch(SQLException | ClassNotFoundException ex) {
			//LogUtil.info("Database", "Unable to connect to the Database.");
			//LogUtil.info("Database", "Printing Stack Trace of Error: ");
			//ex.printStackTrace();
			LogUtil.info("Database", "Unable to connect to the database: " + ex.getMessage());
			System.exit(1);
		}
		
		LogUtil.info("Database", "Success!");
		
		// The fix
		applyFix();
	}
	
	// A fix for that bug mentioned above
	private void applyFix() {
		Thread t = new Thread() {
			public void run() {
				Thread.currentThread().setName("Database Keep Alive Thread");
				try {
					ResultSet result = connection.createStatement().executeQuery("SELECT * FROM `AtticusIsReallyCool`");
				} catch (SQLException e) {
				}
				
				try {
					// 5 Minutes?
					Thread.sleep((1000 * 60) * 5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				applyFix();
			}
		};
		t.start();
	}
	
	public Connection getConnection() {
		return connection;
	}
}
