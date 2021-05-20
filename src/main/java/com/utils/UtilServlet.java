package com.utils;

import javax.servlet.http.HttpServletResponse;

public class UtilServlet {

	public static void empecherRetourNavigateur(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0);
	}
	
}
