package edu.fcmd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

//import static edu.fcmd.generated.tables.Msmsentry.MSMSENTRY;
import static edu.fcmd.generated.rel.tables.Msmsentry.MSMSENTRY;


public class MSMSData {

	static Logger logger;

	Connection connection = null;

	public MSMSData(Connection connection){
		this.connection = connection;
		logger = Logger.getLogger(MSMSData.class);
	}

	public void createTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		
		dslContext.createTableIfNotExists("msmsentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("ACTION", SQLDataType.VARCHAR(25).nullable(false))
		.column("EXTRA", SQLDataType.VARCHAR(50))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.constraint(DSL.constraint("PK_MSMSENTRY").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void insertIntoTable(String nameid, String action, String extra, String timestamp, String userID) throws SQLException{

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		dslContext.insertInto(MSMSENTRY, MSMSENTRY.NAMEID, MSMSENTRY.ACTION, MSMSENTRY.EXTRA, MSMSENTRY.TIME_STAMP, MSMSENTRY.USERID)
		.values(nameid, action, extra, new Timestamp(Long.parseLong(timestamp)), userID)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}

	public void indexMSMS(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

//		try{
//			logger.debug("Indexing NAMEID");
//			dslContext.createIndex("INDEX_NAMEID").on(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.NAMEID).execute();
//			logger.debug("NAMEID indexed");
//		}catch (DataAccessException sqlException){
//			logger.error("ERROR: "+sqlException.getMessage());
//			logger.debug("NAMEID already indexed");
//		} //NAMEID is already indexed as PRIMARY KEY
		
		try{
			logger.debug("Indexing ACTION");
			dslContext.createIndex("INDEX_ACTION").on(MSMSENTRY, MSMSENTRY.ACTION).execute();
			logger.debug("ACTION indexed");
		}catch (DataAccessException sqlException){
//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("ACTION already indexed");
		}
		try{
			logger.debug("Indexing TIME_STAMP");
			dslContext.createIndex("INDEX_TIMESTAMP").on(MSMSENTRY, MSMSENTRY.TIME_STAMP).execute();
			logger.debug("TIME_STAMP indexed");
		}catch (DataAccessException sqlException){
//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("TIME_STAMP already indexed");
		}
		try{
			logger.debug("Indexing USERID");
			dslContext.createIndex("INDEX_USERID").on(MSMSENTRY, MSMSENTRY.USERID).execute();
			logger.debug("USERID indexed");
		}catch (DataAccessException sqlException){
//			logger.error("ERROR: "+sqlException.getMessage());
			logger.debug("USERID already indexed");
		}
		dslContext.close();
	}

	public void dropTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.dropTable(MSMSENTRY).execute();
		dslContext.close();
	}

	public void truncateTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.truncate("msmsentry").execute();

		dslContext.close();
	}

	public Result<Record> queryMSMS(Timestamp startDate, Timestamp endDate){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		if(endDate == null && startDate == null) {
			return dslContext.select().from(MSMSENTRY).fetch();
		}else if(startDate != null && endDate == null){
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.greaterOrEqual(startDate))
					.orderBy(MSMSENTRY.TIME_STAMP)
					.fetch();
		}else if(startDate == null && endDate !=null){
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.lessOrEqual(endDate))
					.orderBy(MSMSENTRY.TIME_STAMP)
					.fetch();
		}else if(endDate.before(startDate)){
			logger.error("end date is before start date");
			return null;
		}else {
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.between(startDate, endDate))
					.orderBy(MSMSENTRY.TIME_STAMP)
					.fetch();
		}
	}

	public Result<Record> queryMSMS(String type){

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		if(type.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")){
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.like(type+"%"))
					.fetch();
		}else if(type.contains("SMS")){
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.ACTION.eq(type))
					.fetch();
		}else{
			return dslContext.select().from(MSMSENTRY)
					.where(MSMSENTRY.USERID.eq(type))
					.fetch();

		}
	}

	/*
	 * Count the number of occurrence of 
	 * @param countOn  - Field from the database schema
	 * @param startDate - Timestamp  - year-1900, MM 00-11, @nullable
	 * @param endDate - Timestamp  - year-1900, MM 00-11, @nullable
	 * 
	 * returns Record3<String, String, Integer> type object with MSMSENTRY.USERID, MSMSENTRY.ACTION and count
	 */
	public Result<Record3<String, String, Integer>> countQueryMSMS(Field<?> countOn, Timestamp startDate, Timestamp endDate){


		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		if(startDate!=null && endDate!=null){
			if(endDate.before(startDate)){
				logger.error("end date is before start date");
				return null;
			}else return dslContext.select(MSMSENTRY.USERID, MSMSENTRY.ACTION, DSL.count(countOn))
					.from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.between(startDate,endDate))
					.groupBy(MSMSENTRY.USERID, MSMSENTRY.ACTION)
					.orderBy(MSMSENTRY.USERID)
					.fetch();
		}else if(startDate != null && endDate == null){
			return dslContext.select(MSMSENTRY.USERID, MSMSENTRY.ACTION, DSL.count(countOn))
					.from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.greaterOrEqual(startDate))
					.groupBy(MSMSENTRY.USERID, MSMSENTRY.ACTION)
					.orderBy(MSMSENTRY.USERID)
					.fetch();
		}else if(startDate == null && endDate !=null){
			return dslContext.select(MSMSENTRY.USERID, MSMSENTRY.ACTION, DSL.count(countOn))
					.from(MSMSENTRY)
					.where(MSMSENTRY.TIME_STAMP.lessOrEqual(endDate))
					.groupBy(MSMSENTRY.USERID, MSMSENTRY.ACTION)
					.orderBy(MSMSENTRY.USERID)
					.fetch();
		}else return dslContext.select(MSMSENTRY.USERID, MSMSENTRY.ACTION, DSL.count(countOn))
				.from(MSMSENTRY)
				.groupBy(MSMSENTRY.USERID, MSMSENTRY.ACTION)
				.orderBy(MSMSENTRY.USERID)
				.fetch();
	}
}
