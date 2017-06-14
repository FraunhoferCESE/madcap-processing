package edu.fcmd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.jooq.Constraint;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.fcmd.generated.tables.Msmsentry;

public class MSMSData {

	static Logger logger;

	static Connection connection = null;
	static Statement statement = null;

	public MSMSData(Connection connection, Statement statement){
		MSMSData.connection = connection;
		MSMSData.statement = statement;

		logger = LoggerFactory.getLogger(MainDatabaseClass.class);
	}

	public void createTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		
		dslContext.createTableIfNotExists("msmsentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("ACTION", SQLDataType.VARCHAR(25).nullable(false))
		.column("EXTRA", SQLDataType.VARCHAR(50))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP)
		.column("USERID", SQLDataType.VARCHAR(25))
		.constraint(DSL.constraint("PK_MSMSENTRY").primaryKey(Msmsentry.MSMSENTRY.NAMEID))
		.execute();

		dslContext.close();
	}

	public void insertIntoAll(String nameid, String action, String extra, String timestamp, String userID) throws SQLException{

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		dslContext.insertInto(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.NAMEID, Msmsentry.MSMSENTRY.ACTION, Msmsentry.MSMSENTRY.EXTRA, Msmsentry.MSMSENTRY.TIME_STAMP, Msmsentry.MSMSENTRY.USERID)
		.values(nameid, action, extra, new Timestamp(Long.parseLong(timestamp)), userID)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
	
	public void indexMSMS(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		dslContext.createIndex("INDEX_NAMEID").on(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.NAMEID).execute();
		dslContext.createIndex("INDEX_ACTION").on(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.ACTION).execute();
		dslContext.createIndex("INDEX_TIMESTAMP").on(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.TIME_STAMP).execute();
		dslContext.createIndex("INDEX_USERID").on(Msmsentry.MSMSENTRY, Msmsentry.MSMSENTRY.USERID).execute();
		
		dslContext.close();
	}

	public void dropTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		
		dslContext.dropTable(Msmsentry.MSMSENTRY).execute();
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
			return dslContext.select().from(Msmsentry.MSMSENTRY).fetch();
		}else if(startDate != null && endDate == null){
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.greaterOrEqual(startDate))
					.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
					.fetch();
		}else if(startDate == null && endDate !=null){
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.lessOrEqual(endDate))
					.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
					.fetch();
		}else if(endDate.before(startDate)){
			logger.error("end date is before start date");
			return null;
		}else {
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.between(startDate, endDate))
					.orderBy(Msmsentry.MSMSENTRY.TIME_STAMP)
					.fetch();
		}
	}

	public Result<Record> queryMSMS(String type){

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		if(type.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")){
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.like(type+"%"))
					.fetch();
		}else if(type.contains("SMS")){
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.ACTION.eq(type))
					.fetch();
		}else{
			return dslContext.select().from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.USERID.eq(type))
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
			}else return dslContext.select(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION, DSL.count(countOn))
					.from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.between(startDate,endDate))
					.groupBy(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION)
					.orderBy(Msmsentry.MSMSENTRY.USERID)
					.fetch();
		}else if(startDate != null && endDate == null){
			return dslContext.select(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION, DSL.count(countOn))
					.from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.greaterOrEqual(startDate))
					.groupBy(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION)
					.orderBy(Msmsentry.MSMSENTRY.USERID)
					.fetch();
		}else if(startDate == null && endDate !=null){
			return dslContext.select(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION, DSL.count(countOn))
					.from(Msmsentry.MSMSENTRY)
					.where(Msmsentry.MSMSENTRY.TIME_STAMP.lessOrEqual(endDate))
					.groupBy(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION)
					.orderBy(Msmsentry.MSMSENTRY.USERID)
					.fetch();
		}else return dslContext.select(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION, DSL.count(countOn))
				.from(Msmsentry.MSMSENTRY)
				.groupBy(Msmsentry.MSMSENTRY.USERID, Msmsentry.MSMSENTRY.ACTION)
				.orderBy(Msmsentry.MSMSENTRY.USERID)
				.fetch();
	}
}
