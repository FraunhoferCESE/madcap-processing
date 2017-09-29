package edu.fcmd.radioinfo;

import static edu.generated.rel.tables.Nfcentry.NFCENTRY;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

public class NFCEntry implements EntryTable {
	protected static Logger logger;
	protected Connection connection = null;

	public NFCEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(NFCEntry.class);
	}

	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("nfcentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(15))
		
		.constraint(DSL.constraint("PK_NFCENTRY").primaryKey("NAMEID"))
		.execute();

		dslContext.close();
	}

	public void insertIfNot(String name, String userID, String timestamp, String state) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(NFCENTRY, NFCENTRY.NAMEID,
				NFCENTRY.TIME_STAMP, NFCENTRY.USERID,
				NFCENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state
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
