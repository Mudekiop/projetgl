package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.beans.Reponse;
import com.beans.Role;
import com.beans.Utilisateur;

public class ReponseDaoImpl implements ReponseDao {

	private Connection connexion;
	private ResultSet resultat;
	private Statement st;

	public ReponseDaoImpl(Connection connexion) {
		super();
		this.connexion = connexion;
		this.resultat = null;
		this.st = null;
	}

	public List<Reponse> getReponse(QuestionDao questionDao, AuditDao auditDao, UtilisateurDao userDao) {
		List<Reponse> liste = new ArrayList<>();
		Reponse reponse = null;
		try {
			this.st = this.connexion.createStatement();
			this.resultat = this.st.executeQuery("SELECT * FROM reponse ORDER BY id_Question;");
			while (this.resultat.next()) {
				reponse = new Reponse(resultat.getInt("id"), resultat.getString("contenuReponse"),
						auditDao.getAuditById(this.resultat.getInt("id_Audit"),
								userDao.getAuteurDeAuditById(this.resultat.getInt("id_Audit"))),
						userDao.getUtilisateurById(this.resultat.getInt("id_Utilisateur")),
						questionDao.getQuestionById(this.resultat.getInt("id_Question")));
				liste.add(reponse);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}

	public List<Reponse> getReponseByUtilisateurAndAudit(QuestionDao questionDao, AuditDao auditDao,
			UtilisateurDao userDao, int idAudit, int idUtilisateur) {
		List<Reponse> liste = new ArrayList<>();
		Reponse reponse = null;
		try {
			PreparedStatement statement = this.connexion.prepareStatement(
					"SELECT * FROM reponse WHERE id_Audit=? AND id_Utilisateur=? ORDER BY id_Question;");
			statement.setString(1, String.valueOf(idAudit));
			statement.setString(2, String.valueOf(idUtilisateur));
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				reponse = new Reponse(resultat.getInt("id"), resultat.getString("contenuReponse"),
						auditDao.getAuditById(idAudit, userDao.getAuteurDeAuditById(idAudit)),
						userDao.getUtilisateurById(idUtilisateur),
						questionDao.getQuestionById(this.resultat.getInt("id_Question")));
				liste.add(reponse);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}
	
	@Override
	public List<Reponse> getReponseByQuestionAndAudit(QuestionDao questionDao, AuditDao auditDao,
			UtilisateurDao userDao, int idAudit) {
		List<Reponse> liste = new ArrayList<>();
		Reponse reponse = null;
		try {
			PreparedStatement statement = this.connexion.prepareStatement(
					"SELECT q.id AS \"idQuestion\", q.contenu, r.id, r.contenuReponse, r.id_Utilisateur FROM question q LEFT JOIN reponse r ON r.id_Question = q.id AND r.id_Audit = ?");
			statement.setString(1, String.valueOf(idAudit));
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				reponse = new Reponse(resultat.getInt("id"), resultat.getString("contenuReponse"),
						auditDao.getAuditById(idAudit, userDao.getAuteurDeAuditById(idAudit)),
						userDao.getUtilisateurById(this.resultat.getInt("id_Utilisateur")),
						questionDao.getQuestionById(this.resultat.getInt("idQuestion")));
				liste.add(reponse);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}
	
	@Override
	public Reponse modifierReponse(String contenuReponse, int idReponse) {
		try {
			PreparedStatement statement = connexion.prepareStatement("UPDATE reponse SET contenuReponse = ? WHERE id = " + idReponse);
			statement.setString(1, String.valueOf(contenuReponse));
			statement.executeUpdate();
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
