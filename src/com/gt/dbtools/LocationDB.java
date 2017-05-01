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
import com.gt.pojo.LocationData;

public class LocationDB {
	private static final String LOG_TAG = "LocationDB";
	private DBProvider provider;
	private Connection connection;
	private Statement statement;
	int AutoID = 0;
	private MembersDB membersDB;

	public LocationDB() {
		provider = new DBProvider();
	}

	/**
	 * Add new Location of given member id and return the auto-generated keys
	 * 
	 * @param locationData
	 * @param memberid
	 * @return
	 */
	public int addNewLocation(LocationData locationData) {
		try {
			String query = "INSERT INTO "
					+ DBConfig.getLocationTable()
					+ " (`memberid`,`latitude`,`longitude`,`place`,`extrainfo`,`username`,`distance`,`date`,`direction`,`accuracy`,`phoneumber`,`speed`,`locationmethod`) VALUES( "
					+ locationData.getMemberid() + " , '" + locationData.getLatitude() + "' , '"
					+ locationData.getLongitude() + "' , '" + locationData.getPlace() + "' , '"
					+ locationData.getExtrainfo() + "' , '" + locationData.getUsername() + "' , '"
					+ locationData.getDistance() + "' , '" + locationData.getDate() + "' , '"
					+ locationData.getDirection() + "' , '" + locationData.getAccuaracy() + "' , '"
					+ locationData.getPhonenumber() + "' , '" + locationData.getSpeed() + "' , '"
					+ locationData.getLocationMethod() + "');";

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
				LogUtility.LogInfo("Location Added of Member " + locationData.getMemberid(),
						LOG_TAG);

			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Adding Location of : " + locationData.getMemberid()
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Adding Location of : " + locationData.getMemberid()
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError(
						"Error While Adding Location of : " + locationData.getMemberid()
								+ " Error : " + e, LOG_TAG);
			}

		}
		return AutoID;
	}

	/**
	 * Update location data of given user name
	 * 
	 * @param locationData
	 * @param username
	 * @return
	 */
	public boolean updateLocationByUsername(LocationData locationData, String username) {
		try {
			String query = "UPDATE " + DBConfig.getLocationTable() + " SET `LATITUDE` = '"
					+ locationData.getLatitude() + " ',`LONGITUDE` = '"
					+ locationData.getLongitude() + "' , `PLACE` = '" + locationData.getPlace()
					+ "' ,`EXTRAINFO` = '" + locationData.getExtrainfo() + "', `DISTANCE` = '"
					+ locationData.getDistance() + "' , `DATE` = '" + locationData.getDate()
					+ "' , `DIRECTION` = '" + locationData.getDirection() + "',`ACCURACY` = '"
					+ locationData.getAccuaracy() + "' , `PHONEUMBER` = '"
					+ locationData.getPhonenumber() + "',`SPEED` = '" + locationData.getSpeed()
					+ "', `LOCATIONMETHOD` = '" + locationData.getLocationMethod()
					+ "' WHERE `USERNAME` = '" + username + "';";

			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			int rowCount = statement.executeUpdate(query);
			if (rowCount > 0) {
				return true;
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError(
					"Error While Updating Location Data of : " + locationData.getUsername()
							+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError(
					"Error While Update  Location Data of : " + locationData.getUsername()
							+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError(
						"Error While Update  Location Data of : " + locationData.getUsername()
								+ " Error : " + e, LOG_TAG);
			}

		}
		return false;
	}

	/**
	 * Update the location data of given memberid
	 * 
	 * @param locationData
	 * @param memberid
	 * @return
	 */
	public synchronized boolean updateLocationByMemberID(LocationData locationData, int memberid) {
		try {
			String query = "UPDATE " + DBConfig.getLocationTable() + " SET `LATITUDE` = '"
					+ locationData.getLatitude() + " ',`LONGITUDE` = '"
					+ locationData.getLongitude() + "' , `PLACE` = '" + locationData.getPlace()
					+ "' ,`EXTRAINFO` = '" + locationData.getExtrainfo() + "', `DISTANCE` = '"
					+ locationData.getDistance() + "' , `DATE` = '" + locationData.getDate()
					+ "' , `DIRECTION` = '" + locationData.getDirection() + "',`ACCURACY` = '"
					+ locationData.getAccuaracy() + "' , `SPEED` = '" + locationData.getSpeed()
					+ "', `LOCATIONMETHOD` = '" + locationData.getLocationMethod()
					+ "' WHERE `MEMBERID` = '" + memberid + "';";

			if (provider == null) {
				provider = new DBProvider();
			}
			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			int rowCount = statement.executeUpdate(query);
			if (rowCount > 0) {
				return true;
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Adding Location of : " + locationData.getUsername()
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Adding Location of : " + locationData.getUsername()
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Adding Location of : " + e, LOG_TAG);
			}

		}
		return false;
	}

	/**
	 * Retrieve the location data of given member id
	 * 
	 * @param memberid
	 */
	public LocationData getLocationByMemberID(int memberid) {
		LocationData location = null;
		try {
			location = new LocationData();
			String query = "SELECT * FROM " + DBConfig.getLocationTable() + " WHERE MEMBERID= "
					+ memberid;
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

			if (resultSet.next()) {
				location.setIdlocation(resultSet.getInt(1));
				location.setMemberid(resultSet.getInt(2));
				location.setLongitude(resultSet.getString(3));
				location.setLatitude(resultSet.getString(4));
				location.setPlace(resultSet.getString(5));
				location.setExtrainfo(resultSet.getString(6));
				location.setUsername(resultSet.getString(7));
				location.setDistance(resultSet.getString(8));
				location.setDate(resultSet.getString(9));
				location.setDirection(resultSet.getString(10));
				location.setAccuaracy(resultSet.getString(11));
				location.setPhonenumber(resultSet.getString(12));
				location.setSpeed(resultSet.getString(13));
				location.setLocationMethod(resultSet.getString(14));
				LogUtility.LogInfo("Got Location : " + location, LOG_TAG);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Location of member id :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Location of member id :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting Location of member id :" + memberid
						+ " Error : " + e, LOG_TAG);
			}

		}
		return location;
	}

	/**
	 * Retrieve the location data of given member id and returnt the location
	 * json
	 * 
	 * @param memberid
	 */
	public JsonObject getLocationJSONByMemberID(int memberid) {
		JsonObject locationJson = null;
		try {
			String query = "SELECT * FROM " + DBConfig.getLocationTable() + " WHERE MEMBERID= "
					+ memberid;
			if (provider == null) {
				provider = new DBProvider();
			}

			membersDB = new MembersDB();
			// Initialize the connection and statement
			if (connection == null) {
				connection = provider.getConnection();
			}
			statement = connection.createStatement();

			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery(query);

			// Check the result row count if greater than 0 means lecture
			// details are found. Set the success flag to true.
			// Check if resultset has a row

			if (resultSet.next()) {
				locationJson = new JsonObject();
				locationJson.addProperty("locationid", resultSet.getInt(1));
				locationJson.addProperty("memberid", resultSet.getInt(2));
				locationJson.addProperty("latitude", resultSet.getString(3));
				locationJson.addProperty("longitude", resultSet.getString(4));
				locationJson.addProperty("place", resultSet.getString(5));
				locationJson.addProperty("extrainfo", resultSet.getString(6));
				locationJson.addProperty("username", resultSet.getString(7));
				locationJson.addProperty("distance", resultSet.getString(8));
				locationJson.addProperty("date", resultSet.getString(9));
				locationJson.addProperty("direction", resultSet.getString(10));
				locationJson.addProperty("accuarcy", resultSet.getString(11));
				locationJson.addProperty("phone", resultSet.getString(12));
				locationJson.addProperty("speed", resultSet.getString(13));
				locationJson.addProperty("locationMethod", resultSet.getString(14));

				// Get the member picture
				String picture = membersDB.getMemberPicByID(memberid);
				locationJson.addProperty("markerpic", picture);
			}

		}
		catch (SQLException e) {
			LogUtility.LogError("Error While Getting Location of member id :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Location of member id :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Location of member id :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting All Member:  Error : " + e.getMessage(),
						LOG_TAG);
			}

		}

		return locationJson;
	}

	/**
	 * Retrieve the location data of all members json
	 * 
	 * @param memberid
	 */
	public JsonArray getAllMembersLocationJSON() {
		JsonArray locationArrayJson = null;
		try {
			String query = "SELECT * FROM " + DBConfig.getLocationTable() + ";";
			if (provider == null) {
				provider = new DBProvider();
			}

			membersDB = new MembersDB();
			// Initialize the connection and statement
			if (connection == null) {
				connection = provider.getConnection();
			}
			statement = connection.createStatement();

			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery(query);

			// Check the result row count if greater than 0 means lecture
			// details are found. Set the success flag to true.
			// Check if resultset has a row
			locationArrayJson = new JsonArray();
			while (resultSet.next()) {
				JsonObject locationJson = new JsonObject();
				locationJson.addProperty("locationid", resultSet.getInt(1));
				int memberid = resultSet.getInt(2);
				locationJson.addProperty("memberid", memberid);
				locationJson.addProperty("latitude", resultSet.getString(3));
				locationJson.addProperty("longitude", resultSet.getString(4));
				locationJson.addProperty("place", resultSet.getString(5));
				locationJson.addProperty("extrainfo", resultSet.getString(6));
				locationJson.addProperty("username", resultSet.getString(7));
				locationJson.addProperty("distance", resultSet.getString(8));
				locationJson.addProperty("date", resultSet.getString(9));
				locationJson.addProperty("direction", resultSet.getString(10));
				locationJson.addProperty("accuarcy", resultSet.getString(11));
				locationJson.addProperty("phone", resultSet.getString(12));
				locationJson.addProperty("speed", resultSet.getString(13));
				locationJson.addProperty("locationMethod", resultSet.getString(14));

				// Get the member picture
				String picture = membersDB.getMemberPicByID(memberid);
				locationJson.addProperty("markerpic", picture);
				locationArrayJson.add(locationJson);

			}

		}
		catch (SQLException e) {
			LogUtility.LogError("Error While Getting All Members Location :" + " Error : " + e,
					LOG_TAG);
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting All Members Location :" + " Error : " + e,
					LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting All Members Location :" + " Error : " + e,
					LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting All Members Location :" + " Error : " + e,
						LOG_TAG);
			}

		}

		return locationArrayJson;
	}

	/**
	 * Retrieve the location data of given user name
	 * 
	 * @param username
	 * @return
	 */
	public LocationData getLocationByUserName(String username) {
		LocationData location = null;
		try {
			location = new LocationData();
			String query = "SELECT * FROM " + DBConfig.getLocationTable() + " WHERE USERNAME= "
					+ username;
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

			if (resultSet.next()) {
				location.setIdlocation(resultSet.getInt(1));
				location.setMemberid(resultSet.getInt(2));
				location.setLongitude(resultSet.getString(3));
				location.setLatitude(resultSet.getString(4));
				location.setPlace(resultSet.getString(5));
				location.setExtrainfo(resultSet.getString(6));
				location.setUsername(resultSet.getString(7));
				location.setDistance(resultSet.getString(8));
				location.setDate(resultSet.getString(9));
				location.setDirection(resultSet.getString(10));
				location.setAccuaracy(resultSet.getString(11));
				location.setPhonenumber(resultSet.getString(12));
				location.setSpeed(resultSet.getString(13));
				location.setLocationMethod(resultSet.getString(14));

				LogUtility.LogInfo("Got Location : " + location, LOG_TAG);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Location By user name :" + username
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Location By user name :" + username
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting Location By user name :" + username
						+ " Error : " + e, LOG_TAG);
			}

		}
		return location;
	}

	/**
	 * Retrieve the latitude and Longitude location data of given user name
	 * 
	 * @param username
	 * @return
	 */
	public LocationData getLatLong(String username) {
		LocationData location = null;
		try {
			location = new LocationData();
			String query = "SELECT LATITUDE, LONGITUDE FROM " + DBConfig.getLocationTable()
					+ " WHERE USERNAME= '" + username + "';";
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

			if (resultSet.next()) {
				location.setLongitude(resultSet.getString(1));
				location.setLatitude(resultSet.getString(2));
				LogUtility.LogInfo("Got Location : " + location, LOG_TAG);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting latitude longitude By user name :" + username
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting latitude longitude By user name :" + username
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting latitude longitude By user name :"
						+ username + " Error : " + e, LOG_TAG);
			}

		}
		return location;
	}

	/**
	 * Retrieve the latitude and Longitude location data of given memberid
	 * 
	 * @param username
	 * @return
	 */
	public LocationData getLatLongMemberID(int memberid) {
		LocationData location = null;
		try {
			location = new LocationData();
			String query = "SELECT LATITUDE, LONGITUDE FROM " + DBConfig.getLocationTable()
					+ " WHERE IDMEMBERS= " + memberid;
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

			if (resultSet.next()) {
				location.setLongitude(resultSet.getString(1));
				location.setLatitude(resultSet.getString(2));
				LogUtility.LogInfo("Got Location : " + location, LOG_TAG);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting latitude longitude By memberid :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting latitude longitude By memberid :" + memberid
					+ " Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting latitude longitude By memberid :"
						+ memberid + " Error : " + e, LOG_TAG);
			}
			finally {
			}

		}
		return location;
	}

}
