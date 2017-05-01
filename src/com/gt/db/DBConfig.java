/**
 *
 */

package com.gt.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.gt.logs.LogUtility;

/**
 * RegistrationModule com.regmodule.db Apr 22, 2015
 */
public class DBConfig {

	private static final String CLASS_NAME = DBConfig.class.getName();
	// Property Keys
	private static final String JDBC_TYPE = "jdbcName"; // jdbc:mysql:
	private static final String DB_CLASS = "driverClass"; // "com.mysql.jdbc.Driver"
	private static final String DB_PORT = "port"; // 3306
	private static final String DB_IP = "ip"; // localhost
	private static final String DB_NAME = "dbname"; // dbname
	private static final String DB_UID = "userName";
	private static final String DB_PWD = "password";
	private static final String MEMBERS_TABLE = "members";
	private static final String LOCATION_TABLE = "locations";
	private static final String MESSAGES_TABLE = "messages";

	private static final String DB_STRING_SEPRATOR = "//";

	public static final String PROPERTIES_FILE = "db.properties";
	private static final Properties dbProp = new Properties();
	OutputStream output = null;

	private static String jdbc = "";
	private static String dbClass;
	private static String dbPort = "";
	private static String dbIp = "";
	private static String dbName = "";
	private static String dbUid = "";
	private static String dbPwd = "";
	private static String databaseConnection = "";

	// Tables Name
	private static String members = "";
	private static String location = "";
	private static String messages="";
	/**
	 *
	 */
	public DBConfig() {
	}

	/**
	 * Initialize the database configuration file instance
	 */
	public void init() {

		try {

			// File propertiFile = new File(defaultPath);
			InputStream inputStream = DBConfig.class.getResourceAsStream(PROPERTIES_FILE);
			// Read the configuration and set the value
			dbProp.load(inputStream);

			if (dbProp != null) {

				// load properties
				setJdbc(dbProp.getProperty(JDBC_TYPE));
				setDbClass(dbProp.getProperty(DB_CLASS));
				setDbPort(dbProp.getProperty(DB_PORT));
				setDbIp(dbProp.getProperty(DB_IP));
				setDbName(dbProp.getProperty(DB_NAME));
				setDbUid(dbProp.getProperty(DB_UID));
				setDbPwd(dbProp.getProperty(DB_PWD));

				setMembersTable(dbProp.getProperty(MEMBERS_TABLE, "members"));
				setLocationTable(dbProp.getProperty(LOCATION_TABLE, "location"));
				setMessagesTable(dbProp.getProperty(MESSAGES_TABLE, "messages"));
				// Generate connection string from properties

				// jdbc:mysql://localhost:3306/dbname"
				String connectionString = getJdbc() + DB_STRING_SEPRATOR + getDbIp() + ":"
						+ getDbPort() + "/" + getDbName();

				// Set connection String
				setDatabaseConnection(connectionString);

				LogUtility.LogInfo("Connection String : " + connectionString, CLASS_NAME);
				LogUtility.LogInfo("Database Property Initialize Finished Successfully.",
						CLASS_NAME);

			}

		} catch (IOException e) {
			LogUtility.LogError("Error Occurred :" + e.getMessage(), CLASS_NAME);
		} catch (Exception e) {
			LogUtility.LogError("Error Occurred :" + e.getMessage(), CLASS_NAME);
		}
	}

	public static void main(String[] args) {
		DBConfig dbConfig = new DBConfig();
		dbConfig.init();
		System.out.println(DBConfig.getMembersTable());
	}

	public static String getJdbc() {
		return jdbc;
	}

	public static String getDbClass() {
		return dbClass;
	}

	public static String getDbPort() {
		return dbPort;
	}

	public static String getDbIp() {
		return dbIp;
	}

	public static String getDbName() {
		return dbName;
	}

	public static String getDbUid() {
		return dbUid;
	}

	public static String getDbPwd() {
		return dbPwd;
	}

	public static void setJdbc(String jdbc) {
		DBConfig.jdbc = jdbc;
	}

	public static void setDbClass(String dbClass) {
		DBConfig.dbClass = dbClass;
	}

	public static void setDbPort(String dbPort) {
		DBConfig.dbPort = dbPort;
	}

	public static void setDbIp(String dbIp) {
		DBConfig.dbIp = dbIp;
	}

	public static void setDbName(String dbName) {
		DBConfig.dbName = dbName;
	}

	public static void setDbUid(String dbUid) {
		DBConfig.dbUid = dbUid;
	}

	public static void setDbPwd(String dbPwd) {
		DBConfig.dbPwd = dbPwd;
	}

	public static String getDatabaseConnection() {
		return databaseConnection;
	}

	public static void setDatabaseConnection(String databaseConnection) {
		DBConfig.databaseConnection = databaseConnection;
	}

	public static String getMembersTable() {
		return members;
	}

	public static String getLocationTable() {
		return location;
	}

	public static void setMembersTable(String members) {
		DBConfig.members = members;
	}
	
	public static void setLocationTable(String location) {
		DBConfig.location = location;
	}

	public static String getMessagesTable() {
		return messages;
	}

	public static void setMessagesTable(String messages) {
		DBConfig.messages = messages;
	}
}
