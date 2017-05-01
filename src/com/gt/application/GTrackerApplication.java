package com.gt.application;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gt.db.DBConfig;

/**
 * Servlet implementation class GTTrackerApplication
 */
@WebServlet("/GTTrackerApplication")
public class GTrackerApplication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GTrackerApplication() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	public void init() throws ServletException {
		DBConfig dbConfig = new DBConfig();
		dbConfig.init();
		super.init();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

}
