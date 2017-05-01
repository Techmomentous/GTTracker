package com.gt.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gt.dbtools.MembersDB;
import com.gt.dbtools.MessageDB;
import com.gt.gcm.GCMServer;
import com.gt.logs.LogUtility;
import com.gt.pojo.Member;
import com.gt.pojo.PushMessage;

/**
 * Servlet implementation class GCMService
 */
@WebServlet(description = "This servlet handle all Google Cloud Messaging Activits", urlPatterns = { "/GCMService" })
public class GCMService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOG_TAG = "GCMService";
	GCMServer gcm = null;
	private MessageDB messageDB;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GCMService() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Execute The POST OR GET Request Response
	 * 
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter writer = null;
		try {
			String jsonData = "";
			String result = "";
			Member member = null;
			String messageCode = "";
			String message = "";
			String date = "";
			String currentTime = "";
			int memberID = 0;
			writer = response.getWriter();
			if (request.getParameter("request") == null) {
				LogUtility.LogError("Request is null : ", LOG_TAG);
				writer.write("Requset Object is invalid");
				return;
			}
			String requestType = request.getParameter("request");
			gcm = new GCMServer();

			switch (requestType) {

			case "sendMessage":

				// Get the parameters values
				messageCode = request.getParameter("messageCode");
				memberID = Integer.valueOf(request.getParameter("memberID"));
				message = request.getParameter("message");
				date = request.getParameter("date");
				currentTime = request.getParameter("currentTime");
				// Construct the json to send in gcm message
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("messageCode", messageCode);
				jsonObject.addProperty("messageText", message);
				jsonObject.addProperty("messageDate", date);
				jsonObject.addProperty("currentTime", currentTime);

				// Get the app registration id by member id
				MembersDB membersDB = new MembersDB();
				String appid = membersDB.getMemberyAPPID(memberID);

				// Send the push notification on gcm registered devices
				result = gcm.pushNotification(jsonObject.toString(), appid);

				// if Result is not equal to NotRegistered
				if (!result.equalsIgnoreCase("[ errorCode=NotRegistered ]")) {
					writer.write("Message Send Successfully");
				}
				else {
					writer.write("Your Device is Not Registered Under GCM.\n  Please Registered First ");
				}
				break;

			case "deviceRegistration":
				// Get the student data from json and parse it
				jsonData = request.getParameter("jsonData");
				member = parseMemberJSON(jsonData);
				result = gcm.registeredAndroidDevice(member);

				// TODO display popup on ui that a device is just registered
				// with device details
				if (!result.equalsIgnoreCase("[ errorCode=NotRegistered] ")) {
					writer.write("Device registration is done you will recived a notification of registration ");
				}
				else {
					writer.write("Your Device is Not Registered Under GCM.\n  Please Registered First ");
				}

				break;

			case "pushNotification":
				// Get the parameters values
				jsonData = request.getParameter("jsonData");
				int autoid = displayNotfication(jsonData);
				if(autoid>0){
					writer.write("Your Message Notification Is Succesfully Pushed. \n Message ID Is " + autoid);
				}else{
					writer.write("Failed To Push Message");
				}

				break;
			default:
				writer.write("Invalid Notification Request");
				break;
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error : " + e.getMessage(), LOG_TAG);
			writer.write("Sorry Your Requset Cant Process");
		}
		catch (Exception e) {
			writer.write("Sorry Your Requset Cant Process");
			LogUtility.LogError("Error : " + e.getMessage(), LOG_TAG);
		}
	}

	/**
	 * Parse the member json and return the member object
	 * 
	 * @param jsonData
	 * @return
	 */
	private Member parseMemberJSON(String jsonData) {
		Member member = null;
		try {
			JsonElement jelement = new JsonParser().parse(jsonData);
			JsonObject jsonObject = jelement.getAsJsonObject();

			int mebmerID = jsonObject.get("memberID").getAsInt();
			String appid = jsonObject.get("memberAppId").getAsString();
			String memberName = jsonObject.get("memberName").getAsString();
			member = new Member();
			member.setIdmember(mebmerID);
			member.setAppid(appid);
			member.setFullname(memberName);

			LogUtility.LogInfo("Member JSON : " + member, LOG_TAG);

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Parsing member JSON : " + e.getMessage(), LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Parsing member JSON : " + e.getMessage(), LOG_TAG);
		}
		return member;
	}

	/**
	 * Display The notification received from member device
	 * 
	 * @param jsonData
	 */
	private int displayNotfication(String jsonData) {
		int autoid = 0;
		try {
			JsonElement jelement = new JsonParser().parse(jsonData);
			JsonObject jsonObject = jelement.getAsJsonObject();

			int mebmerID = jsonObject.get("memberID").getAsInt();
			String messageType = jsonObject.get("messageType").getAsString();
			String messageText = jsonObject.get("message").getAsString();
			String memberName = jsonObject.get("memberName").getAsString();

			PushMessage pushMessage = new PushMessage();
			pushMessage.setMemberid(mebmerID);
			pushMessage.setType(messageType);
			pushMessage.setMessage(messageText);
			pushMessage.setName(memberName);

			// We Always Set Flag New to YES because its new message received
			// from device
			pushMessage.setFlagnew("YES");
			messageDB = new MessageDB();
			autoid = messageDB.addPushMessage(pushMessage);

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error While Parsing message JSON : " + e.getMessage(), LOG_TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error While Parsing message JSON : " + e.getMessage(), LOG_TAG);
		}
		return autoid;
	}
}
