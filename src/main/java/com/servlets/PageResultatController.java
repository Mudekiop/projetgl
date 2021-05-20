package com.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.beans.Audit;
import com.beans.Utilisateur;
import com.dao.AuditDao;
import com.dao.DaoFactory;
import com.dao.ModeleDao;
import com.dao.UtilisateurDao;

@WebServlet("/PageResultatController")
public class PageResultatController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PageResultatController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UtilisateurDao daoUtilisateur = null;
		AuditDao daoAudit = null;
		ModeleDao daoModele = null;
		
		HttpSession session = request.getSession();
		Utilisateur utilisateurCourrant = (Utilisateur) session.getAttribute("utilisateur");
		
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isEncadrantMatiere() || utilisateurCourrant.isResponsableOption() || utilisateurCourrant.isResponsableUe() || utilisateurCourrant.isAdministrateur()) {

				try {
					daoUtilisateur = DaoFactory.getInstance().getUtilisateurDao();
					daoAudit = DaoFactory.getInstance().getAuditDao();
					daoModele = DaoFactory.getInstance().getModeleDao();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				List<Audit> res = new ArrayList<>();
				if (daoAudit != null) {
					res = daoAudit.getAuditTermineByUtilisateur(utilisateurCourrant.getId(), daoUtilisateur, daoModele);
				}
		
				request.setAttribute("resultat", res);
		
				try {
					this.getServletContext().getRequestDispatcher("/WEB-INF/pageResultat.jsp").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			
			}else {
					try {
						response.sendRedirect(request.getContextPath()+"/home");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}else {
			try {
				response.sendRedirect(request.getContextPath()+"/connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur utilisateurCourrant = (Utilisateur) session.getAttribute("utilisateur");
		
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isEncadrantMatiere() || utilisateurCourrant.isResponsableOption() || utilisateurCourrant.isResponsableUe() || utilisateurCourrant.isAdministrateur()) {
				try {
					this.doGet(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}else {
				try {
					response.sendRedirect(request.getContextPath()+"/home");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			try {
				response.sendRedirect(request.getContextPath()+"/connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
