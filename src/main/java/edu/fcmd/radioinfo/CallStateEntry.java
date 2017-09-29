package edu.fcmd.radioinfo;

import static edu.generated.rel.tables.Callstateentry.CALLSTATEENTRY;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

public class CallStateEntry implements EntryTable{

	protected static Logger logger;
	protected Connection connection = null;
	
	public CallStateEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(CallStateEntry.class);
	}
	
	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("callstateentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(20))
		
		.constraint(DSL.constraint("PK_CALLSTATEENTRY").primaryKey("NAMEID"))
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

		dslContext.insertInto(CALLSTATEENTRY, CALLSTATEENTRY.NAMEID,
				CALLSTATEENTRY.TIME_STAMP, CALLSTATEENTRY.USERID,
				CALLSTATEENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
}
