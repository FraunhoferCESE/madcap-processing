package edu.fcmd.systeminfo;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import edu.fcmd.EntryTable;

public class SystemInfoEntry implements EntryTable {

	public void createTableIfNot() {
		// TODO Auto-generated method stub

	}

	public void insertIfNot(String name, String userID, String timestamp, String api, String version, String manufacturer, String model) {
		// TODO Auto-generated method stub

	}

	public void indexTable() {
		// TODO Auto-generated method stub

	}

	public void dropTable(){
//		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
//
//		dslContext.dropTable(MSMSENTRY).execute();
//		dslContext.close();
	}

	public void truncateTable(){
//		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
//
//		dslContext.truncate("msmsentry").execute();
//
//		dslContext.close();
	}

}
