package edu.fcmd.radioinfo;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

import static edu.generated.rel.tables.Bluetoothstaticattributesentry.BLUETOOTHSTATICATTRIBUTESENTRY;

public class BluetoothStaticAttributesEntry implements EntryTable{

	protected static Logger logger;
	protected Connection connection = null;
	
	public BluetoothStaticAttributesEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(BluetoothStaticAttributesEntry.class);
	}
	
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("BluetoothStaticAttributesEntry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("ADDRESS", SQLDataType.VARCHAR(20))
		.column("DEVICE_NAME", SQLDataType.VARCHAR(25))
		
		.constraint(DSL.constraint("PK_BLUETOOTHSTATICATTRIBUTEENTRY").primaryKey("NAMEID"))
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
	
	public void insertIfNot(String name, String userID, String timestamp, String address, String dev_name){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(BLUETOOTHSTATICATTRIBUTESENTRY, BLUETOOTHSTATICATTRIBUTESENTRY.NAMEID,
				BLUETOOTHSTATICATTRIBUTESENTRY.TIME_STAMP, BLUETOOTHSTATICATTRIBUTESENTRY.USERID,
				BLUETOOTHSTATICATTRIBUTESENTRY.ADDRESS, BLUETOOTHSTATICATTRIBUTESENTRY.DEVICE_NAME
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				address, dev_name
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
}
