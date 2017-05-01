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
import com.gt.pojo.Member;

/**
 * @version 1.0v
 * @author Rizwan Ahmad Khan
 * 
 */
public class MembersDB {
	private static final String LOG_TAG = "MembersDB";
	private DBProvider provider;
	private Connection connection;
	private Statement statement;
	int AutoID = 0;

	public MembersDB() {
		provider = new DBProvider();
	}

	/**
	 * Add new Member and return the auto-generated keys. It will add default
	 * marker pic to no-picture.png
	 * 
	 * @param member
	 * @return
	 */
	public int addNewMember(Member member) {
		try {
			String defaultMarkerPic = "no-picture.png";
			String query = "INSERT INTO  "
					+ DBConfig.getMembersTable()
					+ "(`fullname`,`age`,`phone`,`email`,`address`,`appid`,`startlongitude`,`startlatituted` , `markerpic`) VALUES ('"
					+ member.getFullname() + "','" + member.getAge() + "','" + member.getPhone()
					+ "','" + member.getEmail() + "','" + member.getAddress() + "','"
					+ member.getAppid() + "','" + member.getStartlongitude() + "','"
					+ member.getStartlatituted() + "','" + defaultMarkerPic + "');";

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
			LogUtility.LogError("Error While Adding Member: " + member.getFullname() + " Error : "
					+ e.getMessage(), LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Adding Member: " + member.getFullname() + " Error : "
					+ e.getMessage(), LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Adding Member: " + member.getFullname()
						+ " Error : " + e.getMessage(), LOG_TAG);
			}

		}
		return AutoID;
	}

	/**
	 * Retrieve the member details of given id and return the member object
	 * 
	 * @param idmember
	 * @return
	 */
	public Member getMemberByID(int idmember) {
		Member members = null;
		try {
			members = new Member();
			String query = "SELECT * FROM " + DBConfig.getMembersTable() + " WHERE IDMEMBERS= "
					+ idmember;
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
				members.setIdmember(resultSet.getInt(1));
				members.setFullname(resultSet.getString(2));
				members.setAge(resultSet.getString(3));
				members.setPhone(resultSet.getString(4));
				members.setEmail(resultSet.getString(5));
				members.setAddress(resultSet.getString(6));
				members.setAppid(resultSet.getString(7));
				members.setStartlongitude(resultSet.getString(8));
				members.setStartlatituted(resultSet.getString(9));
				members.setMarkerpic(resultSet.getString(10));
				LogUtility.LogInfo("Got Member  " + members, LOG_TAG);

			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Member: Error : " + e.getMessage(), LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Member:  Error : " + e.getMessage(), LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting Member:  Error : " + e.getMessage(),
						LOG_TAG);
			}

		}
		return members;
	}

	/**
	 * Retrieve the member registration id of android device of given id and
	 * return the member object
	 * 
	 * @param idmember
	 * @return
	 */
	public String getMemberyAPPID(int idmember) {
		String appid = null;
		try {
			String query = "SELECT APPID FROM " + DBConfig.getMembersTable() + " WHERE IDMEMBERS= "
					+ idmember;
			if (provider == null) {
				provider = new DBProvider();
			}

			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				appid = resultSet.getString(1);
				LogUtility.LogInfo("Got Member APP ID " + appid, LOG_TAG);

			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Member APP ID : Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Member APP ID : Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting Member APP ID : Error : " + e, LOG_TAG);
			}

		}
		return appid;
	}

	/**
	 * Retrieve all the members from data
	 * 
	 * @return
	 */
	public JsonArray getAllMember() {
		JsonArray jsonArray = new JsonArray();
		try {
			String query = "SELECT * FROM " + DBConfig.getMembersTable() + ";";
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
				jsonObject.addProperty("id", resultSet.getInt(1));
				jsonObject.addProperty("fullname", resultSet.getString(2));
				jsonObject.addProperty("age", resultSet.getString(3));
				jsonObject.addProperty("phone", resultSet.getString(4));
				jsonObject.addProperty("email", resultSet.getString(5));
				jsonObject.addProperty("address", resultSet.getString(6));
				jsonObject.addProperty("appid", resultSet.getString(7));
				jsonObject.addProperty("longitude", resultSet.getString(8));
				jsonObject.addProperty("latitude", resultSet.getString(9));
				jsonObject.addProperty("markerpic", resultSet.getString(10));
				jsonArray.add(jsonObject);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting All Member: Error : " + e.getMessage(),
					LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting All Member:  Error : " + e.getMessage(),
					LOG_TAG);
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
		return jsonArray;
	}

	/**
	 * Update Member pic
	 * 
	 * @param id
	 * @param fileName
	 */
	public boolean updateMemberPic(int id, String fileName) {
		try {
			String query = "UPDATE  " + DBConfig.getMembersTable() + " SET `markerpic`='"
					+ fileName + "' WHERE `idmembers`='" + id + "'";
			if (provider == null) {
				provider = new DBProvider();
			}

			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			int row = statement.executeUpdate(query);
			if (row > 0) {
				LogUtility.LogInfo("Sucessfully Updated Member Pic To  " + fileName + " of ID "
						+ id, LOG_TAG);
				return true;
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Updating Member Picr: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Updating Member Picr: Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Updating Member Picr: Error : " + e, LOG_TAG);
			}

		}
		return false;
	}

	/**
	 * Retrieve the member picture of give member id
	 * 
	 * @param id
	 * @return
	 */
	public String getMemberPicByID(int id) {

		// Set default markerPicture if not found we return this
		String picture = "no-picture.png";
		try {
			String query = "SELECT markerpic FROM  " + DBConfig.getMembersTable()
					+ " WHERE  idmembers='" + id + "'";

			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				picture = resultSet.getString(1);
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Getting Member Picture: Error : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Getting Member Picture: Error : " + e, LOG_TAG);
		}
		finally {
			try {
				if (connection != null || !connection.isClosed()) {
					connection.close();
				}
			}
			catch (SQLException e) {
				LogUtility.LogError("Error While Getting Member Picture: Error : " + e, LOG_TAG);
			}

		}
		return picture;
	}

	public static void main(String[] args) {
		DBConfig dbConfig = new DBConfig();
		dbConfig.init();

		Member members = new Member();
		members.setFullname("Rizwan Khan");
		members.setAddress("Nagpur");
		members.setAge("32");
		members.setEmail("rizwan.khan@techmomentous.com");
		members.setPhone("9673615896");
		members.setAppid("APKdsgadjgqoeghogsdjlkahgodih");

		MembersDB membersDB = new MembersDB();
		System.out.println(membersDB.addNewMember(members));

	}
}
