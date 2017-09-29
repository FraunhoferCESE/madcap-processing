package edu.fcmd;

import java.sql.Timestamp;

public interface EntryTable {
	public void createTableIfNot();
//	public void insertValuesIfNot(String...strings);
//	public void insertIfNot(String name, String userID, String timestamp, String state);
//	public void insertValuesUpdate();
	public void indexTable();
	public void dropTable();
	public void truncateTable();
}
