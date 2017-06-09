package edu.fcmd;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.log4j.BasicConfigurator;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
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
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
		BasicConfigurator.configure();

		dbHelper = DatabaseHelper.getInstance();
		dbHelper.init("madcapman","!QAZ@WSX");
		dbHelper.connect();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbHelper.disconnect();
	}

	@Before
	public void setUp() throws Exception {
		create = DSL.using(dbHelper.getConnection(), SQLDialect.MYSQL);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testTwoDates() {

		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.TIME_STAMP.between(new Timestamp(0165, 01, 01,0,0,0,0), new Timestamp(0165, 01, 14,0,0,0,0)))
				.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
				.fetch();
		for(Record r : results)
			assertTrue("Timestamp don't match", r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP).after(new Timestamp(0165, 01, 01, 0,0,0,0)));
	}

	@Test
	public void testOneDate() {

		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.TIME_STAMP.like("2017-02-01%"))
				.fetch();
		for(Record r : results){
			assertEquals(r.getValue(Msmsentry.MSMSENTRY.TIME_STAMP), new Timestamp(0165, 01, 01, 0, 0, 0, 0));
		}
	}

	@Test
	public void testReceiveAction(){
		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.ACTION.eq("SMS_RECEIVED_TEXT_BASED"))
				.fetch();

		for(Record r : results)
			assertEquals("SMS_RECEIVED_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	}

	@Test
	public void testSentAction(){
		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.ACTION.eq("SMS_SENT_TEXT_BASED"))
				.fetch();

		for(Record r : results)
			assertEquals("SMS_SENT_TEXT_BASED", r.getValue(Msmsentry.MSMSENTRY.ACTION));
	}

	@Test
	public void testUserID1(){
		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.ACTION.eq("111691272636258957769"))
				.fetch();

		for(Record r : results)
			assertEquals("111691272636258957769", r.getValue(Msmsentry.MSMSENTRY.USERID));
	}

	@Test
	public void testUserID2(){
		Result<Record> results = create.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.ACTION.eq("105877430327962648647"))
				.fetch();

		for(Record r : results)
			assertEquals("105877430327962648647", r.getValue(Msmsentry.MSMSENTRY.USERID));
	}
}
