package edu.fcmd.radioinfo;

import static edu.generated.rel.tables.Cellentry.CELLENTRY;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import edu.fcmd.EntryTable;

public class CellEntry implements EntryTable{
	protected static Logger logger;
	protected Connection connection = null;
	
	public CellEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(CellEntry.class);
	}

	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("cellentry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("STATE", SQLDataType.VARCHAR(20))
		
		.constraint(DSL.constraint("PK_CELLENTRY").primaryKey("NAMEID"))
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

		dslContext.insertInto(CELLENTRY, CELLENTRY.NAMEID,
				CELLENTRY.TIME_STAMP, CELLENTRY.USERID,
				CELLENTRY.STATE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				state
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
}
