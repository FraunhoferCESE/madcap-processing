package edu.fcmd;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static edu.generated.rel.tables.Locationentry.LOCATIONENTRY;

public class LocationEntry implements EntryTable{
	Logger logger;

	Connection connection = null;

	public LocationEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(LocationEntry.class);
	}

	public void createTableIfNot(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("locationentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		.column("ACCURACY", SQLDataType.DOUBLE.nullable(false))
		
		.column("LATITUDE", SQLDataType.DOUBLE.nullable(false))
		.column("LONGITUDE", SQLDataType.DOUBLE.nullable(false))
		.column("BEARING", SQLDataType.DOUBLE.nullable(true))
		.column("ALTITUDE", SQLDataType.DOUBLE.nullable(false))
		.column("ORIGIN", SQLDataType.VARCHAR(10).nullable(false))
		.constraint(DSL.constraint("PK_LOCATIONENTRY").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void insertValuesIfNot(String name, String timestamp, String userid, double accuracy, double longitide, double latitude, 
			double bearing, double altitude, String origin) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(LOCATIONENTRY,LOCATIONENTRY.NAMEID,
				LOCATIONENTRY.ACCURACY, LOCATIONENTRY.TIME_STAMP, LOCATIONENTRY.USERID,
				LOCATIONENTRY.BEARING, LOCATIONENTRY.LONGITUDE,
				LOCATIONENTRY.LATITUDE, LOCATIONENTRY.ALTITUDE,
				LOCATIONENTRY.ORIGIN)
		.values(name, accuracy, new Timestamp(Long.parseLong(timestamp)), userid, 
				bearing, longitide, latitude, altitude, origin)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}

	public void insertValuesUpdate() {
		// TODO Auto-generated method stub

	}

	public void indexTable() {
		// TODO index based on name, timestamp, userid and, origin

	}

	public void dropTable() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.dropTable("locationentry").execute();

		dslContext.close();

	}

	public void truncateTable() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.truncate("locationentry").execute();

		dslContext.close();
	}

}