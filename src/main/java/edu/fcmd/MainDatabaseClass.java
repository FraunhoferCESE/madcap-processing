package edu.fcmd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainDatabaseClass {

	static Logger logger;

	static DatabaseHelper dbHelper;

	public static void main(String[] args) {

		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();

//		databaseMSMS();
		
	}

	private static void databaseMSMS() {		
		MSMSData msmsData = new MSMSData(dbHelper.getConnection(), dbHelper.getStatement());


		try {
			File file = new File("C:\\Users\\PGuruprasad\\Desktop\\pocketsecurity_raw\\json\\MSMSEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			msmsData.createTable();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: {}", ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				msmsData.insertIntoAll(
						innerJobj.get("name").toString(), 
						jObject.get("action").toString(), 
						jObject.get("extra").toString(), 
						jObject.get("timestamp").toString().substring(0, 10),
						jObject.get("userID").toString());
			}

		} catch (FileNotFoundException e) {
			logger.error("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("I/O exception");
			e.printStackTrace();
		} catch (ParseException e) {
			logger.error("Could not parse for JSON object");
			e.printStackTrace();
		} catch (NullPointerException npe){
			logger.error("Null pointer exception");
			npe.printStackTrace();
		} catch (SQLException e) {
			logger.error("SQL command failed");
			e.printStackTrace();
		}
	}
}
