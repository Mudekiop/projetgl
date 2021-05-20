package com.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.Utilisateur;
import com.dao.DaoFactory;
import com.dao.UtilisateurDao;
import com.utils.UtilServlet;

/**
 * Servlet implementation class PageAccueil
 */
@WebServlet("/PageAccueil")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AdminController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute("utilisateur");
		if (utilisateurCourrant != null) {
			UtilServlet.empecherRetourNavigateur(response);
			if (utilisateurCourrant.isAdministrateur()) {
				DaoFactory daoFactory = DaoFactory.getInstance();

				try {
					UtilisateurDao utilisateursDB = daoFactory.getUtilisateurDao();
					request.setAttribute("utilisateurs", utilisateursDB.getListUtilisateurs());
				} catch (SQLException e) {
					e.printStackTrace();
				}

				try {
					this.getServletContext().getRequestDispatcher("/WEB-INF/accueilAdmin.jsp").forward(request,
							response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					response.sendRedirect(request.getContextPath() + "/home");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				response.sendRedirect(request.getContextPath() + "/connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute("utilisateur");
		if (utilisateurCourrant != null) {
			if (utilisateurCourrant.isAdministrateur()) {
				try {
					doGet(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					response.sendRedirect(request.getContextPath() + "/home");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				response.sendRedirect(request.getContextPath() + "/connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
