package edu.fcmd.systeminfo;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;
import edu.fcmd.LocationEntry;

import static edu.generated.rel.tables.Powerinfotable.POWERINFOTABLE;

public class PowerInfoEntry implements EntryTable{
	Logger logger;

	Connection connection = null;

	public PowerInfoEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(LocationEntry.class);
	}

	public void createTableIfNot(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("powerinfotable")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		.column("HEALTH", SQLDataType.DOUBLE.nullable(true))
		.column("REMAINING_POWER", SQLDataType.DOUBLE.nullable(false))
		.column("VOLTAGE", SQLDataType.DOUBLE.nullable(false))
		.column("TEMPERATURE", SQLDataType.DOUBLE.nullable(false))
		.constraint(DSL.constraint("PK_POWERINFOTABLE").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void insertIntoTable(String name, String timestamp, String userid, double health, double power, double volt, 
			double temperature) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(POWERINFOTABLE,POWERINFOTABLE.NAMEID,
				POWERINFOTABLE.TIME_STAMP, POWERINFOTABLE.USERID,
				POWERINFOTABLE.HEALTH, POWERINFOTABLE.REMAINING_POWER,
				POWERINFOTABLE.VOLTAGE, POWERINFOTABLE.TEMPERATURE)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userid, 
				health, power, volt, temperature)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}

	public void indexTable() {
		// TODO Auto-generated method stub

	}

	public void dropTable() {
		// TODO Auto-generated method stub

	}

	public void truncateTable() {
		// TODO Auto-generated method stub

	}

}