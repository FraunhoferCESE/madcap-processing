package edu.fcmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static edu.generated.rel.tables.Censusinfotable.CENSUSINFOTABLE;

public class CensusData implements EntryTable {

	private Logger logger;

	private Connection connection;

	public CensusData(Connection connection) {
		this.connection = connection;
		logger = Logger.getLogger(AppInfoData.class);
	}

	@Override
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("censusinfotable")
		.column("Latitude", SQLDataType.DOUBLE.nullable(false))
		.column("Longitude", SQLDataType.DOUBLE.nullable(false))
		.column("Level", SQLDataType.VARCHAR(20).nullable(false))
		.column("Year", SQLDataType.VARCHAR(4).nullable(true))
		.column("State", SQLDataType.VARCHAR(4).nullable(true))
		.column("County", SQLDataType.VARCHAR(50).nullable(true))
		.column("Tract", SQLDataType.VARCHAR(10).nullable(true))
		.column("Block_Group", SQLDataType.VARCHAR(2).nullable(true))
		.column("Block", SQLDataType.VARCHAR(4).nullable(true))
		.column("Place", SQLDataType.VARCHAR(45).nullable(true))
		.column("Place_Name", SQLDataType.VARCHAR(45).nullable(true))

		.column("Income", SQLDataType.VARCHAR(8).nullable(true))
		.column("Population", SQLDataType.VARCHAR(10).nullable(true))
		.column("Percapita", SQLDataType.VARCHAR(8).nullable(true))
		.column("Median_age", SQLDataType.VARCHAR(4).nullable(true))
		.column("Employed", SQLDataType.VARCHAR(10).nullable(true))
		.column("Unemployed", SQLDataType.VARCHAR(10).nullable(true))
		.column("Poverty", SQLDataType.VARCHAR(10).nullable(true))
		.column("Total_household", SQLDataType.VARCHAR(10).nullable(true))
		.column("Single_Female_Household", SQLDataType.VARCHAR(10).nullable(true))
		.column("Single_Male_Household", SQLDataType.VARCHAR(10).nullable(true))
		.column("Male_under_5",SQLDataType.INTEGER.nullable(true))
		.column("Male_5_14",SQLDataType.INTEGER.nullable(true))
		.column("Male_15_17",SQLDataType.INTEGER.nullable(true))
		.column("Male_18_24",SQLDataType.INTEGER.nullable(true))
		.column("Male_25_44",SQLDataType.INTEGER.nullable(true))
		.column("Male_45_64",SQLDataType.INTEGER.nullable(true))
		.column("Male_above_65",SQLDataType.INTEGER.nullable(true))
		.column("Female_5_14",SQLDataType.INTEGER.nullable(true))
		.column("Female_15_17",SQLDataType.INTEGER.nullable(true))
		.column("Female_18_24",SQLDataType.INTEGER.nullable(true))
		.column("Female_25_44",SQLDataType.INTEGER.nullable(true))
		.column("Female_45_64",SQLDataType.INTEGER.nullable(true))
		.column("Female_above_65",SQLDataType.INTEGER.nullable(true))

		.execute();
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

