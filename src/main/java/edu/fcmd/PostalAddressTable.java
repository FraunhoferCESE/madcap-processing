package edu.fcmd;

import static edu.generated.rel.tables.Appinfotable.APPINFOTABLE;
import static edu.generated.rel.tables.Postaladdresstable.POSTALADDRESSTABLE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PostalAddressTable implements EntryTable {
	private Logger logger;

	private Connection connection;

	public PostalAddressTable(Connection connection) {
		this.connection = connection;
		logger = Logger.getLogger(AppInfoData.class);
	}
	
	@Override
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("postaladdresstable")
		.column("Latitude", SQLDataType.DOUBLE.nullable(false))
		.column("Longitude", SQLDataType.DOUBLE.nullable(false))
		.column("Address", SQLDataType.VARCHAR(100).nullable(false))
		.column("Accuracy", SQLDataType.VARCHAR(45).nullable(false))
		
//		.constraint(DSL.constraint("PK_POSTALADDRESSTABLE").primaryKey("ADDRESS"))
		.execute();

		dslContext.close();
	}
	
	public void addPostalAddress(Double lat, Double lng){
		logger.debug("Fetching address from Google...");

		String geoURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
		String key = "&key="+Constants.geocodeKey;
		String latlng = lat+","+lng;
		String lookupURL = geoURL+latlng+key;

//		createTableIfNot();
			
			try {
				//connect to url
				
				HttpURLConnection httpConn = (HttpURLConnection) new URL(lookupURL).openConnection();
				httpConn.setRequestProperty("Accept", "application/json");
				httpConn.setRequestMethod("GET");
				
				if(httpConn.getResponseCode()!=200){
					System.out.println("HTTP response: "+httpConn.getResponseCode());
				}
				
				//read response
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
				
				//JSON the response
				JSONObject response = (JSONObject) new JSONParser().parse(buffReader);
				JSONArray result = (JSONArray) response.get("results");
				JSONObject result1 = (JSONObject) result.get(0);
				String formattedAddress = (String) result1.get("formatted_address");
				JSONObject result1Geometry = (JSONObject) result1.get("geometry"); 
				String accuracy =  (String) result1Geometry.get("location_type");
				
				System.out.println(response);
//				System.out.println("Results"); System.out.println(result);
//				System.out.println("Results1"); System.out.println(result1);
//				System.out.println("Address"); System.out.println(formattedAddress);
//				System.out.println("accuracy"); System.out.println(accuracy);
			
				insertValuesIfNot(lat,lng,formattedAddress,accuracy);
				
				
				
			}catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				
	}

	private void insertValuesIfNot(Double lat, Double lng, String formattedAddress, String accuracy) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(POSTALADDRESSTABLE, POSTALADDRESSTABLE.LATITUDE, POSTALADDRESSTABLE.LONGITUDE, POSTALADDRESSTABLE.ADDRESS, POSTALADDRESSTABLE.ACCURACY)
		.values(lat, lng, formattedAddress, accuracy)
//		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();

		logger.debug("Address info updated");
	}

	@Override
	public void indexTable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropTable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void truncateTable() {
		// TODO Auto-generated method stub

	}

	public int getPostalAddress(double latitude, double longitude) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		return dslContext.select(POSTALADDRESSTABLE.ADDRESS)
				.from(POSTALADDRESSTABLE)
				.where(POSTALADDRESSTABLE.LATITUDE.eq(latitude)
						.and(POSTALADDRESSTABLE.LONGITUDE.eq(longitude)))
				.execute();
	}

}
