package edu.fcmd;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import org.apache.log4j.BasicConfigurator;
import org.hamcrest.CoreMatchers;
import org.jooq.DSLContext;
import org.jooq.Record;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.fcmd.generated.tables.Msmsentry;

public class MainDatabaseClassTest {

	static Logger logger;
	static DatabaseHelper dbHelper;
	static DSLContext create;
	static MSMSData testMsmsData;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();

		testMsmsData = new MSMSData(dbHelper.getConnection(), dbHelper.getStatement());

		create = DSL.using(dbHelper.getConnection(), SQLDialect.MYSQL);
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

	@Test
	public void testBetweenTimestamps() {

		//		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
		//				.where(Msmsentry.MSMSENTRY.TIME_STAMP.between(new Timestamp(0165, 01, 01,0,0,0,0), new Timestamp(0165, 01, 14,0,0,0,0)))
		//				.fetch();

		Result<Record> results = testMsmsData.queryMSMS(new Timestamp(0165, 01, 01,0,0,0,0), new Timestamp(0165, 01, 14,0,0,0,0));
		for(Record r : results)
			assertTrue("Timestamp don't match", r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).after(new Timestamp(0165, 01, 01, 0,0,0,0)));
	}

	@Test
	public void testStartTimestamps() {
		Result<Record> results = testMsmsData.queryMSMS(new Timestamp(0165, 02, 01, 0, 0, 0, 0), null);
		for(Record r : results){
			assertTrue(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).after(new Timestamp(0165, 02, 01, 0, 0, 0, 0)));
		}
	}

	@Test
	public void testEndTimestamp() {
		Result<Record> results = testMsmsData.queryMSMS(null, new Timestamp(0165, 02, 14, 0, 0, 0, 0));
		for(Record r : results){
			assertTrue(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).before(new Timestamp(0165, 02, 14, 0, 0, 0, 0)));
		}
	}

	@Test
	public void testStartDate() {
		Result<Record> results = testMsmsData.queryMSMS("2017-02-04");
		for(Record r : results){
			assertThat(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).toString(), CoreMatchers.containsString("2017-02-04"));
		}
	}

	@Test
	public void testReceiveAction(){
		Result<Record> results = testMsmsData.queryMSMS("SMS_RECEIVED_TEXT_BASED");
		for(Record r : results)
			assertEquals("SMS_RECEIVED_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	}

	@Test
	public void testSentAction(){
		Result<Record> results = testMsmsData.queryMSMS("SMS_SENT_TEXT_BASED");
		for(Record r : results)
			assertEquals("SMS_SENT_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	}

	@Test
	public void testUserID1(){
		Result<Record> results = testMsmsData.queryMSMS("111691272636258957769");
		for(Record r : results)
			assertEquals("111691272636258957769", r.getValue(Msmsentry.MSMSENTRY.USERID));
	}

	@Test
	public void testUserID2(){
		Result<Record> results = testMsmsData.queryMSMS("105877430327962648647");
		for(Record r : results)
			assertEquals("105877430327962648647", r.getValue(Msmsentry.MSMSENTRY.USERID));
	}

	@Test
	public void testCountBetweenDates(){
		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, new Timestamp(0165, 00, 01, 0, 0, 0, 0), new Timestamp(0165, 06, 14, 0, 0, 0, 0));
		System.out.println("Between dates");
		for(Record3<String, String, Integer> r : result)
			System.out.println(r.toString());
	}
	
	@Test
	public void testCountStartDate(){
		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, new Timestamp(0165, 03, 01, 0, 0, 0, 0),null);
		System.out.println("Start date");
		for(Record3<String, String, Integer> r : result)
			System.out.println(r.toString());
	}
	
	@Test
	public void testCountEndDate(){
		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, null, new Timestamp(0165, 03, 28, 0, 0, 0, 0));
		System.out.println("End date");
		for(Record3<String, String, Integer> r : result)
			System.out.println(r.toString());
	}
	
	@Test
	public void testCountAll(){
		Result<Record3<String, String, Integer>> result = testMsmsData.countQueryMSMS(Msmsentry.MSMSENTRY.USERID, null, null);
		System.out.println("All date");
		for(Record3<String, String, Integer> r : result)
			System.out.println(r.toString());
	}

}
