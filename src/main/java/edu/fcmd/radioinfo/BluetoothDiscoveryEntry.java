package edu.fcmd.radioinfo;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

import static edu.generated.rel.tables.Bluetoothdiscoveryentry.BLUETOOTHDISCOVERYENTRY;

public class BluetoothDiscoveryEntry implements EntryTable{
	protected static Logger logger;
	protected Connection connection = null;
	
	public BluetoothDiscoveryEntry(Connection connection) {
		this.connection = connection;

		logger = Logger.getLogger(BluetoothDiscoveryEntry.class);
	}

	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("bluetoothdiscoveryentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(15))
		
		.constraint(DSL.constraint("PK_BLUETOOTHDISCOVERYENTRY").primaryKey("NAMEID"))
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
	public void insertIfNot(String name, String userID, String timestamp, String state){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(BLUETOOTHDISCOVERYENTRY, BLUETOOTHDISCOVERYENTRY.NAMEID,
				BLUETOOTHDISCOVERYENTRY.TIME_STAMP, BLUETOOTHDISCOVERYENTRY.USERID,
				BLUETOOTHDISCOVERYENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();

	}
}
