package edu.fcmd;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static edu.generated.rel.tables.Physicalactivityentry.PHYSICALACTIVITYENTRY;



public class PhysicalActivityEntry implements EntryTable{

	private Connection connection;
	Logger logger;

	public PhysicalActivityEntry(Connection connection) {
		this.connection = connection;
		logger = Logger.getLogger(PhysicalActivityEntry.class);
	}

	public void createTableIfNot(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		dslContext.createTableIfNotExists("physicalactivityentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("WALKING", SQLDataType.DOUBLE)
		.column("STILL", SQLDataType.DOUBLE)
		.column("TILTING", SQLDataType.DOUBLE)
		.column("IN_VEHICLE", SQLDataType.DOUBLE)
		.column("RUNNING", SQLDataType.DOUBLE)
		.column("ON_FOOT", SQLDataType.DOUBLE)
		.column("ON_BICYCLE", SQLDataType.DOUBLE)
		.column("UNKNOWN", SQLDataType.DOUBLE)
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		.constraint(DSL.constraint("PK_ACTIVITYENTRY").primaryKey("NAMEID"))
		.execute(); 

		dslContext.close();
	}

	public void insertIntoTable(String name, Double walking, Double still, Double tilting, Double vehicle, Double running, 
			Double unknown, Double foot, Double bicycle, String timestamp, String userid){

		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
		dslContext.insertInto(PHYSICALACTIVITYENTRY, 
				PHYSICALACTIVITYENTRY.NAMEID, PHYSICALACTIVITYENTRY.USERID, PHYSICALACTIVITYENTRY.WALKING,
				PHYSICALACTIVITYENTRY.STILL, PHYSICALACTIVITYENTRY.TILTING, PHYSICALACTIVITYENTRY.IN_VEHICLE,
				PHYSICALACTIVITYENTRY.RUNNING, PHYSICALACTIVITYENTRY.ON_FOOT, PHYSICALACTIVITYENTRY.ON_BICYCLE,
				PHYSICALACTIVITYENTRY.UNKNOWN, PHYSICALACTIVITYENTRY.TIME_STAMP)
		.values(name, userid, walking, still, tilting, vehicle, 
				running, foot, bicycle, unknown, new Timestamp(Long.parseLong(timestamp)))
		.onDuplicateKeyIgnore()
		.execute();
	}

	public int getPhysicalActivity(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		return dslContext.select(PHYSICALACTIVITYENTRY.USERID, PHYSICALACTIVITYENTRY.TIME_STAMP,
				DSL.choose(DSL.greatest(PHYSICALACTIVITYENTRY.WALKING, PHYSICALACTIVITYENTRY.RUNNING,
						PHYSICALACTIVITYENTRY.STILL, PHYSICALACTIVITYENTRY.IN_VEHICLE,
						PHYSICALACTIVITYENTRY.ON_BICYCLE, PHYSICALACTIVITYENTRY.ON_FOOT,
						PHYSICALACTIVITYENTRY.UNKNOWN)
						)
				.when(PHYSICALACTIVITYENTRY.WALKING, "Walking")
				.when(PHYSICALACTIVITYENTRY.RUNNING, "Running")
				.when(PHYSICALACTIVITYENTRY.STILL, "Still")
				.when(PHYSICALACTIVITYENTRY.IN_VEHICLE, "In Vehicle")
				.when(PHYSICALACTIVITYENTRY.ON_BICYCLE, "On Bicycle")
				.when(PHYSICALACTIVITYENTRY.ON_FOOT, "On Foot")
				.when(PHYSICALACTIVITYENTRY.UNKNOWN, "Unknown")
				)
				.from(PHYSICALACTIVITYENTRY)
				.orderBy(PHYSICALACTIVITYENTRY.TIME_STAMP)
				.execute();
	}

	public void dropTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.dropTable(PHYSICALACTIVITYENTRY).execute();
		dslContext.close();
	}

	public void truncateTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.truncate("physicalactivityentry").execute();

		dslContext.close();
	}

	public void indexTable() {
		// TODO Auto-generated method stub

	}
}
