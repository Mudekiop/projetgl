package com.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.beans.Utilisateur;
import com.dao.DaoFactory;
import com.dao.UtilisateurDao;
import com.forms.FormConnexion;
import com.utils.UtilServlet;

@WebServlet("/connexionController")
public class ConnexionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String cheminConnexion = "/WEB-INF/connexion.jsp";

	public ConnexionController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			this.getServletContext().getRequestDispatcher(cheminConnexion).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		DaoFactory daoFactory = DaoFactory.getInstance();

		try {
			UtilisateurDao utilisateurDao = daoFactory.getUtilisateurDao();
			request.setAttribute("personnes", utilisateurDao.getListUtilisateurs());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (request.getParameter("connect") != null) {
			FormConnexion form = null;
			try {
				form = new FormConnexion(request);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (form != null && form.getStatus().equals("good")) {
				Utilisateur uConnected = form.getConnectedUser();
				HttpSession session = request.getSession();
				session.setAttribute("utilisateur", uConnected); 
				try {
					UtilServlet.empecherRetourNavigateur(response);
					if(uConnected.isInviteOnly()) {
						response.sendRedirect(request.getContextPath() + "/home");
					}
					else {
						response.sendRedirect(request.getContextPath() + "/audits");											
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if(form != null) {
					request.setAttribute("errorMsg", form.getStatus());
				}
				try {
					this.getServletContext().getRequestDispatcher(cheminConnexion).forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (request.getParameter("deco") != null) {
				HttpSession session = request.getSession();
				if (session.getAttribute("utilisateur") != null) {
					session.invalidate();
				}
				try {
					this.getServletContext().getRequestDispatcher(cheminConnexion).forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else {
				request.setAttribute("errorMsg", request.getSession());
			}
		}

	}

}
