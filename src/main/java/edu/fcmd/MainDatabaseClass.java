package edu.fcmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainDatabaseClass {

	static Logger logger;

	static DatabaseHelper dbHelper;

	static String filePath;

	public static void main(String[] args) {

		logger = Logger.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();

		filePath = "C:\\Users\\PGuruprasad\\Desktop\\pocketsecurity_raw\\Rel 8-2-17\\json\\";

//		databaseFBActivity();
		databaseMSMS();
//		databaseAppInfo();
		databasePhysicalActivity();
		databaseLocationEntry();
		databasePowerInfo();
	}

	private static void databasePowerInfo() {
		PowerInfoData powerInfoData = new PowerInfoData(dbHelper.getConnection());

		//create table
		powerInfoData.createTable();

		//		insert data into the table
		try {
			File file = new File(filePath+"PowerEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				powerInfoData.insertIntoTable(
						innerJobj.get("name").toString(), 
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("userID").toString(),
						Double.parseDouble(jObject.get("health").toString()),
						Double.parseDouble(jObject.get("remainingPower").toString()),
						Double.parseDouble(jObject.get("voltage").toString()),
						Double.parseDouble(jObject.get("temperature").toString()));
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
		}

	}

	private static void databaseLocationEntry() {
		LocationData locationData = new LocationData(dbHelper.getConnection());

		//create table
		locationData.createTable();

		//		insert data into the table
		try {
			File file = new File(filePath+"LocationEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				String origin;
				try{
					origin = jObject.get("origin").toString();
				}catch (NullPointerException npe){
					origin = "UNKNOWN";
				}
				locationData.insertIntoTable(
						innerJobj.get("name").toString(), 
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("userID").toString(),
						Double.parseDouble(jObject.get("accuracy").toString()),
						Double.parseDouble(jObject.get("longitude").toString()),
						Double.parseDouble(jObject.get("latitude").toString()),
						Double.parseDouble(jObject.get("bearing").toString()),
						Double.parseDouble(jObject.get("altitude").toString()),
						origin);
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
		}

	}

	private static void databasePhysicalActivity() {
		PhysicalActivityData activityData = new PhysicalActivityData(dbHelper.getConnection());

		//create table
		activityData.createTable();

		//		insert data into the table
		try {
			File file = new File(filePath+"ActivityEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				activityData.insertIntoTable(
						innerJobj.get("name").toString(),
						Double.parseDouble(jObject.get("walking").toString()), 
						Double.parseDouble(jObject.get("still").toString()), 
						Double.parseDouble(jObject.get("tilting").toString()), 
						Double.parseDouble(jObject.get("inVehicle").toString()), 
						Double.parseDouble(jObject.get("running").toString()), 
						Double.parseDouble(jObject.get("unknown").toString()), 
						Double.parseDouble(jObject.get("onFoot").toString()), 
						Double.parseDouble(jObject.get("onBicycle").toString()),  
						jObject.get("timestamp").toString(),//.substring(0, 10),
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
		}
	}

	private static void databaseAppInfo() {
		AppInfoData appInfo = new AppInfoData(dbHelper.getConnection());

		//		appInfo.createTableIfNot();
		try {
			File file = new File(filePath+"ForegroundBackgroundEventEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);
				if(appInfo.getAppNameAndCategory(jObject.get("packageName").toString())==1)	logger.debug("SKIP");	
				else appInfo.addAppNameAndCategory(jObject.get("packageName").toString());
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
		}

	}

	private static void databaseFBActivity() {
		ForegroundAppData foregroundAppData = new ForegroundAppData(dbHelper.getConnection());


		//create table
		foregroundAppData.createTable();

		//read josn file and insert into database

		try {
			File file = new File(filePath+"ForegroundBackgroundEventEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				String accuracy = jObject.get("accuracy").toString();
				String timestamp = jObject.get("timestamp").toString();

				if(accuracy.equalsIgnoreCase(timestamp)) accuracy = "1";

				foregroundAppData.insertIntoTable(
						innerJobj.get("name").toString(),
						Integer.parseInt(accuracy), 
						jObject.get("packageName").toString(), 
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("userID").toString(),
						jObject.get("eventType").toString());
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
		}

		//index database
		foregroundAppData.indexForegroundApp();
	}

	private static void databaseMSMS() {		
		MSMSData msmsData = new MSMSData(dbHelper.getConnection());

		//create table
		msmsData.createTable();

		// read json and insert into database
		try {
			File file = new File(filePath+"MSMSEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				msmsData.insertIntoTable(
						innerJobj.get("name").toString(), 
						jObject.get("action").toString(), 
						jObject.get("extra").toString(), 
						jObject.get("timestamp").toString(),//.substring(0, 10),
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

		//index database
		msmsData.indexMSMS();
	}
}
