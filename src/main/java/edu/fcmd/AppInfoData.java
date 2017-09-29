package edu.fcmd;

import java.io.IOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static edu.generated.rel.tables.Appinfotable.APPINFOTABLE;

public class AppInfoData implements EntryTable{

	private Logger logger;

	private Connection connection;

	public AppInfoData(Connection connection) {
		this.connection = connection;
		logger = Logger.getLogger(AppInfoData.class);
	}

	void addAppNameAndCategory(String packageName){
		logger.debug("Fetching app info from playstore...");

		String playStoreURL = "https://play.google.com/store/apps/details?id=";
		String playstoreLink = playStoreURL+packageName;

		createTableIfNot();


		Elements appName = null, appCategory = null, appDeveloper = null;

		try {
			Document doc = Jsoup.connect(playstoreLink).get();

			appName = doc.select("h1.document-title > div.id-app-title");
			appCategory = doc.select("[itemprop = genre]");
			appDeveloper = doc.select("[itemprop = author]").select("[itemprop = name]");

			insertValuesIfNot(packageName, appName.text(), appCategory.text(), appDeveloper.text());

		} catch (IOException e) {
			logger.error(e.getMessage());
			insertValuesIfNot(packageName, "UNKNOWN", "UNKNOWN", "UNKNOWN");
		}		
	}

	public void insertValuesIfNot(String packageName, String appName, String appCategory, String appDeveloper) {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.insertInto(APPINFOTABLE, APPINFOTABLE.PACKAGE_NAME, APPINFOTABLE.APP_NAME, APPINFOTABLE.CATEGORY, APPINFOTABLE.DEVELOPER)
		.values(packageName, appName, appCategory, appDeveloper)
		.onDuplicateKeyIgnore()
		.execute();

		dslContext.close();

		logger.debug("App info updated");
	}

	int getAppNameAndCategory(String packageName){
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		return dslContext.select(APPINFOTABLE.PACKAGE_NAME)
				.from(APPINFOTABLE)
				.where(APPINFOTABLE.PACKAGE_NAME.eq(packageName))
				.execute();
	}


	public void createTableIfNot() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.createTableIfNotExists("appInfoTable")
		.column("PACKAGE_NAME", SQLDataType.VARCHAR(50).nullable(false))
		.column("APP_NAME", SQLDataType.VARCHAR(50).nullable(false))
		.column("CATEGORY", SQLDataType.VARCHAR(50).nullable(false))
		.column("DEVELOPER", SQLDataType.VARCHAR(50).nullable(false))
		.constraint(DSL.constraint("PK_APPINFOTABLE").primaryKey("PACKAGE_NAME"))
		.execute();

		dslContext.close();
	}

	public void insertValuesUpdate() {
		// TODO Auto-generated method stub

	}

	public void indexTable() {
		// TODO Auto-generated method stub

	}

	public void dropTable() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.dropTable("appInfoTable").execute();

		dslContext.close();

	}

	public void truncateTable() {
		DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

		dslContext.truncate("appInfoTable").execute();

		dslContext.close();
	}

}
