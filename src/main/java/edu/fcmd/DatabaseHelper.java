package edu.fcmd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DatabaseHelper {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false";

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

//	public Statement getStatement(){
//		return statement;
//	}

	private DatabaseHelper() {
		// TODO Auto-generated constructor stub
	}

	public static DatabaseHelper getInstance(){
		logger = Logger.getLogger(MainDatabaseClass.class);
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

			DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
			dslContext.createSchemaIfNotExists("madcap").execute();
						
			//use created database
			statement = connection.createStatement();
			String query = "use MADCAP";
			statement.executeUpdate(query);

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

//	public void createSchemaIfNotExists(String schemaName) {
//		
//		logger.info("Creating schema");
//		int value = dslContext.createSchemaIfNotExists(schemaName).execute();
//		logger.info("Schema created?: " +value );
//	}
}
