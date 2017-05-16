import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.management.Query;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.*;


public class MainDatabaseClass {

	public static void main(String[] args) {

		DatabaseHelper dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();
		
		try {
			File file = new File("C:\\Users\\PGuruprasad\\Desktop\\pocketsecurity_raw\\json\\MSMSEntry-000000000000.json");
			System.out.println("File Size: "+(int)file.length());
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readline = "";
			int count = 0;

			JSONParser jParser = new JSONParser();
			while((readline = b.readLine()) != null){
				JSONObject jObject = (JSONObject) jParser.parse(readline);
				
				System.out.println("Count: "+ ++count);
				System.out.println("Json: \n"+ jObject);
				System.out.println("Json string: \n"+ jObject.toJSONString());
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
//				System.out.println(sdf.format(jObject.get("timestamp").toString().substring(0, 9)));
				
				JSONObject innerJobj = (JSONObject) jObject.get("__key__");
				dbHelper.insertInto("msms",
						innerJobj.get("name").toString(), 
						jObject.get("action").toString(), 
						jObject.get("extra").toString(), 
						jObject.get("timestamp").toString().substring(0, 10),
						jObject.get("userID").toString());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException npe){
			npe.printStackTrace();
		}
		finally {
			dbHelper.disconnect();
		}
	}
}
