package com.gt.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gt.dbtools.MessageDB;
import com.gt.logs.LogUtility;
import com.gt.pojo.PushMessage;

/**
 * Servlet implementation class MessageService
 */
@WebServlet("/MessageService")
public class MessageService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOG_TAG = "MessageService";
	private MessageDB messageDB = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageService() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		return null;
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
	void processRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			String jsonData = "";
			JsonArray jsonArray = null;
			int rowCount = 0;
			if (request.getParameter("request") != null) {
				PrintWriter writer = response.getWriter();
				String serviceRequest = request.getParameter("request");
				messageDB = new MessageDB();
				switch (serviceRequest) {
				case "addMessage":
					// Get the parameters values
					jsonData = request.getParameter("jsonData");
					int autoid = displayNotfication(jsonData);
					if (autoid > 0) {
						writer.write("Your Message Notification Is Succesfully Pushed. \n Message ID Is "
								+ autoid);
					}
					else {
						writer.write("Failed To Push Message");
					}
					break;
				case "getMessages":
					jsonArray = messageDB.getAllNewPushMessages();
					if (jsonArray != null) {
						writer.write(jsonArray.toString());
					}
					else {
						writer.write("No Messages Found");
					}

					break;
				case "deleteMessage":
					String ids = request.getParameter("ids");
					JsonElement jelement = new JsonParser().parse(ids);
					jsonArray = jelement.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						int id = jsonArray.get(i).getAsInt();
						rowCount = messageDB.removeMessage(id);
					}

					if (rowCount > 0) {
						writer.write("Message Deleted Successfully");
					}
					else {
						writer.write("Failed To Delete Message");
					}
					break;
				default:
					break;
				}
			}
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error in Members Service : " + e, LOG_TAG);
		}

		catch (Exception e) {
			LogUtility.LogError("Error in Members Service : " + e, LOG_TAG);
		}
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
			String currentTime = jsonObject.get("time").getAsString();

			PushMessage pushMessage = new PushMessage();
			pushMessage.setMemberid(mebmerID);
			pushMessage.setType(messageType);
			pushMessage.setMessage(messageText);
			pushMessage.setName(memberName);
			pushMessage.setTime(currentTime);
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
