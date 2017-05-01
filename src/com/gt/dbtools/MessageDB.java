package com.gt.dbtools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gt.db.DBConfig;
import com.gt.db.DBProvider;
import com.gt.logs.LogUtility;
import com.gt.pojo.PushMessage;

public class MessageDB {
	private static final String LOG_TAG = "MessageDB";
	private DBProvider provider;
	private Connection connection;
	private Statement statement;
	int AutoID = 0;
	private MembersDB membersDB;

	/**
	 * Add new Push Message and return the auto-generated keys.
	 * 
	 * @param member
	 * @return
	 */
	public int addPushMessage(PushMessage message) {
		try {
			String query = "INSERT INTO  " + DBConfig.getMessagesTable()
					+ "(`memberid`,`type`,`message`,`name`,`flagnew`,`time`) VALUES ('"
					+ message.getMemberid() + "','" + message.getType() + "','"
					+ message.getMessage() + "','" + message.getName() + "','"
					+ message.getFlagnew() + "','"+ message.getTime() +"');";

			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			PreparedStatement statement = connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			int affectedRows = statement.executeUpdate();

			if (affectedRows > 0) {
				ResultSet resultSet = statement.getGeneratedKeys();
				resultSet.next();
				AutoID = resultSet.getInt(1);

			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Adding Message: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Adding Message:  Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Adding Message:  Error : " + e, LOG_TAG);
			}

		}
		return AutoID;
	}

	/**
	 * Retrieve All those messages whose flag new is set to 'YES'
	 */
	public JsonArray getAllNewPushMessages() {
		JsonArray jsonArray = new JsonArray();
		try {
			String query = "SELECT * FROM " + DBConfig.getMessagesTable()
					+ " WHERE flagnew = 'YES'";
			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery(query);

			// Check the result row count if greater than 0 means lecture
			// details are found. Set the success flag to true.
			// Check if resultset has a row

			while (resultSet.next()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("idmessages", resultSet.getInt(1));
				jsonObject.addProperty("memberid", resultSet.getInt(2));
				jsonObject.addProperty("type", resultSet.getString(3));
				jsonObject.addProperty("message", resultSet.getString(4));
				jsonObject.addProperty("name", resultSet.getString(5));
				jsonObject.addProperty("flagnew", resultSet.getString(6));
				jsonObject.addProperty("time", resultSet.getString(7));

				jsonArray.add(jsonObject);
			}
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting ALL New Message: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting ALL New Message: Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting ALL New Message: Error : " + e, LOG_TAG);
			}

		}
		return jsonArray;
	}

	/**
	 * Remove the Message of Given ID
	 * 
	 * @param idmessages
	 * @return
	 */
	public int removeMessage(int idmessages) {
		int affectedRows = 0;
		try {
			String query = "DELETE FROM " + DBConfig.getMessagesTable() + " WHERE idmessages ="
					+ idmessages;
			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			affectedRows = statement.executeUpdate();

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Deleting Message: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Deleting Message: Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Deleting Message: Error : " + e, LOG_TAG);
			}

		}
		return affectedRows;
	}

	/**Update The Message Flag of given message id
	 * @param idmessages
	 * @param flag
	 * @return
	 */
	public int updateMessageFlag(int idmessages, String flag) {
		int affectedRows = 0;
		try {
			String query = "UPDATE " + DBConfig.getMessagesTable() + " SET flagnew = '" + flag
					+ "'  WHERE idmessages= " + idmessages;
			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			affectedRows = statement.executeUpdate();
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Updating Message Flag: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Updating Message Flag: Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Updating Message Flag: Error : " + e, LOG_TAG);
			}

		}
		return affectedRows;
	}
}
