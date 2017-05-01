package com.gt.services;

import java.io.IOException;

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
import com.gt.dbtools.LocationDB;
import com.gt.dbtools.MembersDB;
import com.gt.logs.LogUtility;
import com.gt.pojo.LocationData;

/**
 * Servlet implementation class LocationService
 */
@WebServlet("/LocationService")
public class LocationService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "LocationService";
	private LocationDB locationDB = null;
	private MembersDB membersDB = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocationService() {
		super();
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
		preocessRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		preocessRequest(request, response);
	}

	/**
	 * Execute The POST OR GET Request Response
	 * 
	 * @param request
	 * @param response
	 */
	private void preocessRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getParameter("request") != null) {
				String serviceRequset = request.getParameter("request");

				if (serviceRequset.equalsIgnoreCase("locationUpdate")) {

					locationDB = new LocationDB();
					int memberid = Integer.valueOf(request.getParameter("MemberID"));
					LocationData locationData = new LocationData();
					locationData.setLongitude(request.getParameter("longitude"));
					locationData.setLatitude(request.getParameter("latitude"));
					locationData.setExtrainfo(request.getParameter("extrainfo"));
					locationData.setUsername(request.getParameter("username"));
					locationData.setDistance(request.getParameter("distance"));
					locationData.setDate(request.getParameter("date"));
					locationData.setDirection(request.getParameter("direction"));
					locationData.setAccuaracy(request.getParameter("accuracy"));
					locationData.setEventtype(request.getParameter("eventtype"));
					locationData.setSessionid(request.getParameter("sessionid"));
					locationData.setSpeed(request.getParameter("speed"));
					locationData.setLocationMethod(request.getParameter("locationmethod"));
					locationData.setMemberid(memberid);

					LogUtility.LogInfo("Got New Location Update : " + locationData, TAG);
					boolean isUpdated = locationDB.updateLocationByMemberID(locationData, memberid);

					if (isUpdated) {
						response.getWriter().write("Location Updated of Member ID " + memberid);
					}
					else {
						response.getWriter().write(
								"Failed To Updated Location of Member ID " + memberid);
					}

				}
				else if (serviceRequset.equalsIgnoreCase("getLocations")) {
					if (request.getParameter("members") != null) {
						String membersID = request.getParameter("members");
						JsonArray jsonArray = getMembersLocations(membersID);
						if (jsonArray != null) {
							response.getWriter().write(jsonArray.toString());
						}
						else {
							response.getWriter().write("No Data");
						}
					}

				}
				else if (serviceRequset.equalsIgnoreCase("getMembers")) {

					membersDB = new MembersDB();
					JsonArray jsonArray = membersDB.getAllMember();
					response.getWriter().write(jsonArray.toString());
				}
				else if (serviceRequset.equalsIgnoreCase("getAllMembersLocation")) {
					locationDB = new LocationDB();
					JsonArray jsonArray = locationDB.getAllMembersLocationJSON();
					if (jsonArray != null) {
						response.getWriter().write(jsonArray.toString());
					}
					else {
						response.getWriter().write("No Data");
					}
				}

			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error in Location Service : " + e, TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error in Location Service : " + e, TAG);
		}
	}

	/**
	 * Parse the json members id and return the members location of give id in
	 * json
	 * 
	 * @param memberid
	 */
	private JsonArray getMembersLocations(String memberid) {
		JsonArray locationJson = null;
		try {
			locationJson = new JsonArray();
			JsonElement json = new JsonParser().parse(memberid);
			JsonArray jsonArray = json.getAsJsonArray();
			locationDB = new LocationDB();

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				int id = jsonObject.get("id").getAsInt();
				JsonObject locationData = locationDB.getLocationJSONByMemberID(id);
				locationJson.add(locationData);
			}

		}
		catch (NullPointerException e) {
			e.printStackTrace(System.out);
			LogUtility.LogError("Error in Location Service Get Members Location : " + e, TAG);
		}
		catch (Exception e) {
			LogUtility.LogError("Error in Location Service Get Members Location: " + e, TAG);
		}
		return locationJson;

	}

}
