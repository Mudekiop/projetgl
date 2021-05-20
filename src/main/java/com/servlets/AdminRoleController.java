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

import com.beans.Role;
import com.beans.Utilisateur;

import com.dao.DaoFactory;
import com.dao.UtilisateurDao;

import com.forms.FormAjouterRoleUtilisateur;
import com.forms.FormSupprimerRoleUtilisateur;
import com.utils.UtilServlet;

/**
 * Servlet implementation class AdminRole
 */
@WebServlet("/AdminRole")
public class AdminRoleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String utilisateurString = "utilisateur";
       
    public AdminRoleController() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute(utilisateurString);
		if(utilisateurCourrant!=null) {
			UtilServlet.empecherRetourNavigateur(response);
			if(utilisateurCourrant.isAdministrateur()) {
		    	int id = 0;
		    	try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				DaoFactory daoFactory = DaoFactory.getInstance();
		
				try {
					UtilisateurDao utilisateurDB = daoFactory.getUtilisateurDao();
					
					Utilisateur utilisateur = utilisateurDB.getUtilisateurById(id);
					List<Role> role = utilisateur.getListeRoles();
					request.setAttribute(utilisateurString, utilisateur);
					request.setAttribute("role", role);
					
					if(role.isEmpty()) {
						String roleAjouter = "INVITE";
						Role rolePlus = Role.valueOf(roleAjouter);
						utilisateurDB.ajouterRoleUtilisateur(id, rolePlus);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				List<Role> roles = new ArrayList<>();
				roles.add(Role.ADMINISTRATEUR);
				roles.add(Role.ELEVE);
				roles.add(Role.ENCADRANT_MATIERE);
				roles.add(Role.INVITE);
				roles.add(Role.RESPONSABLE_OPTION);
				roles.add(Role.RESPONSABLE_UE);
				request.setAttribute("roles", roles);
				
				try {
					this.getServletContext().getRequestDispatcher("/WEB-INF/editerRole.jsp").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
			else {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute(utilisateurString);
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isAdministrateur()) {
		    	int id = 0;
		    	try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				DaoFactory daoFactory = DaoFactory.getInstance();
		
				try {
					UtilisateurDao utilisateurDB = daoFactory.getUtilisateurDao();
					
					Utilisateur utilisateur = utilisateurDB.getUtilisateurById(id);
					List<Role> role = utilisateur.getListeRoles();
					request.setAttribute(utilisateurString, utilisateur);
					request.setAttribute("role", role);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				List<Role> roles = new ArrayList<>();
				roles.add(Role.ADMINISTRATEUR);
				roles.add(Role.ELEVE);
				roles.add(Role.ENCADRANT_MATIERE);
				roles.add(Role.INVITE);
				roles.add(Role.RESPONSABLE_OPTION);
				roles.add(Role.RESPONSABLE_UE);
				request.setAttribute("roles", roles);
				
				if(request.getParameter("ajouterUser") != null) {
					new FormAjouterRoleUtilisateur(request);
					try {
						this.doGet(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
				
				if(request.getParameter("supprimerUser") != null) {
					new FormSupprimerRoleUtilisateur(request);
					try {
						this.doGet(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
			}
			else {
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
