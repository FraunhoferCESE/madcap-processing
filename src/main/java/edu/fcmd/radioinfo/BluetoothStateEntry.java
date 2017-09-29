package edu.fcmd.radioinfo;

import static edu.generated.rel.tables.Bluetoothstateentry.BLUETOOTHSTATEENTRY;


import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

public class BluetoothStateEntry implements EntryTable{

	protected static Logger logger;
	protected Connection connection = null;
	
	public BluetoothStateEntry(Connection connection) {
		this.connection = connection;

		logger = Logger.getLogger(BluetoothStateEntry.class);
	}

	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("bluetoothstateentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(15))
		
		.constraint(DSL.constraint("PK_BULETOOTHSTATEENTRY").primaryKey("NAMEID"))
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

		dslContext.insertInto(BLUETOOTHSTATEENTRY, BLUETOOTHSTATEENTRY.NAMEID,
				BLUETOOTHSTATEENTRY.TIME_STAMP, BLUETOOTHSTATEENTRY.USERID,
				BLUETOOTHSTATEENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
}
