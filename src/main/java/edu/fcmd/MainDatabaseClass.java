package edu.fcmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jooq.tools.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.fcmd.radioinfo.AirplaneModeEntry;
import edu.fcmd.radioinfo.BluetoothConnectionEntry;
import edu.fcmd.radioinfo.BluetoothDiscoveryEntry;
import edu.fcmd.radioinfo.BluetoothScanModeEntry;
import edu.fcmd.radioinfo.BluetoothStateEntry;
import edu.fcmd.radioinfo.BluetoothStaticAttributesEntry;
import edu.fcmd.radioinfo.CallStateEntry;
import edu.fcmd.radioinfo.CellEntry;
import edu.fcmd.radioinfo.CellLocationEntry;
import edu.fcmd.radioinfo.NFCEntry;
import edu.fcmd.radioinfo.NetworkEntry;
import edu.fcmd.systeminfo.PowerInfoEntry;

public class MainDatabaseClass {

	static Logger logger;

	static DatabaseHelper dbHelper;

	public static void main(String[] args) {

		logger = Logger.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init(Constants.dbUser,Constants.dbPass);
		dbHelper.connect();

		//		databaseFBActivity();
		//		databaseMSMS(); 
		//		databaseAppInfo();
		//		databasePhysicalActivity();
		//		databaseLocationEntry();
		//		databasePowerInfo();
		//		databaseAirplaneMode();
		//		databaseBTConn();
		//		databaseBTDiscovery();
		//		databaseBTScan();
		//		databaseBTState();
		//		databaseBTStatic();
		//		databaseCallState();
		//		databaseCellEntry();
		//		databaseCellLocation();
		//		databaseNetwork();
		//		databaseNFC();

//				databasePostalAddress();
		databaseCensusInfo();
	}

