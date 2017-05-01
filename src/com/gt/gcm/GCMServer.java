package com.gt.gcm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.JsonObject;
import com.gt.db.DBConfig;
import com.gt.db.DBProvider;
import com.gt.logs.LogUtility;
import com.gt.pojo.Member;

/**
 * @author Rizwan Ahmad Khan
 *
 */
public class GCMServer {
	public static final String LOG_TAG = "GCMServer";
	public static final String GCM_PROPERTIES_FILE = "GCM.properties";
	private static final String API_KEY = "AIzaSyAF6yMHKAXEqd7DcU4-hTVLIGd_mkyt34A";
	private Result gcmResult = null;
	static final String MESSAGE_KEY = "message";
	private DBProvider provider = null;
	private Connection connection;
	private Statement statement;

	public static void main(String[] args) {
		DBConfig dbConfig = new DBConfig();
		dbConfig.init();
		GCMServer server = new GCMServer();
		server.pushNotification(
				"Testing",
				"APA91bHQkfQnESBjCGdzBhAKNk6i8zZnBrpkpI7d6yDcmEFEA-6W9gdf4jRBhbPbwkM8PZChveM1lTXYhw93oqxewFpJsNyVhGEQ7I2-5SVI2uRwODf-gfBvOM7MRoTS9KzioQGOew63");
	}

	/**
	 * Registered member APP and save the registration id.
	 * <p>
	 * This will update the existing app registration id of specified student
	 * 
	 * @param student
	 */
	@SuppressWarnings("deprecation")
	public String registeredAndroidDevice(Member member) {
		String result = "";
		try {
			provider = new DBProvider();
			String query = " UPDATE " + DBConfig.getMembersTable() + " SET APPID  = '"
					+ member.getAppid() + "' WHERE IDMEMBERS = " + member.getIdmember();

			if (provider == null) {
				provider = new DBProvider();
			}

			// Initialize the connection and statement
			connection = provider.getConnection();
			statement = connection.createStatement();

			// Execute the query and get the result set
			int rowCount = statement.executeUpdate(query);

			if (rowCount > 0) {
				// Updated
				LogUtility.LogInfo(
						"SuccessFully Updated API Key of Member ID :" + member.getIdmember(),
						LOG_TAG);
				
				//Construct the json to send in gcm message
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("messageCode", "105");
				// Send a success notification
				String responseMessage = " You have successfully Registered You Device To Receive Push Notification Of Member : "
						+ member.getFullname();
				jsonObject.addProperty("messageText", responseMessage);
				jsonObject.addProperty("messageDate", new Date().toGMTString());
				
				result = pushNotification(jsonObject.toString(), member.getAppid());

			}
		}
		catch (SQLException e) {
			LogUtility.LogError("Error While Registering Device  : " + e.getMessage(), LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Registering Device  : " + e.getMessage(), LOG_TAG);
		}
		return result;
	}

	/**
	 * Send the push notification to Member APP of specified app id
	 * 
	 * @param responseMessage
	 * @param appID
	 * @return
	 */
	public String pushNotification(String responseMessage, String appID) {
		String gcmResponse = "";
		try {

			Sender sender = new Sender(API_KEY);
			Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true)
					.addData(MESSAGE_KEY, responseMessage).build();
			gcmResult = sender.send(message, appID, 1);

			LogUtility.LogInfo("<------  GCM Result ----------->  :" + gcmResult.getErrorCodeName()
					+ " Message ID " + gcmResult.getMessageId(), LOG_TAG);
			gcmResponse = "[ errorCode=" + gcmResult.getErrorCodeName() + "] ";

		}
		catch (IOException e) {
			LogUtility.LogError("Error While Sending Notification : " + e, LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Sending Notification : " + e, LOG_TAG);
		}
		return gcmResponse;

	}

}
