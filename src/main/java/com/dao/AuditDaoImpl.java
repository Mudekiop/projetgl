package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.beans.Audit;
import com.beans.Modele;
import com.beans.StatutBase;
import com.beans.Ue;
import com.beans.Utilisateur;

public class AuditDaoImpl implements AuditDao {
	private Connection connexion;
	private ResultSet resultat;
	private Statement st;

	private Audit audit;

	private String dateCloture = "dateCloture";
	private String dateOuverture = "dateOuverture";
	private String titre = "titre";
	private String typeStatut = "typeStatut";

	public AuditDaoImpl(Connection connexion) {
		super();
		this.connexion = connexion;
		this.resultat = null;
		this.st = null;
	}

	@Override
	public List<Audit> listerAudits() {

		List<Audit> listeAudits = new ArrayList<>();
		Statement statement = null;
		ResultSet resultat = null;

		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery("SELECT " + "a.*, s.typestatut, ue.titre as titreUE " + "from audit a "
					+ "inner join statut s on s.id=a.id_Statut " + "inner join ue ue on ue.id=a.id_Ue "
					+ "order by a.id");

			while (resultat.next()) {
				audit = new Audit(resultat.getInt("id"), resultat.getString(titre),
						StatutBase.valueOf(resultat.getString("typestatut")), resultat.getDate(dateOuverture),
						resultat.getDate(dateCloture));
				UtilisateurDao utilisateurDao = DaoFactory.getInstance().getUtilisateurDao();
				audit.setAuteur(utilisateurDao.getUtilisateurById(resultat.getInt("auteur")));
				ModeleDao modeleDao = DaoFactory.getInstance().getModeleDao();
				audit.setModele(modeleDao.getModeleById(resultat.getInt("id_Modele")));
				audit.setUniteEnseignement(new Ue(resultat.getInt("id_Ue"), resultat.getString("titreUE")));
				PreparedStatement statement2 = connexion.prepareStatement("SELECT c.id_Utilisateur FROM audit a "
						+ "INNER JOIN concerne c on c.id_Audit=a.id " + "WHERE c.id_Audit=?");
				statement2.setInt(1, audit.getId());
				ResultSet resultat2 = statement2.executeQuery();
				while (resultat2.next()) {
					audit.ajouterUtilisateurConcerne(
							utilisateurDao.getUtilisateurById(resultat2.getInt("id_Utilisateur")));
				}
				audit.setStatutCourrant();
				listeAudits.add(audit);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeAudits;
	}

	@Override
	public List<Audit> listerAuditsUe(Utilisateur utilisateur) {
		List<Audit> listeAudits = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultat = null;

		try {
			// Modifier cette partie pour prendre que les audits du bon UE

			for (Ue ue : utilisateur.getListeUe()) {
				statement = connexion.prepareStatement(
						"select " + "a.*, s.typestatut " + "from audit a " + "inner join statut s on s.id=a.id_Statut "
								+ "inner join estresponsableue eue ON a.id_Ue=eue.id_Ue "
								+ "inner join ue ue on ue.id=eue.id_Ue " + "where ue.id=?");
				statement.setInt(1, ue.getId());
				resultat = statement.executeQuery();
				while (resultat.next()) {
					audit = new Audit(resultat.getInt("id"), resultat.getString(titre),
							StatutBase.valueOf(resultat.getString("typestatut")), resultat.getDate(dateOuverture),
							resultat.getDate(dateCloture));
					UtilisateurDao utilisateurDao = DaoFactory.getInstance().getUtilisateurDao();
					audit.setAuteur(utilisateurDao.getUtilisateurById(resultat.getInt("auteur")));
					ModeleDao modeleDao = DaoFactory.getInstance().getModeleDao();
					audit.setModele(modeleDao.getModeleById(resultat.getInt("id_Modele")));
					audit.setUniteEnseignement(ue);
					PreparedStatement statement2 = connexion.prepareStatement("SELECT c.id_Utilisateur FROM audit a "
							+ "INNER JOIN concerne c on c.id_Audit=a.id " + "WHERE c.id_Audit=?");
					statement2.setInt(1, audit.getId());
					ResultSet resultat2 = statement2.executeQuery();
					while (resultat2.next()) {
						audit.ajouterUtilisateurConcerne(
								utilisateurDao.getUtilisateurById(resultat2.getInt("id_Utilisateur")));
					}
					audit.setStatutCourrant();
					listeAudits.add(audit);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeAudits;
	}

	@Override
	public List<Audit> listerMesAudits(Utilisateur utilisateur) throws SQLException {

		List<Audit> listeAudits = new ArrayList<>();

		Statement statement = null;
		ResultSet resultat = null;
		Statement statement2 = null;
		ResultSet resultat2 = null;

		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery(
					"SELECT a.id as id, a.titre as titre, a.dateOuverture as dateOuverture, a.dateCloture as dateCloture, a.id_Statut as id_Statut FROM utilisateur u, concerne c, audit a WHERE a.id = c.id_Audit AND c.id_Utilisateur = "
							+ utilisateur.getId() + " GROUP BY 1,2,3,4,5;");

			while (resultat.next()) {
				// On recherche le statut de l'audit
				try {
					statement2 = connexion.createStatement();
					resultat2 = statement2
							.executeQuery("SELECT * FROM statut WHERE id = " + resultat.getString("id_Statut") + ";");
					while (resultat2.next()) {
						StatutBase statutBase = StatutBase.valueOf(resultat2.getString(typeStatut));
						audit = new Audit(resultat.getInt("id"), resultat.getString(titre), statutBase,
								resultat.getDate(dateOuverture), resultat.getDate(dateCloture));

						listeAudits.add(audit);
					}
				} finally {
					if (statement2 != null) {
						statement2.close();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return listeAudits;
	}

	@Override
	public boolean auditAlreadyExists(Audit audit) {
		boolean retour = false;
		PreparedStatement statement = null;
		try {
			statement = connexion.prepareStatement("SELECT * FROM audit where id=?");
			statement.setString(1, String.valueOf(audit.getId()));
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				retour = true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retour;
	}

	public boolean publierAudit(Audit audit) {
		boolean retour = false;
		PreparedStatement statement = null;
		try {
			statement = this.connexion.prepareStatement(
					"INSERT INTO Audit (id, dateOuverture, dateCloture, titre, auteur, id_Statut, id_Modele) "
							+ "VALUES (NULL, ?, ?, ?, ?, ?, ?)");

			statement.setString(1, audit.getDateOuverture().toString());
			statement.setString(2, audit.getDateCloture().toString());
			statement.setString(3, audit.getTitre());
			statement.setString(4, String.valueOf(audit.getAuteur().getId()));
			statement.setInt(5, StatutBase.getIdByStatut(audit.getStatutBase()));
			statement.setString(6, String.valueOf(audit.getModele().getId()));
			statement.setInt(7, audit.getUniteEnseignement().getId());
			statement.executeUpdate();

			statement = this.connexion.prepareStatement("SELECT id FROM Audit ORDER BY id DESC LIMIT 1");
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				audit.setId(Integer.valueOf(this.resultat.getString("id")));
			}

			for (Utilisateur utilisateur : audit.getListeConcerne()) {
				statement = this.connexion
						.prepareStatement("INSERT INTO concerne (id_Audit, id_Utilisateur) VALUES (?,?) ");
				statement.setString(1, String.valueOf(audit.getId()));
				statement.setString(2, String.valueOf(utilisateur.getId()));
				statement.executeUpdate();
			}
			retour = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retour;
	}

	public List<Audit> getAuditByUtilisateur(Utilisateur user) {
		Audit audit = null;
		Modele modele = null;
		StatutBase statutBase = null;
		List<Audit> liste = new ArrayList<>();
		try {
			this.st = this.connexion.createStatement();
			this.resultat = this.st.executeQuery("SELECT s.typeStatut FROM audit a, statut s WHERE a.auteur="
					+ user.getId() + " AND a.id_Statut=s.id;");
			while (this.resultat.next()) {
				statutBase = StatutBase.valueOf(this.resultat.getString(typeStatut));
			}
			// a modifier
			ModeleDao modeleDao = DaoFactory.getInstance().getModeleDao();
			modele = modeleDao.getModeleById(resultat.getInt("id_Modele"));
			//
			this.resultat = this.st.executeQuery("SELECT * FROM audit a WHERE a.auteur=" + user.getId() + ";");
			while (this.resultat.next()) {
				audit = new Audit(this.resultat.getInt("id"), this.resultat.getString(titre), user, statutBase, modele,
						this.resultat.getDate(dateOuverture), this.resultat.getDate(dateCloture), null);
				audit.setStatutCourrant();
				liste.add(audit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}

	public Audit getAuditById(int id, Utilisateur user) {
		Audit audit = null;
		Modele modele = null;
		StatutBase statutBase = null;

		try {
			this.st = this.connexion.createStatement();
			this.resultat = this.st.executeQuery("SELECT s.typeStatut FROM audit a, statut s WHERE a.auteur="
					+ user.getId() + " AND a.id_Statut=s.id;");
			while (this.resultat.next()) {
				statutBase = StatutBase.valueOf(this.resultat.getString(typeStatut));
			}
			this.resultat = this.st
					.executeQuery("SELECT m.id, m.visibilite, m.titre FROM modele m, audit a WHERE a.auteur="
							+ user.getId() + " AND a.id_Modele=m.id;");
			while (this.resultat.next()) {
				modele = new Modele(this.resultat.getInt("id"), this.resultat.getBoolean("visibilite"),
						this.resultat.getString(titre));
			}
			this.resultat = this.st.executeQuery("SELECT * FROM audit a WHERE a.id=" + id + ";");
			while (resultat.next()) {
				audit = new Audit(this.resultat.getInt("id"), this.resultat.getString(titre), user, statutBase, modele,
						this.resultat.getDate(dateOuverture), this.resultat.getDate(dateCloture), null);
				audit.setStatutCourrant();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return audit;
	}

	public List<Audit> getAuditTermineByUtilisateur(int idUtilisateur, UtilisateurDao daoUser, ModeleDao daoModele) {
		Audit audit = null;
		List<Audit> liste = new ArrayList<>();

		try {
			PreparedStatement statement = connexion.prepareStatement(
					"SELECT * FROM audit JOIN concerne ON audit.id=concerne.id_Audit AND id_Utilisateur=? JOIN statut ON audit.id_Statut=statut.id AND statut.typeStatut='FERME'");
			statement.setInt(1, idUtilisateur);
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				audit = new Audit(this.resultat.getInt("id"), this.resultat.getString(titre),
						daoUser.getUtilisateurById(this.resultat.getInt("auteur")), StatutBase.valueOf("FERME"),
						daoModele.getModeleById(this.resultat.getInt("id_Modele")),
						this.resultat.getDate(dateOuverture), this.resultat.getDate(dateCloture), null);
				audit.setStatutCourrant();
				liste.add(audit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}

	public List<Audit> getAuditEnCoursByUtilisateur(int idUtilisateur, UtilisateurDao daoUser, ModeleDao daoModele) {
		Audit audit = null;
		List<Audit> liste = new ArrayList<>();

		try {
			PreparedStatement statement = connexion.prepareStatement(
					"SELECT * FROM audit JOIN concerne ON audit.id=concerne.id_Audit AND id_Utilisateur=? JOIN statut ON audit.id_Statut=statut.id AND statut.typeStatut='EN_COURS'");
			statement.setInt(1, idUtilisateur);
			this.resultat = statement.executeQuery();
			while (this.resultat.next()) {
				audit = new Audit(this.resultat.getInt("id"), this.resultat.getString(titre),
						daoUser.getUtilisateurById(this.resultat.getInt("auteur")), StatutBase.valueOf("FERME"),
						daoModele.getModeleById(this.resultat.getInt("id_Modele")),
						this.resultat.getDate(dateOuverture), this.resultat.getDate(dateCloture), null);
				audit.setStatutCourrant();
				liste.add(audit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return liste;
	}

}