	public int getCensusInfo(double lat, double lng) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		return dslContext.select(CENSUSINFOTABLE.LATITUDE, CENSUSINFOTABLE.LONGITUDE)
				.from(CENSUSINFOTABLE)
				.where(CENSUSINFOTABLE.LATITUDE.eq(lat)
						.and(CENSUSINFOTABLE.LONGITUDE.eq(lng)))
				.execute();
	}

	public void addCensusInfo(double lat, double lng) {

		String fipsUrl = "http://data.fcc.gov/api/block/2010/find?showall=true&format=json&";
		fipsUrl += "latitude="+lat
				+ "&longitude="+lng;
		System.out.println(fipsUrl);

		HttpURLConnection httpConn, httpConn1, httpConn2, httpConn3;
		try {
			httpConn = (HttpURLConnection) new URL(fipsUrl).openConnection();
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setRequestMethod("GET");

			if(httpConn.getResponseCode()!=200){
				System.out.println("HTTP response: "+httpConn.getResponseCode());
			}

			//read response
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			JSONObject response = (JSONObject) new JSONParser().parse(buffReader);
			System.out.println(response);
			JSONObject block = (JSONObject) response.get("Block");
			String fipsCode = block.get("FIPS").toString();

			String stateID = fipsCode.substring(0, 2);
			String countyID = fipsCode.substring(2, 5);
			String tractID = fipsCode.substring(5, 11);
			String blockGroupID = fipsCode.substring(11, 12);
			String blockID = fipsCode.substring(11,15);

			JSONObject county = (JSONObject) response.get("County");
			String countyName = county.get("name").toString();

			JSONObject state = (JSONObject) response.get("State");
			String stateName = state.get("code").toString();

			logger.info("blockGroup: "+blockGroupID);


			String censusUrl1 = "https://api.census.gov/data/2014/acs5?key="+Constants.CITYSDK_KEY;
			censusUrl1 += "&get=NAME,B19013_001E,B19301_001E,B01003_001E," //income(1), income_per_capita(2), population(3) 
					+ "B01002_001E,B23025_005E,B23025_004E,B17001_002E," //median age (4), unemployed(5), employed(6), poverty(7)
					+ "B11002_011E,B11002_006E," //Single_Female_Household(8), Single_Male_Household(9)

					+ "B01001_003E," //male under 5 (10)
					+ "B01001_004E,B01001_005E," //male 5-14 (11,12)
					+ "B01001_006E," //male 15-17 (13)
					+ "B01001_007E,B01001_008E,B01001_009E,B01001_010E," //male 18-24 (14,15,16,17)
					+ "B01001_011E,B01001_012E,B01001_013E,B01001_014E,"//male 25-44 (18,19,20,21)
					+ "B01001_015E,B01001_016E,B01001_017E,B01001_018E,B01001_019E,"//male 45-64 (22,23,24,25,26)
					+ "B01001_020E,B01001_021E,B01001_022E,B01001_023E,B01001_024E,B01001_025E,"//male 65 above (27,28,29,30,31,32)

					+ "B01001_027E," //female under 5 (33)
					+ "B01001_028E,B01001_029E," //female 5-14 (34,35)
					+ "B01001_030E," //female 15-17 (36)
					+ "B01001_031E,B01001_032E,B01001_033E,B01001_034E," //female 18-24 (37,38,39,40)
					+ "B01001_035E,B01001_036E,B01001_037E,B01001_038E"//,"//female 25-44 (41,42,43,44)
					//					+ "B01001_039E,B01001_040E,B01001_041E,B01001_042E,B01001_043E,"//female 45-64 (45,46,47,48,49)
					//					+ "B01001_044E,B01001_045E,B01001_046E,B01001_047E,B01001_048E,B01001_049E"//female 65 above (50,51,52,53,54,55)

					+ "&for=block+group:"+blockGroupID
					+ "&in=state:"+stateID
					+ "+county:"+countyID
					+ "+tract:"+tractID;

			logger.info("url1 "+censusUrl1);

			httpConn1 = (HttpURLConnection) new URL(censusUrl1).openConnection();
			httpConn1.setRequestProperty("Accept", "application/json");
			httpConn1.setRequestMethod("GET");

			if(httpConn1.getResponseCode()!=200){
				System.out.println("HTTP response: "+httpConn1.getResponseCode());
			}

			//read response
			BufferedReader buffReader1 = new BufferedReader(new InputStreamReader(httpConn1.getInputStream()));
			JSONArray response1 = (JSONArray) new JSONParser().parse(buffReader1);
			logger.info(response1.get(1));
			JSONArray line = (JSONArray) response1.get(1);

			insertValuesIfNot(lat, lng, "Block Group", "2014", stateName, countyName, tractID, blockGroupID, blockID, "NULL", "NULL", 
					line.get(1).toString(), 
					line.get(2).toString(), 
					line.get(3).toString(),
					line.get(4).toString(), 
					line.get(5).toString(), 
					line.get(6).toString(),
					line.get(7)==null?"NULL":line.get(7).toString(),
					line.get(8).toString(),
					line.get(9).toString(),

					add(line.get(10),0)+"",
					add(line.get(11),line.get(12))+"",
					add(line.get(13),0)+"",
					add( add(line.get(14),line.get(15)), add(line.get(16),line.get(17)) )+"",
					add( add(line.get(18),line.get(19)), add(line.get(20),line.get(21)) )+"",
					add( add( add(line.get(22),line.get(23)), add(line.get(24),line.get(25)) ), line.get(26) )+"" ,
					add( add( add(line.get(27),line.get(28)), add(line.get(29),line.get(30))) ,add(line.get(31),line.get(32)) )+"",

					add(line.get(33),0)+"",
					add(line.get(34),line.get(35))+"",
					add(line.get(36),0)+"",
					add( add(line.get(37),line.get(38)), add(line.get(39),line.get(40)) )+"",
					add( add(line.get(41),line.get(42)), add(line.get(43),line.get(44)) )+""
					//					add( add( add(line.get(4),line.get(5)), add(line.get(6),line.get(7)) ), line.get(8) )+"",
					//					add( add( add(line.get(9),line.get(10)), add(line.get(11),line.get(12))) ,add(line.get(13),line.get(14)) )+""

					);

			
			//Census url restircts 50 variables per GET call. 
			//Second URL is now used to fetch the remaining Vairables required

			String censusUrl2 = "https://api.census.gov/data/2014/acs5?key="+Constants.CITYSDK_KEY;
			censusUrl2 += "&get=NAME,B19013_001E,B19301_001E,B01003_001E," // //income(1), income_per_capita(2), population(3) 
					+ "B01001_039E,B01001_040E,B01001_041E,B01001_042E,B01001_043E,"//female 45-64 (4,5,6,7,8)
					+ "B01001_044E,B01001_045E,B01001_046E,B01001_047E,B01001_048E,B01001_049E"//female 65 above (9,10,11,12,13,14)

					+ "&for=block+group:"+blockGroupID
					+ "&in=state:"+stateID
					+ "+county:"+countyID
					+ "+tract:"+tractID;

			logger.info("url2 "+censusUrl2);

			httpConn2 = (HttpURLConnection) new URL(censusUrl2).openConnection();
			httpConn2.setRequestProperty("Accept", "application/json");
			httpConn2.setRequestMethod("GET");

			if(httpConn2.getResponseCode()!=200){
				System.out.println("HTTP response: "+httpConn2.getResponseCode());
			}

			//read response
			BufferedReader buffReader2 = new BufferedReader(new InputStreamReader(httpConn2.getInputStream()));
			JSONArray response2 = (JSONArray) new JSONParser().parse(buffReader2);
			logger.info(response2.get(1));
			JSONArray line2 = (JSONArray) response2.get(1);

			appendValuesIfNot(lat, lng, 
					add( add( add(line2.get(4),line2.get(5)), add(line2.get(6),line2.get(7)) ), line2.get(8) )+"",
					add( add( add(line2.get(9),line2.get(10)), add(line2.get(11),line2.get(12))) ,add(line2.get(13),line2.get(14)) )+""

					);
			
			String censusUrl3 = "https://api.census.gov/data/2014/acs5?key="+Constants.CITYSDK_KEY;
			censusUrl3 += "&get=NAME,B19013_001E,B19301_001E,B01003_001E," // //income(1), income_per_capita(2), population(3) 
					+ "B11001_001E" //total household (4)

					+ "&for=block+group:"+blockGroupID
					+ "&in=state:"+stateID
					+ "+county:"+countyID
					+ "+tract:"+tractID;

			logger.info("url3 "+censusUrl3);

			httpConn3 = (HttpURLConnection) new URL(censusUrl3).openConnection();
			httpConn3.setRequestProperty("Accept", "application/json");
			httpConn3.setRequestMethod("GET");

			if(httpConn3.getResponseCode()!=200){
				System.out.println("HTTP response: "+httpConn3.getResponseCode());
			}

			//read response
			BufferedReader buffReader3 = new BufferedReader(new InputStreamReader(httpConn3.getInputStream()));
			JSONArray response3 = (JSONArray) new JSONParser().parse(buffReader3);
			logger.info(response3.get(1));
			JSONArray line3 = (JSONArray) response3.get(1);

			appendValuesIfNot(lat, lng, line3.get(4).toString());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NullPointerException npe){
			npe.getMessage();
			npe.printStackTrace();
			insertValuesIfNot(lat, lng, "NA", 
					"NA", "NA", "NA", "NA", "NA",
					"NA", "NA", "NA", 
					"NA", "NA", "NA",
					"NA", "NA","NA", 
					"NA", "NA", "NA","NA", 
					"NA", "NA", 
					"NA", "NA", "NA", "NA",
					"NA", "NA", "NA", 
					"NA", "NA" 
					//"NA","NA"
					);
			appendValuesIfNot(lat, lng, "NA","NA");
			appendValuesIfNot(lat, lng, "NA");
		}
	}

	private void appendValuesIfNot(double lat, double lng, String household) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.update(CENSUSINFOTABLE)
		.set(CENSUSINFOTABLE.TOTAL_HOUSEHOLD, household)
		.where(CENSUSINFOTABLE.LATITUDE.eq(lat)
				.and(CENSUSINFOTABLE.LONGITUDE.eq(lng)))
		.execute();

		dslContext.close();
	}

	private void appendValuesIfNot(double lat, double lng,  String f4564, String fAbove65) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.update(CENSUSINFOTABLE)
		.set(CENSUSINFOTABLE.FEMALE_45_64, f4564)
		.set(CENSUSINFOTABLE.FEMALE_ABOVE_65,fAbove65)
		.where(CENSUSINFOTABLE.LATITUDE.eq(lat)
				.and(CENSUSINFOTABLE.LONGITUDE.eq(lng)))
		.execute();

		dslContext.close();
	}

	private int add(Object a, Object b) {

		return Integer.parseInt(a.toString()) + Integer.parseInt(b.toString());
	}

	private void insertValuesIfNot(double lat, double lng, String level, String year, String stateID,
			String countyID, String tractID, String blockGroupID, String blockID, 
			String place, String placeName, 
			String income, String perCapita, String population, 
			String age, String unemployed, String employed, String poverty, 
			String singleFemale, String singleMale, 
			String mUnder5, String m514, String m1517, 
			String m1824, String m2544, String m4564, String mAbove65,
			String fUnder5, String f514, String f1517, 
			String f1824, String f2544
			//String f4564, String fAbove65
			) {

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(CENSUSINFOTABLE, CENSUSINFOTABLE.LATITUDE, CENSUSINFOTABLE.LONGITUDE, CENSUSINFOTABLE.LEVEL,
				CENSUSINFOTABLE.YEAR, CENSUSINFOTABLE.STATE, CENSUSINFOTABLE.COUNTY,
				CENSUSINFOTABLE.TRACT, CENSUSINFOTABLE.BLOCK_GROUP, CENSUSINFOTABLE.BLOCK, 
				CENSUSINFOTABLE.PLACE, CENSUSINFOTABLE.PLACE_NAME,
				CENSUSINFOTABLE.INCOME, CENSUSINFOTABLE.PERCAPITA, CENSUSINFOTABLE.POPULATION,
				CENSUSINFOTABLE.MEDIAN_AGE, CENSUSINFOTABLE.UNEMPLOYED, CENSUSINFOTABLE.EMPLOYED,
				CENSUSINFOTABLE.POVERTY, 

				CENSUSINFOTABLE.SINGLE_FEMALE_HOUSEHOLD, CENSUSINFOTABLE.SINGLE_MALE_HOUSEHOLD,

				CENSUSINFOTABLE.MALE_UNDER_5, CENSUSINFOTABLE.MALE_5_14, CENSUSINFOTABLE.MALE_15_17,
				CENSUSINFOTABLE.MALE_18_24, CENSUSINFOTABLE.MALE_25_44, CENSUSINFOTABLE.MALE_45_64, CENSUSINFOTABLE.MALE_ABOVE_65,

				CENSUSINFOTABLE.FEMALE_UNDER_5, CENSUSINFOTABLE.FEMALE_5_14, CENSUSINFOTABLE.FEMALE_15_17,
				CENSUSINFOTABLE.FEMALE_18_24, CENSUSINFOTABLE.FEMALE_25_44
				//CENSUSINFOTABLE.FEMALE_45_64, CENSUSINFOTABLE.FEMALE_ABOVE_65
				)
		.values(lat, lng, level,
				year, stateID, countyID,
				tractID, blockGroupID, blockID, 
				place, placeName,
				income, perCapita, population,
				age, unemployed, employed, poverty,
				singleFemale, singleMale,
				mUnder5, m514, m1517,
				m1824, m2544, m4564,mAbove65,
				fUnder5, f514, f1517,
				f1824, f2544 
				//f4564, fAbove65
				)
		.execute();

		dslContext.close();

	}
}
