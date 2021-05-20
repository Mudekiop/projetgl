package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.beans.Utilisateur;
import com.utils.UtilServlet;

@WebServlet("/monProfilController")
public class MonProfilController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MonProfilController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
		if (utilisateur != null) {
			UtilServlet.empecherRetourNavigateur(response);
			try {
				this.getServletContext().getRequestDispatcher("/WEB-INF/monProfil.jsp").forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}			
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			this.doGet(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
