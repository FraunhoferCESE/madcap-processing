package edu.fcmd.radioinfo;

import java.sql.Connection;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import edu.fcmd.EntryTable;

import static edu.generated.rel.tables.Celllocationentry.CELLLOCATIONENTRY;

public class CellLocationEntry implements EntryTable{
	protected static Logger logger;
	protected Connection connection = null;
	
	public CellLocationEntry(Connection connection){
		this.connection = connection;

		logger = Logger.getLogger(CellLocationEntry.class);
	}

	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("CellLocationEntry")
		.column("NAMEID", SQLDataType.VARCHAR(40).nullable(false))
		.column("USERID", SQLDataType.VARCHAR(25).nullable(false))
		.column("TIME_STAMP", SQLDataType.TIMESTAMP.nullable(false))
		
		.column("AREA_CODE", SQLDataType.VARCHAR(10))
		.column("CELL_TYPE", SQLDataType.VARCHAR(5))
		
		.constraint(DSL.constraint("PK_CELLLOCATIONENTRY").primaryKey("NAMEID"))
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

	public void insertIfNot(String name, String userID, String timestamp, String area_code, String cell_type){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(CELLLOCATIONENTRY, CELLLOCATIONENTRY.NAMEID,
				CELLLOCATIONENTRY.TIME_STAMP, CELLLOCATIONENTRY.USERID,
				CELLLOCATIONENTRY.AREA_CODE, CELLLOCATIONENTRY.CELL_TYPE
				)
		.values(name, new Timestamp(Long.parseLong(timestamp)), userID,
				area_code, cell_type
				)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();
	}
}
