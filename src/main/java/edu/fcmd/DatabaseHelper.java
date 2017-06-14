package edu.fcmd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.BasicConfigurator;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHelper {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/madcap?useSSL=false";

	//  Database credentials
	static String USER;
	static String PASS;

	static Connection connection = null;
	static Statement statement = null;

	private static DatabaseHelper databaseHelper = new DatabaseHelper();

	static Logger logger;

	public Connection getConnection(){
		if(connection == null) connect();
		return connection;
	}

	public Statement getStatement(){
		return statement;
	}

	private DatabaseHelper() {
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

	public void connect(){
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

		} catch (ClassNotFoundException e) {
			logger.error("Database connection failed");
			e.printStackTrace();
			connection = null;
		} catch (SQLException e) {
			logger.error("Database connection failed");
			e.printStackTrace();
			connection = null;
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

	public void createSchemaIfNotExists(String schemaName) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		logger.info("Creating schema");
		int value = dslContext.createSchemaIfNotExists(schemaName).execute();
		logger.info("Schema created?: " +value );
	}
}
