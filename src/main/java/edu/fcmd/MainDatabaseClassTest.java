package edu.fcmd;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.jooq.CaseWhenStep;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//import edu.fcmd.generated.tables.Msmsentry;



public class MainDatabaseClassTest {

	static Logger logger;
	static DatabaseHelper dbHelper;
	static DSLContext dslContext;
	static MSMSEntry testMsmsData;
	static ForegroundAppEntry testfbData;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger = Logger.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();

		testMsmsData = new MSMSEntry(dbHelper.getConnection());
		testfbData = new ForegroundAppEntry(dbHelper.getConnection());

		dslContext = DSL.using(dbHelper.getConnection(), SQLDialect.MYSQL);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbHelper.disconnect();
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	//	@Test
	//	public void testBetweenTimestampsMSMS() {
	//
	//		Result<Record> results = testMsmsData.queryMSMS(new Timestamp(0165, 01, 01,0,0,0,0), new Timestamp(0165, 01, 14,0,0,0,0));
	//		for(Record r : results)
	//			assertTrue("Timestamp don't match", r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).after(new Timestamp(0165, 01, 01, 0,0,0,0)));
	//	}
	//
	//	@Test
	//	public void testStartTimestampsMSMS() {
	//		Result<Record> results = testMsmsData.queryMSMS(new Timestamp(0165, 02, 01, 0, 0, 0, 0), null);
	//		for(Record r : results){
	//			assertTrue(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).after(new Timestamp(0165, 02, 01, 0, 0, 0, 0)));
	//		}
	//	}
	//
	//	@Test
	//	public void testEndTimestampMSMS() {
	//		Result<Record> results = testMsmsData.queryMSMS(null, new Timestamp(0165, 02, 14, 0, 0, 0, 0));
	//		for(Record r : results){
	//			assertTrue(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).before(new Timestamp(0165, 02, 14, 0, 0, 0, 0)));
	//		}
	//	}
	//
	//	@Test
	//	public void testStartDateMSMS() {
	//		Result<Record> results = testMsmsData.queryMSMS("2017-02-04");
	//		for(Record r : results){
	//			assertThat(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).toString(), CoreMatchers.containsString("2017-02-04"));
	//		}
	//	}
	//
	//	@Test
	//	public void testReceiveActionMSMS(){
	//		Result<Record> results = testMsmsData.queryMSMS("SMS_RECEIVED_TEXT_BASED");
	//		for(Record r : results)
	//			assertEquals("SMS_RECEIVED_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	//	}
	//
	//	@Test
	//	public void testSentActionMSMS(){
	//		Result<Record> results = testMsmsData.queryMSMS("SMS_SENT_TEXT_BASED");
	//		for(Record r : results)
	//			assertEquals("SMS_SENT_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	//	}
	//
	//	@Test
	//	public void testUserID1MSMS(){
	//		Result<Record> results = testMsmsData.queryMSMS("111691272636258957769");
	//		for(Record r : results)
	//			assertEquals("111691272636258957769", r.getValue(Msmsentry.MSMSENTRY.USERID));
	//	}
	//
	//	@Test
	//	public void testUserID2MSMS(){
	//		Result<Record> results = testMsmsData.queryMSMS("105877430327962648647");
	//		for(Record r : results)
	//			assertEquals("105877430327962648647", r.getValue(Msmsentry.MSMSENTRY.USERID));
	//	}
	//
	//	@Test
	//	public void testCountBetweenDatesMSMS(){
	//		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, new Timestamp(0165, 00, 01, 0, 0, 0, 0), new Timestamp(0165, 06, 14, 0, 0, 0, 0));
	//		System.out.println("Between dates");
	//		for(Record3<String, String, Integer> r : result)
	//			System.out.println(r.toString());
	//	}
	//
	//	@Test
	//	public void testCountStartDateMSMS(){
	//		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, new Timestamp(0165, 03, 01, 0, 0, 0, 0),null);
	//		System.out.println("Start date");
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void testCountEndDateMSMS(){
	//		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, null, new Timestamp(0165, 03, 28, 0, 0, 0, 0));
	//		System.out.println("End date");
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void testCountAllMSMS(){
	//		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, null, null);
	//		System.out.println("All date");
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void testGroupingAppsByUserBetweenTimestamp(){
	//		Result<Record3<String, String, Timestamp>> result = testfbData.getAppsByUserOnTime("111691272636258957769", new Timestamp(0165, 01, 01, 0, 0, 0, 0), new Timestamp(0165, 01, 28, 0, 0, 0, 0));
	//	}
	//	
	//	@Test
	//	public void testGroupingAppsByUserStartTimestamp(){
	//		Result<Record3<String, String, Timestamp>> result = testfbData.getAppsByUserOnTime("111691272636258957769", new Timestamp(0165, 01, 01, 0, 0, 0, 0), null);
	//	}
	//	
	//	@Test
	//	public void testGroupingAppsByUserEndTimestamp(){
	//		Result<Record3<String, String, Timestamp>> result = testfbData.getAppsByUserOnTime("111691272636258957769", null, new Timestamp(0165, 01, 28, 0, 0, 0, 0));
	//	}
	//
	//
	//	@Test
	//	public void testCountOfAppsUsedByUser(){
	//		Result<Record3<String, String, Integer>> result = testfbData.getCountOfAppsUsedByUser();
	//		System.out.println(result);
	//	}
	//
	//	@Test
	//	public void testCountUsersbyApp(){
	//		Result<Record2<String, Integer>> result = testfbData.getNumberOfUsersByApp();
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void testCountAppsbyUserID(){
	//		Result<Record2<String, Integer>> result = testfbData.getNumberOfAppsByUserid();
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void getAppNameAndCategoryByTimeTwoDates(){
	//		Result<Record4<String, String, String, Timestamp>> result = testfbData.getAppNameAndCategoryByTime(new Timestamp(0165, 01, 01, 0, 0, 0, 0),new Timestamp(0165, 01, 28, 0, 0, 0, 0));
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void getAppNameAndCategoryByTimeStartDates(){
	//		Result<Record4<String, String, String, Timestamp>> result = testfbData.getAppNameAndCategoryByTime(new Timestamp(0165, 02, 28, 0, 0, 0, 0), null);
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void getAppNameAndCategoryByTimeEndDates(){
	//		Result<Record4<String, String, String, Timestamp>> result = testfbData.getAppNameAndCategoryByTime(null, new Timestamp(0165, 01, 28, 0, 0, 0, 0));
	//		System.out.println(result.toString());
	//	}
	//
	//	@Test
	//	public void getAppNameAndCategoryByTimeNoDates(){
	//		Result<Record4<String, String, String, Timestamp>> result = testfbData.getAppNameAndCategoryByTime(null, null);
	//		System.out.println(result.toString());
	//	}
	//	
	//	@Test
	//	public void getProbablePhysicalActivity(){
	//		PhysicalActivityEntry activityData = new PhysicalActivityEntry(dbHelper.getConnection());
	//		
	//		int result = activityData.getPhysicalActivity();
	//		System.out.println(result);
	//	}
	//	
	//	@Test
	//	public void testGetAppNameAndCategory(){
	//		AppInfoData appData = new AppInfoData(dbHelper.getConnection());
	//		int result = appData.getAppNameAndCategory("com.spotify.music");
	//		System.out.println("com.spotify.music="+result);
	//		
	//		result = appData.getAppNameAndCategory("com.google.android.apps.photos.scanner");
	//		System.out.println("com.UMD.OperationGenovesee="+result);
	//	}
}
