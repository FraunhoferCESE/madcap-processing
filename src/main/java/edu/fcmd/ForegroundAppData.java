package edu.fcmd;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static edu.fcmd.generated.tables.Foregroundappentry.FOREGROUNDAPPENTRY;


public class ForegroundAppData {

	static Logger logger;

	Connection connection = null;

	public ForegroundAppData(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(MainDatabaseClass.class);
	}

	public void createTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("foregroundappentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("ACCURACY", SQLDataType.INTEGER.nullable(false))
		.column("APPPACKAGE", SQLDataType.VARCHAR(50).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("EVENTTYPE", SQLDataType.VARCHAR(2).nullable(false))
		.constraint(DSL.constraint("PK_FOREGROUNDAPPENTRY").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void indexForegroundApp(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		/*
		 * MySQL does not support createIndexIfNotExists(). 
		 * This method creates an index on the required column. If the index already exists, the exception is caught and understood that the index already exists. 
		 */

		//		try{
		//			logger.debug("Indexing NAMEID");
		//			dslContext.createIndex("INDEX_NAMEID").on(Foregroundbackgroundentry.FOREGROUNDBACKGROUNDENTRY, Foregroundbackgroundentry.FOREGROUNDBACKGROUNDENTRY.NAMEID).execute();
		//			logger.debug("NAMEID indexed");
		//		}catch (DataAccessException sqlException){
		//			logger.error("ERROR: "+sqlException.getMessage());
		//			logger.debug("NAMEID already indexed");
		//		} //NAMEID is already indexed as PRIMARY KEY.


		try{
			logger.debug("Indexing ACCURACY");
			dslContext.createIndex("INDEX_ACCURACY").on(FOREGROUNDAPPENTRY, FOREGROUNDAPPENTRY.ACCURACY).execute();
			logger.debug("ACCURACY indexed");
		}catch (DataAccessException sqlException){
			//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("ACCURACY already indexed");
		}
		try{
			logger.debug("Indexing EVENTTYPE");
			dslContext.createIndex("INDEX_EVENTTYPE").on(FOREGROUNDAPPENTRY, FOREGROUNDAPPENTRY.EVENTTYPE).execute();
			logger.debug("EVENTTYPE indexed");
		}catch (DataAccessException sqlException){
			//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("EVENTTYPE already indexed");
		}
		try{
			logger.debug("Indexing ACTION");
			dslContext.createIndex("INDEX_TIMESTAMP").on(FOREGROUNDAPPENTRY, FOREGROUNDAPPENTRY.TIME_STAMP).execute();
			logger.debug("NAMEID indexed");
		}catch (DataAccessException sqlException){
			//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("NAMEID already indexed");
		}
		try{
			logger.debug("Indexing USERID");
			dslContext.createIndex("INDEX_USERID").on(FOREGROUNDAPPENTRY, FOREGROUNDAPPENTRY.USERID).execute();
			logger.debug("USERID indexed");
		}catch (DataAccessException sqlException){
			//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("USERID already indexed");
		}
		dslContext.close();
	}

	public void insertIntoAll(String nameid, Integer accuracy, String appPackage, String timestamp, String userID, String eventType){

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(FOREGROUNDAPPENTRY,FOREGROUNDAPPENTRY.NAMEID,
				FOREGROUNDAPPENTRY.ACCURACY, FOREGROUNDAPPENTRY.APPPACKAGE,
				FOREGROUNDAPPENTRY.TIME_STAMP, FOREGROUNDAPPENTRY.USERID,
				FOREGROUNDAPPENTRY.EVENTTYPE)
		.values(nameid, accuracy, appPackage, new Timestamp(Long.parseLong(timestamp)), userID, eventType)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}

	public void dropTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.dropTable("foregroundappentry").execute();
		dslContext.close();
	}

	public void truncateTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.truncate("foregroundappentry").execute();

		dslContext.close();
	}

	public Result<Record3<String, String, Timestamp>> getAppsByUserOnTime(String userID, Timestamp startTime, Timestamp endTime){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		if(startTime == null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime != null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.greaterOrEqual(startTime))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime == null && endTime !=null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.lessOrEqual(endTime))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
				.from(FOREGROUNDAPPENTRY)
				.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
				.and(FOREGROUNDAPPENTRY.TIME_STAMP.between(startTime, endTime))
				.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
				.fetch();
	}
	
	public Result<Record3<String, String, Timestamp>> getAppsByUserOnTime(String userID, String startTime, String endTime){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		if(startTime == null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime != null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.like(startTime+"%"))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime == null && endTime !=null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.like(endTime+"%"))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
				.from(FOREGROUNDAPPENTRY)
				.where(FOREGROUNDAPPENTRY.USERID.eq(userID))
				.and(FOREGROUNDAPPENTRY.TIME_STAMP.between(Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)))
				.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
				.fetch();
	}
	
	public Result<Record3<String, String, Timestamp>> getAppsByUser(String userid, String startTime, String endTime){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		if(startTime == null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userid))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime != null && endTime == null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userid))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.like(startTime+"%"))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		if(startTime == null && endTime !=null){
			return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
					.from(FOREGROUNDAPPENTRY)
					.where(FOREGROUNDAPPENTRY.USERID.eq(userid))
					.and(FOREGROUNDAPPENTRY.TIME_STAMP.like(endTime+"%"))
					.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
					.fetch();
		}
		return dslContext.select(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE, FOREGROUNDAPPENTRY.TIME_STAMP)
				.from(FOREGROUNDAPPENTRY)
				.where(FOREGROUNDAPPENTRY.USERID.eq(userid))
				.and(FOREGROUNDAPPENTRY.TIME_STAMP.between(Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)))
				.orderBy(FOREGROUNDAPPENTRY.TIME_STAMP)
				.fetch();
	}
	
	public Result<Record2<String, String>> getAppsOrderByUser(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		
		return dslContext.selectDistinct(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE)
				.from(FOREGROUNDAPPENTRY)
				.groupBy(FOREGROUNDAPPENTRY.USERID, FOREGROUNDAPPENTRY.APPPACKAGE)
				.orderBy(FOREGROUNDAPPENTRY.USERID)
				.fetch();
	}
	
	public Result<Record2<String, Integer>> getNumberOfUsersByApp(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		
		return dslContext.select(FOREGROUNDAPPENTRY.APPPACKAGE, DSL.countDistinct(FOREGROUNDAPPENTRY.USERID))
				.from(FOREGROUNDAPPENTRY)
				.groupBy(FOREGROUNDAPPENTRY.APPPACKAGE)
				.fetch();
	}
}
