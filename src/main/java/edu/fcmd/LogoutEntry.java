package edu.fcmd;


import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class LogoutEntry implements EntryTable {
	
	protected static Logger logger;
	protected Connection connection = null;

	public void createTableIfNot() {
		// TODO Auto-generated method stub

	}

	public void insertIfNot(String name, String userID, String timestamp) {
		// TODO Auto-generated method stub

	}

	public void indexTable() {
		// TODO Auto-generated method stub

	}

	public void dropTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

//		dslContext.dropTable(MSMSENTRY).execute();
//		dslContext.close();
	}

	public void truncateTable(){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

//		dslContext.truncate("msmsentry").execute();

		dslContext.close();
	}

}
