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
import com.dao.AuditDao;
import com.dao.DaoFactory;
import com.utils.UtilServlet;

@WebServlet("/auditsController")
public class AuditsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String listeAudits = "listeAudits";
	private static String cheminAudits = "/WEB-INF/audits.jsp";

	public AuditsController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DaoFactory daoFactory = DaoFactory.getInstance();

		HttpSession session = request.getSession();
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

		if (utilisateur != null) {
			AuditDao auditDao = null;
			try {
				auditDao = daoFactory.getAuditDao();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			//Bloc pour empêcher le retour arrière
			UtilServlet.empecherRetourNavigateur(response);
			if (utilisateur.isResponsableOption() || utilisateur.isAdministrateur()) {
				// Responsable d'option peut tout voir et administrateur aussi toto
				try {
					if(auditDao != null) {
						request.setAttribute(listeAudits, auditDao.listerAudits());
						this.getServletContext().getRequestDispatcher(cheminAudits).forward(request, response);
					}
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
			// NE PEUT VOIR QUE LES AUDITS DE SON UE
			else if (utilisateur.isResponsableUe()) {
				try {
					if(auditDao != null) {
						request.setAttribute(listeAudits, auditDao.listerAuditsUe(utilisateur));
						this.getServletContext().getRequestDispatcher(cheminAudits).forward(request, response);
					}
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			} else if (utilisateur.isEleve() || utilisateur.isEncadrantMatiere()) {
				try {
					if(auditDao != null) {
						request.setAttribute(listeAudits, auditDao.listerMesAudits(utilisateur));
						this.getServletContext().getRequestDispatcher(cheminAudits).forward(request, response);
					}
				} catch (ServletException | IOException | SQLException e) {
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
		try {
			this.doGet(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
