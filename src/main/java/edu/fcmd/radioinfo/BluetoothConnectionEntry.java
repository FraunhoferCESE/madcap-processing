package edu.fcmd.radioinfo;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

import static edu.generated.rel.tables.Bluetoothconenctionentry.BLUETOOTHCONENCTIONENTRY;

public class BluetoothConnectionEntry implements EntryTable{
	protected static Logger logger;
	protected Connection connection = null;

	public BluetoothConnectionEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(BluetoothConnectionEntry.class);
	}
	
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("bluetoothconenctionentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("ADDRESS", SQLDataType.VARCHAR(20))
		.column("DEVICE_NAME", SQLDataType.VARCHAR(25))
		.column("STATE", SQLDataType.VARCHAR(15))
		
		.constraint(DSL.constraint("PK_BLUETOOTHCONNECTIONENTRY").primaryKey("NAMEID"))
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

	public void insertIfNot(String name, String userID, String timestamp, String address, String device_name, String state){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(BLUETOOTHCONENCTIONENTRY,BLUETOOTHCONENCTIONENTRY.NAMEID,
				BLUETOOTHCONENCTIONENTRY.TIME_STAMP, BLUETOOTHCONENCTIONENTRY.USERID,
				BLUETOOTHCONENCTIONENTRY.ADDRESS, BLUETOOTHCONENCTIONENTRY.DEVICE_NAME,
				BLUETOOTHCONENCTIONENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID, 
				address, device_name, state
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();

	}
}
