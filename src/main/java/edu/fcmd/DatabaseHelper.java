package edu.fcmd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHelper {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306?useSSL=false";

	//  Database credentials
	static String USER;
	static String PASS;

	static Connection connection = null;
	static Statement statement = null;

	private static DatabaseHelper databaseHelper = new DatabaseHelper();

	static Logger logger;

	public Connection getConnection(){
		return connection;
	}

	public Statement getStatement(){
		return statement;
	}

	public DatabaseHelper() {
		// TODO Auto-generated constructor stub
	}

	public static DatabaseHelper getInstance(){
		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
		//			BasicConfigurator.configure();
		return databaseHelper;
	}

	public void init(String uname, String pass){
		this.USER = uname;
		this.PASS = pass;
	}

	public boolean connect(){
		try {
			//register jdbc driver
			Class.forName(JDBC_DRIVER);

			//connect
			logger.info("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			//execute query
			logger.info("Creating database...");
			statement = connection.createStatement();

			String query = "create database if not exists MADCAP";
			int val = statement.executeUpdate(query);
			logger.info("Database created? {}", val);

			return true;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void disconnect(){
		try{
			if(statement!=null)
				statement.close();
		}catch(SQLException se2){
		}// nothing we can do
		try{
			if(connection!=null)
				//connection.setAutoCommit(true);
				connection.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
}
