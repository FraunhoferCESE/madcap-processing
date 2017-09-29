package edu.fcmd.radioinfo;

import static edu.generated.rel.tables.Networkentry.NETWORKENTRY;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import edu.fcmd.EntryTable;

public class NetworkEntry implements EntryTable {
	protected static Logger logger;
	protected Connection connection = null;
	
	public NetworkEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(NetworkEntry.class);
	}
	
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("networkentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(20))
		.column("INFO", SQLDataType.VARCHAR(20))
		
		.constraint(DSL.constraint("PK_NETWORKENTRY").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void insertIfNot(String name, String userID, String timestamp, String state, String info) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(NETWORKENTRY, NETWORKENTRY.NAMEID,
				NETWORKENTRY.TIME_STAMP, NETWORKENTRY.USERID,
				NETWORKENTRY.STATE, NETWORKENTRY.INFO
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state, info
				)
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
