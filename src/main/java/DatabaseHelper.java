import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306?useSSL=false";

	//  Database credentials
	static String USER;
	static String PASS;

	static Connection connection = null;
	static Statement statement = null;

	private static DatabaseHelper databaseHelper = new DatabaseHelper();



	public DatabaseHelper() {
		// TODO Auto-generated constructor stub
	}

	public static DatabaseHelper getInstance(){
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
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);

			//execute query
			System.out.println("Creating database...");
			statement = connection.createStatement();

			String query = "create database if not exists MADCAP";
			int val = statement.executeUpdate(query);
			System.out.println("Database created: " +val);

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
				connection.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}

	//	public boolean table(String tableName){
	//		
	//		return true;
	//	}	

	public boolean insertInto(String tableName, String nameid, String action, String extra, String timestamp, String userID){
		String query;
		int result;

		if(connection==null){
			System.out.println("Database connection error");
			return false;
		}

		//		query = "create table if not exists "+tableName;
		//		try {
		//			statement.execute(query);
		//		} catch (SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//			return false;
		//		}
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		query = "CREATE TABLE IF NOT EXISTS madcap.msmsentry ("+
				"nameid VARCHAR(100) NOT NULL,"+
				"action VARCHAR(100) NOT NULL,"+
				"extra VARCHAR(100),"+
				"time_stamp DATE,"+
				"userID VARCHAR(25) NOT NULL,"+
				"PRIMARY KEY (nameid));";
		try {
			result = statement.executeUpdate(query);
			System.out.println("Table created: "+result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Table creation failed");
			e.printStackTrace();
			return false;
		}

		query = "insert into madcap.msmsentry values (\"" + nameid +"\",\""
				+ action + "\",\""
				+ extra + "\","
				+ "from_unixtime("+timestamp +"),\""
				+ userID +"\");";
		try {
			System.out.println("query: \n" +query);
			result = statement.executeUpdate(query);
			System.out.println("values inserted: " +result);
			return result==1?true:false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Data insert failed");
			e.printStackTrace();
			return false;
		}

	}
}
