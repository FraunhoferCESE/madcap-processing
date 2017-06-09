package edu.fcmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.fcmd.generated.tables.Msmsentry;

public class MSMSData {
	
	static Logger logger;

	static Connection connection = null;
	static Statement statement = null;

	static DSLContext dslContext;

	public MSMSData(Connection connection, Statement statement){
		MSMSData.connection = connection;
		MSMSData.statement = statement;

		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
		dslContext = DSL.using(connection, SQLDialect.MYSQL);
	}

	public int createTable() throws SQLException{
		int result = -1;
		String query = "CREATE TABLE IF NOT EXISTS madcap.msmsentry ("+
				"nameid VARCHAR(100) NOT NULL,"+
				"action VARCHAR(100) NOT NULL,"+
				"extra VARCHAR(100),"+
				"time_stamp DATETIME,"+
				"userID VARCHAR(25) NOT NULL,"+
				"PRIMARY KEY (nameid));";
		return statement.executeUpdate(query);
	}	

	public void insertIntoAll(String nameid, String action, String extra, String timestamp, String userID) throws SQLException{
		String query = "insert ignore into madcap.msmsentry values (\"" + nameid +"\",\""
				+ action + "\",\""
				+ extra + "\","
				+ "from_unixtime("+timestamp +"),\""
				+ userID +"\");";
		
		statement.executeUpdate(query);

		//		System.out.println("query: \n" +query);
		
		
//		dslContext.insertInto(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.NAMEID, Msmsentry.MSMSENTRY.ACTION, Msmsentry.MSMSENTRY.EXTRA, Msmsentry.MSMSENTRY.TIME_STAMP, Msmsentry.MSMSENTRY.USERID)
//		.values(nameid, action, extra, new Timestamp(Long.parseLong(timestamp)), userID)
//		.onDuplicateKeyIgnore();
//		logger.info("row added.");
				
//		return statement.executeUpdate(query);
	}

	public ResultSet selectFromTable(String columns, String where, String condition) throws SQLException{
		String q= "select "+ columns +" from madcap.msmsentry";
		if(where!=null) q = q + " where "+condition;
		q = q+";";
		System.out.println(q);
		return statement.executeQuery(q);
	}

	public static Result<Record> queryMSMS(Timestamp startDate, Timestamp endDate){

		if(endDate == null || startDate == endDate) {
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.eq(startDate))
					.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
					.fetch();
		}else if(endDate.before(startDate)){
			logger.error("end date is before start date");
			return null;
		}else{

			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.between(startDate, endDate))
					.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
					.fetch();
		}
	}

	public static Result<Record> queryMSMS(String type){
		if(type.contains("SMS")){
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.ACTION.eq(type))
					.fetch();
		}else{
		return dslContext.select().from(Msmsentry.MSMSENTRY)
				.where(Msmsentry.MSMSENTRY.USERID.eq(type))
				.fetch();

		}
	}
}
