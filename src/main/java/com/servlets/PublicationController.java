package com.servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.Audit;
import com.beans.Modele;
import com.beans.Role;
import com.beans.Ue;
import com.beans.Utilisateur;
import com.dao.AuditDao;
import com.dao.DaoFactory;
import com.dao.ModeleDao;
import com.dao.UeDao;
import com.dao.UtilisateurDao;

import com.utils.UtilServlet;
//import com.sun.org.apache.xml.internal.security.Init;

@WebServlet("/publicationController")
public class PublicationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UeDao ueDao;

	//private ModeleDao modeleDao;
	//private UtilisateurDao utilisateurDao;
	//private AuditDao auditDao;
	
	private static String listeEleveString = "listeEleve";
	private static String listeEncadrantMatiereString = "listeEncadrantMatiere";

	public PublicationController() {
		super();
	}

	/*public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		try {
			ModeleDao modeleDao = daoFactory.getModeleDao();
			this.utilisateurDao = daoFactory.getUtilisateurDao();
			this.auditDao = daoFactory.getAuditDao();
			this.ueDao = daoFactory.getUeDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute("utilisateur");
		
		UtilisateurDao daoUtilisateur = null;
		ModeleDao daoModele = null;

		try {
			daoUtilisateur = DaoFactory.getInstance().getUtilisateurDao();
			daoModele = DaoFactory.getInstance().getModeleDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isResponsableUe()) {
				UtilServlet.empecherRetourNavigateur(response);
				// Si on choisit un audit
				if (request.getParameter("id") != null) {
					String id = request.getParameter("id");
					Modele modele = null;
					try {
						if(daoModele != null) {
							modele = daoModele.getModeleById(Integer.valueOf(id));
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					if(daoUtilisateur != null && modele.getId()!=0) {
						List<Utilisateur> listeResponsableOption = daoUtilisateur.getListeUtilisateurByRole(Role.RESPONSABLE_OPTION);
						List<Utilisateur> listeResponsableUE = daoUtilisateur.getListeUtilisateurByRole(Role.RESPONSABLE_UE);
						List<Utilisateur> listeEncadrantMatiere = daoUtilisateur.getListeUtilisateurByRole(Role.ENCADRANT_MATIERE);
						List<Utilisateur> listeEleve = daoUtilisateur.getListeUtilisateurByRole(Role.ELEVE);
						request.setAttribute("modele", modele);
						request.setAttribute("listeResponsableOption", listeResponsableOption);
						request.setAttribute("listeResponsableUE", listeResponsableUE);
						request.setAttribute(listeEncadrantMatiereString, listeEncadrantMatiere);
						request.setAttribute(listeEleveString, listeEleve);
					}
					
					try {
						this.getServletContext().getRequestDispatcher("/WEB-INF/publicationAudit.jsp").forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
				// Sinon on affiche tous les modeles
				else {
					if(daoModele != null) {
						List<Modele> liste = daoModele.getListeModeles();
						request.setAttribute("listeModeles", liste);
					}
					try {
						this.getServletContext().getRequestDispatcher("/WEB-INF/listeModeles.jsp").forward(request, response);
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Utilisateur utilisateurCourrant = (Utilisateur) request.getSession().getAttribute("utilisateur");
		
		UtilisateurDao daoUtilisateur = null;
		ModeleDao daoModele = null;
		AuditDao daoAudit = null;

		try {
			daoUtilisateur = DaoFactory.getInstance().getUtilisateurDao();
			daoModele = DaoFactory.getInstance().getModeleDao();
			daoAudit = DaoFactory.getInstance().getAuditDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(utilisateurCourrant!=null) {
			if(utilisateurCourrant.isResponsableUe()) {
				int idModele = 0;
				try {
					idModele = Integer.valueOf(request.getParameter("idModele"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				Modele modele = daoModele.getModeleById(idModele);
				Audit audit = new Audit();
				audit.setAuteur(daoUtilisateur.getUtilisateurById(utilisateurCourrant.getId()));
				audit.setModele(modele);
				audit.setTitre(request.getParameter("titre").trim());
				audit.setUniteEnseignement(ueDao.getUeById(Integer.valueOf(request.getParameter("ue"))));
				Date dateOuverture = Date.valueOf(request.getParameter("dateOuverture"));
				audit.setDateOuverture(dateOuverture);
				Date dateFermeture = Date.valueOf(request.getParameter("dateFermeture"));
				audit.setDateCloture(dateFermeture);
				List<String> listeIdConcernes = new ArrayList<>();
				listeIdConcernes.add(String.valueOf(utilisateurCourrant.getId()));
				if(request.getParameterValues("listeRespOption")!=null)
					listeIdConcernes.addAll(Arrays.asList(request.getParameterValues("listeRespOption")));
				if(request.getParameterValues("listeRespUE")!=null)
					listeIdConcernes.addAll(Arrays.asList(request.getParameterValues("listeRespUE")));
				if(request.getParameterValues(listeEncadrantMatiereString)!=null)
					listeIdConcernes.addAll(Arrays.asList(request.getParameterValues(listeEncadrantMatiereString)));
				if(request.getParameterValues(listeEleveString)!=null)
					listeIdConcernes.addAll(Arrays.asList(request.getParameterValues(listeEleveString)));
				
				
				List<Utilisateur> listeUtilisateursConcernes = new ArrayList<>();
				for(String id : listeIdConcernes) {
					try {
						listeUtilisateursConcernes.add(daoUtilisateur.getUtilisateurById(Integer.valueOf(id)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				audit.setListeConcerne(listeUtilisateursConcernes);
				
				audit.setStatutCourrant();

				audit.setStatutBaseFromStatut();

				if (daoAudit != null) {
					boolean resultat = daoAudit.publierAudit(audit);
					if(resultat)
						try {
							this.getServletContext().getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response); //appeler l'url accueil
						} catch (ServletException | IOException e) {
							e.printStackTrace();
						}
					else
						try {
							this.getServletContext().getRequestDispatcher("/WEB-INF/erreurPublicationAudit.jsp").forward(request, response); //afficher page erreur
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
