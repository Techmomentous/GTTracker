package com.gt.services;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.JsonObject;
import com.gt.dbtools.MembersDB;
import com.gt.logs.LogUtility;
import com.gt.pojo.Member;

/**
 * Servlet implementation class MembersService
 */
@WebServlet("/MembersService")
public class MembersService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "MembersService";
	private MembersDB membersDB = null;
	String response = "";
	private int maxFileSize = 5242880; // 5MB -> 5 * 1024 * 1024
	private int maxMemSize = 3145728; // 3MB -> 3 * 1024 * 1024
	private File file;
	private static String PIC_FOLDER = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MembersService() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		PIC_FOLDER = config.getServletContext().getRealPath("member_pic");
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
			String result = "";
			if (request.getParameter("request") != null) {
				String serviceRequest = request.getParameter("request");

				switch (serviceRequest) {
				case "add":
					Member member = new Member();
					member.setFullname(request.getParameter("fullName"));
					member.setAge(request.getParameter("age"));
					member.setPhone(request.getParameter("phone"));
					member.setEmail(request.getParameter("email"));
					member.setAddress(request.getParameter("address"));
					member.setStartlatituted(request.getParameter("startlatitude"));
					member.setStartlongitude(request.getParameter("startlongitude"));
					result = addNewMember(member);

					response.sendRedirect("registration.jsp?responseMessage=" + result);
					LogUtility.LogInfo(result, TAG);
					break;
				case "updatePic":
					boolean isUploade = uplodMarkerPic(request);
					if (isUploade) {
						response.getWriter().write("Success");
					}
					else {
						response.getWriter().write("Faile");
					}
					break;
				default:
					break;
				}
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error in Members Service : " + e, TAG);
		}

		catch (Exception e) {
			LogUtility.LogError("Error in Members Service : " + e, TAG);
		}
	}

	/**
	 * Add new member details in the database
	 * 
	 * @param members
	 */
	private String addNewMember(Member member) {
		try {
			membersDB = new MembersDB();
			JsonObject responseJSON = new JsonObject();
			int rowCount = membersDB.addNewMember(member);
			if (rowCount > 0) {
				responseJSON.addProperty("messageType", "Alert");
				responseJSON.addProperty("result", "Success");
				responseJSON.addProperty("responseMessage",
						"Successfully Added Member Profile. Auto ID :" + rowCount);
				response = responseJSON.toString();
			}
			else {
				responseJSON.addProperty("messageType", "Alert");
				responseJSON.addProperty("result", "Failed");
				responseJSON.addProperty("responseMessage", "Sorry Due To Please Try Later.");
				response = responseJSON.toString();
			}

		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error in addNewMember : " + e, TAG);
		}

		catch (Exception e) {
			LogUtility.LogError("Error in addNewMember : " + e, TAG);
		}
		return response;
	}

	/**
	 * Upload the marker picture and add the file name entry in member table
	 * <p>
	 * The max threshold size is set to 3 MB
	 * </p>
	 * <p>
	 * The max file size is set to 5 MB
	 * </p>
	 * 
	 * @param request
	 */
	private boolean uplodMarkerPic(HttpServletRequest request) {
		boolean bsuccess = false;
		try {
			String autoid = request.getParameter("autoid");

			membersDB = new MembersDB();
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// maximum size that will be stored in memory
			factory.setSizeThreshold(maxMemSize);
			// Location to save data that is larger than maxMemSize.
			factory.setRepository(new File(PIC_FOLDER));

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// maximum file size to be uploaded.
			upload.setSizeMax(maxFileSize);
			// Parse the request to get file items.
			List<?> fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator<?> iterator = fileItems.iterator();
			while (iterator.hasNext()) {
				FileItem fileItem = (FileItem) iterator.next();
				if (!fileItem.isFormField()) {
					// Get the uploaded file parameters

					// Get the file name
					String fileName = fileItem.getName();
					String extension = fileName.substring(fileName.lastIndexOf("."));
					String newFileName = autoid + extension;
					// Get the content type
					String contentType = fileItem.getContentType();

					LogUtility.LogInfo("Upload File Name : " + fileName + "Content Type : "
							+ contentType, TAG);
					// Write the file

					// E:\OPEN_GTS_WORKSPACE\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\GTracker\member_pic
					file = new File(PIC_FOLDER + "//" + newFileName);
					fileItem.write(file);
					// Update the member picture in db
					bsuccess = membersDB.updateMemberPic(Integer.valueOf(autoid), newFileName);

					LogUtility.LogInfo("Marker Image Uploaded :new  File Name : " + newFileName,
							TAG);

				}
			}
		}
		catch (NullPointerException e) {
			LogUtility.LogError("Error in uplodMarkerPic : " + e, TAG);
		}

		catch (Exception e) {
			LogUtility.LogError("Error in uplodMarkerPic : " + e, TAG);
		}
		return bsuccess;
	}
}