	private static void databaseCensusInfo() {
		CensusData censusData = new CensusData(dbHelper.getConnection());
		censusData.createTableIfNot();
		
//		double lat = 38.9899434;
//		double lng = -76.93166;
//		censusData.addCensusInfo(lat, lng);
		
		int count = 0;
		try{
			File csvFile = new File("C:\\Users\\PGuruprasad\\Desktop\\pocketsecurity_raw\\31.08-07.09\\location.csv");
			BufferedReader b = new BufferedReader(new FileReader(csvFile));
			String readline = "";

			while ((readline = b.readLine()) != null) {
				String coord[] = readline.split(",");
				logger.info("count: "+ ++count);

				double lat = Double.parseDouble(coord[0]);
				double lng = Double.parseDouble(coord[1]);
								
//				if(censusData.getCensusInfo(lat,lng) == 1) System.out.println("Census skip");
//				else 
					censusData.addCensusInfo(lat, lng);		
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException ioe){
			ioe.printStackTrace();
		}
				
	}

	private static void databasePostalAddress() {
		PostalAddressTable postalAddressTable = new PostalAddressTable(dbHelper.getConnection());

		postalAddressTable.createTableIfNot();

		//		insert data into the table
		//		try {
		//			File file = new File(Constants.filePath"location1356.json");
		//			BufferedReader b = new BufferedReader(new FileReader(file));
		//			String readline = "";
		//			int count = 0;
		//
		//			JSONParser jParser = new JSONParser();
		//			while((readline = b.readLine()) != null){
		//				JSONObject jObject = (JSONObject) jParser.parse(readline);
		//
		//				logger.info("count: "+ ++count);
		//				//				System.out.println("Json: \n"+ jObject);
		//				double lat = Double.parseDouble(jObject.get("latitude").toString());
		//				double lng = Double.parseDouble(jObject.get("longitude").toString());
		//				if(1 == postalAddressTable.getPostalAddress(lat,lng)) System.out.println("Address skip");
		//				else postalAddressTable.addPostalAddress(lat, lng);			
		//			}
		//
		//		} catch (FileNotFoundException e) {
		//			logger.error("File not found");
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			logger.error("I/O exception");
		//			e.printStackTrace();
		//		} catch (ParseException e) {
		//			logger.error("Could not parse for JSON object");
		//			e.printStackTrace();
		//		} catch (NullPointerException npe){
		//			logger.error("Null pointer exception");
		//			npe.printStackTrace();
		//		}
		int count = 0;
		try{
			File csvFile = new File("C:\\Users\\PGuruprasad\\Desktop\\pocketsecurity_raw\\31.08-07.09\\location.csv");
			BufferedReader b = new BufferedReader(new FileReader(csvFile));
			String readline = "";

			while ((readline = b.readLine()) != null) {
				String coord[] = readline.split(",");
				logger.info("count: "+ ++count);

				double lat = Double.parseDouble(coord[0]);
				double lng = Double.parseDouble(coord[1]);
				if(postalAddressTable.getPostalAddress(lat,lng) == 1) System.out.println("Address skip");
				else postalAddressTable.addPostalAddress(lat, lng);		
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException ioe){
			ioe.printStackTrace();
		}
	}

	private static void databaseNFC() {
		NFCEntry nfcEntry =  new NFCEntry(dbHelper.getConnection());

		//create table
		nfcEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"NFCEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				nfcEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseNetwork() {
		NetworkEntry networkEntry =  new NetworkEntry(dbHelper.getConnection());

		//create table
		networkEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"NetworkEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				networkEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("info").toString(),
						jObject.get("state").toString()
						);
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

	private static void databaseCellLocation() {
		CellLocationEntry cellLocationEntry =  new CellLocationEntry(dbHelper.getConnection());

		//create table
		cellLocationEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"CellLocationEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				cellLocationEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("areaCode").toString(),
						jObject.get("cellType").toString()
						);
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

	private static void databaseCellEntry() {
		CellEntry cellEntry =  new CellEntry(dbHelper.getConnection());

		//create table
		cellEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"CellEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				cellEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseCallState() {
		CallStateEntry callStateEntry =  new CallStateEntry(dbHelper.getConnection());

		//create table
		callStateEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"CallStateEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				callStateEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseBTStatic() {
		BluetoothStaticAttributesEntry staticAttributesEntry =  new BluetoothStaticAttributesEntry(dbHelper.getConnection());

		//create table
		staticAttributesEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"BluetoothStaticAtributesEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				staticAttributesEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("address").toString(),
						jObject.get("name").toString()
						);
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

	private static void databaseBTState() {
		BluetoothStateEntry bluetoothStateEntry =  new BluetoothStateEntry(dbHelper.getConnection());

		//create table
		bluetoothStateEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"BluetoothStateEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				bluetoothStateEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseBTScan() {
		BluetoothScanModeEntry scanModeEntry =  new BluetoothScanModeEntry(dbHelper.getConnection());

		//create table
		scanModeEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"BluetoothScanModeEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				scanModeEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseBTDiscovery() {
		BluetoothDiscoveryEntry bluetoothDiscoveryEntry =  new BluetoothDiscoveryEntry(dbHelper.getConnection());

		//create table
		bluetoothDiscoveryEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"BluetoothDiscoveryEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				bluetoothDiscoveryEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databaseBTConn() {
		BluetoothConnectionEntry bluetoothConnectionEntry =  new BluetoothConnectionEntry(dbHelper.getConnection());

		//create table
		bluetoothConnectionEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"BluetoothConnectionEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				//				System.out.println("Json: \n"+ jObject);

				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				bluetoothConnectionEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("foreignAddress").toString(),
						jObject.get("foreignName").toString(),
						jObject.get("state").toString()
						);
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

	private static void databaseAirplaneMode() {
		AirplaneModeEntry airplaneModeEntry =  new AirplaneModeEntry(dbHelper.getConnection());

		//create table
		airplaneModeEntry.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"AirplaneModeEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				logger.info("count: "+ ++count);
				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				airplaneModeEntry.insertIfNot(
						innerJobj.get("name").toString(), 
						jObject.get("userID").toString(),
						jObject.get("timestamp").toString(),//.substring(0, 10),
						jObject.get("state").toString()
						);
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

	private static void databasePowerInfo() {
		PowerInfoEntry powerInfoData = new PowerInfoEntry(dbHelper.getConnection());

		//create table
		powerInfoData.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"PowerEntry-000000000000.json");
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
		LocationEntry locationData = new LocationEntry(dbHelper.getConnection());

		//create table
		locationData.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"LocationEntry-000000000000.json");
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
				locationData.insertValuesIfNot(
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
		PhysicalActivityEntry activityData = new PhysicalActivityEntry(dbHelper.getConnection());

		//create table
		activityData.createTableIfNot();

		//		insert data into the table
		try {
			File file = new File(Constants.filePath+"ActivityEntry-000000000000.json");
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

		appInfo.createTableIfNot();


		try {
			File file = new File(Constants.filePath+"ForegroundBackgroundEventEntry-000000000000.json");
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
		ForegroundAppEntry foregroundAppData = new ForegroundAppEntry(dbHelper.getConnection());


		//create table
		foregroundAppData.createTableIfNot();

		//read josn file and insert into database

		try {
			File file = new File(Constants.filePath+"ForegroundBackgroundEventEntry-000000000000.json");
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

				foregroundAppData.insertValuesIfNot(
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
		foregroundAppData.indexTable();
	}

	private static void databaseMSMS() {		
		MSMSEntry msmsData = new MSMSEntry(dbHelper.getConnection());

		//create table
		msmsData.createTableIfNot();

		// read json and insert into database
		try {
			File file = new File(Constants.filePath+"MSMSEntry-000000000000.json");
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);

				//				logger.info("count: "+ ++count);
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
		msmsData.indexTable();
	}
}
