package com.gt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.gt.logs.LogUtility;

/**
 * RegistrationModule com.regmodule.db Apr 22, 2015
 */
public class DBProvider {
	private static final String CLASS_NAME = DBProvider.class.getName();
	private Connection connection = null;

	/**
	 *
	 */
	public DBProvider() {
	}

	/**
	 * initialize database connection object
	 */
	private void initConnection() {

		try {
			Class.forName(DBConfig.getDbClass());
			Connection connection = DriverManager.getConnection(DBConfig.getDatabaseConnection(),
					DBConfig.getDbUid(), DBConfig.getDbPwd());

			if (connection != null) {
				setConnection(connection);

			}
		}
		catch (SQLException e) {
			LogUtility.LogError("Error Occurred While initializing connection: " + e, CLASS_NAME);
		}
		catch (ClassNotFoundException e) {
			LogUtility.LogError("Error Occurred While initializing connection: " + e, CLASS_NAME);
		}

	}

	/**
	 * Reset Database connection
	 */
	private void resetConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				try {
					connection.close();
					connection = null;
				}
				catch (SQLException e) {

					LogUtility.LogError("Error Occurred While reseting connection  " + e,
							CLASS_NAME);
				}
			}
		}
		catch (SQLException e) {

			LogUtility.LogError("Error Occurred While reseting connection  " + e, CLASS_NAME);
		}
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Return the jdbc connection object Reset the connection and re-initialize
	 * it.
	 * 
	 * @return
	 */
	public Connection getConnection() {
		resetConnection();
		initConnection();
		return connection;
	}

}