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
import com.beans.Reponse;
import com.beans.Utilisateur;
import com.dao.AuditDao;
import com.dao.DaoFactory;
import com.dao.QuestionDao;
import com.dao.ReponseDao;
import com.dao.UtilisateurDao;

@WebServlet("/ResultatServlet")
public class ResultatController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ResultatController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idAudit = 0;
		try {
			idAudit = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		UtilisateurDao daoUtilisateur = null;
		AuditDao daoAudit = null;
		QuestionDao daoQuestion = null;
		ReponseDao daoReponse = null;

		try {
			daoUtilisateur = DaoFactory.getInstance().getUtilisateurDao();
			daoAudit = DaoFactory.getInstance().getAuditDao();
			daoQuestion = DaoFactory.getInstance().getQuestionDao();
			daoReponse = DaoFactory.getInstance().getReponseDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		Utilisateur utilisateurCourrant = (Utilisateur) session.getAttribute("utilisateur");
		
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isEncadrantMatiere() || utilisateurCourrant.isResponsableOption() || utilisateurCourrant.isResponsableUe() || utilisateurCourrant.isAdministrateur()) {

				if(daoUtilisateur != null && daoAudit != null) {
					Audit audit = daoAudit.getAuditById(idAudit, daoUtilisateur.getAuteurDeAuditById(idAudit));
					request.setAttribute("audit", audit);
				}
				
				List<Reponse> reponses = new ArrayList<>();
				if (daoReponse != null) {
					reponses = daoReponse.getReponseByQuestionAndAudit(daoQuestion, daoAudit, daoUtilisateur, idAudit);
				}
				request.setAttribute("reponse", reponses);
		
				try {
					this.getServletContext().getRequestDispatcher("/WEB-INF/resultat.jsp").forward(request, response);
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
		}
		else {
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
		}
		else {
			try {
				response.sendRedirect(request.getContextPath()+"/connexion");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
