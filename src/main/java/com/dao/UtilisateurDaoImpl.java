package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.beans.Role;
import com.beans.Ue;
import com.beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	private Connection connexion;

	private String email = "email";
	private String prenom = "prenom";

	public UtilisateurDaoImpl(Connection connexion) {
		super();
		this.connexion = connexion;
	}

	// Modification
	@Override
	public List<Utilisateur> getListUtilisateurs() {
		Statement statement = null;
		ResultSet resultat = null;
		List<Utilisateur> listeUtilisateurs = new ArrayList<>();
		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery("SELECT * FROM utilisateur;");
			while (resultat.next()) {

				Utilisateur utilisateur = new Utilisateur(resultat.getInt("id"), resultat.getString("nom"),
						resultat.getString(prenom), resultat.getString(email));
				listeUtilisateurs.add(utilisateur);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeUtilisateurs;
	}

	public boolean exists(Utilisateur utilisateur) {
		ResultSet resultat = null;
		try {
			PreparedStatement preparedStatement = connexion
					.prepareStatement("SELECT COUNT(*) FROM utilisateur WHERE LOWER(email) = LOWER(?);");
			preparedStatement.setString(1, utilisateur.getEmail());
			resultat = preparedStatement.executeQuery();
			if (resultat.next() && resultat.getInt(1) == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Utilisateur getUtilisateurById(int id) {
		Utilisateur user = null;
		List<Role> role = new ArrayList<>();
		try {
			Statement st = this.connexion.createStatement();
			ResultSet resultat = st.executeQuery(
					"SELECT r.type FROM listeroles l, role r WHERE l.id_Utilisateur =" + id + " AND r.id = l.id_Role ");
			while (resultat.next()) {
				role.add(Role.valueOf(resultat.getString("type")));
			}
			resultat = st.executeQuery("SELECT * FROM utilisateur WHERE id=" + id + ";");
			while (resultat.next()) {
				user = new Utilisateur(resultat.getInt("id"), resultat.getString("nom"), resultat.getString(prenom),
						resultat.getString(email), role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public Utilisateur getAuteurDeAuditById(int id) {
		Utilisateur user = null;
		List<Role> role = new ArrayList<>();
		int idUtilisateur = 0;
		try {
			Statement st = connexion.createStatement();
			ResultSet resultat = st.executeQuery(
					"SELECT u.id FROM utilisateur u, audit WHERE audit.id=" + id + " AND u.id=audit.auteur");
			while (resultat.next()) {
				idUtilisateur = resultat.getInt("id");
			}
			st = connexion.createStatement();
			resultat = st.executeQuery("SELECT r.type FROM listeroles l, role r WHERE l.id_Utilisateur ="
					+ idUtilisateur + " AND r.id = l.id_Role ");
			while (resultat.next()) {
				role.add(Role.valueOf(resultat.getString("type")));
			}
			resultat = st.executeQuery("SELECT * FROM utilisateur WHERE id=" + idUtilisateur + ";");
			while (resultat.next()) {
				user = new Utilisateur(resultat.getInt("id"), resultat.getString("nom"), resultat.getString(prenom),
						resultat.getString(email), role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public Utilisateur getAuteurDeReponseByIdAudit(int id) {
		Utilisateur user = null;
		List<Role> role = new ArrayList<>();
		int idUtilisateur = 0;
		try {
			Statement st = connexion.createStatement();
			ResultSet resultat = st.executeQuery("SELECT u.id FROM utilisateur u, reponse WHERE reponse.id_Audit=" + id
					+ " AND u.id=reponse.id_Utilisateur");
			while (resultat.next()) {
				idUtilisateur = resultat.getInt("id");
			}
			st = connexion.createStatement();
			resultat = st.executeQuery("SELECT r.type FROM listeroles l, role r WHERE l.id_Utilisateur ="
					+ idUtilisateur + " AND r.id = l.id_Role ");
			while (resultat.next()) {
				role.add(Role.valueOf(resultat.getString("type")));
			}
			resultat = st.executeQuery("SELECT * FROM utilisateur WHERE id=" + idUtilisateur + ";");
			while (resultat.next()) {
				user = new Utilisateur(resultat.getInt("id"), resultat.getString("nom"), resultat.getString(prenom),
						resultat.getString(email), role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public Utilisateur ajouterRoleUtilisateur(int id, Role role) {
		Statement statement = null;
		ResultSet resultat = null;
		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery("SELECT r.id FROM role r WHERE r.type='" + role + "'");
			while (resultat.next()) {
				PreparedStatement preparedStatement = connexion
						.prepareStatement("INSERT INTO listeroles (id_Role, id_Utilisateur) VALUES ("
								+ resultat.getString("id") + "," + id + ")");
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Utilisateur supprimerRoleUtilisateur(int id, Role role) {
		Statement statement = null;
		ResultSet resultat = null;
		try {
			statement = connexion.createStatement();
			resultat = statement.executeQuery("SELECT r.id FROM role r WHERE r.type='" + role + "'");
			while (resultat.next()) {
				PreparedStatement preparedStatement = connexion
						.prepareStatement("DELETE FROM listeroles WHERE id_Role = " + resultat.getString("id")
								+ " AND id_Utilisateur = " + id + "");
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Utilisateur> getListeUtilisateurByRole(Role role) {
		List<Utilisateur> liste = new ArrayList<>();
		try {
			PreparedStatement statement = connexion.prepareStatement("SELECT " + "u.*, type as role " + "FROM "
					+ "utilisateur u " + "inner join listeroles lr on lr.id_Utilisateur=u.id "
					+ "inner join role r on r.id=lr.id_Role " + "where " + "r.type=?");
			statement.setString(1, role.toString());
			ResultSet resultat = statement.executeQuery();
			while (resultat.next()) {
				Utilisateur utilisateur = this.getUtilisateurById(Integer.valueOf(resultat.getString("id")));
				liste.add(utilisateur);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return liste;
	}

	@Override
	public void ajouterUtilisateur(Utilisateur utilisateur) {
		try {
			PreparedStatement preparedStatement = connexion
					.prepareStatement("INSERT INTO utilisateur (`nom`, `prenom`, `email`) VALUES (?, ?, ?);");
			preparedStatement.setString(1, utilisateur.getNom());
			preparedStatement.setString(2, utilisateur.getPrenom());
			preparedStatement.setString(3, utilisateur.getEmail());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Utilisateur getUtilisateurByEmail(String email) {
		Utilisateur user = null;
		List<Role> role = new ArrayList<>();
		try {
			Statement st = this.connexion.createStatement();
			ResultSet resultat = st
					.executeQuery("SELECT r.type FROM listeroles l, role r, utilisateur u WHERE u.email='" + email
							+ "' AND u.id = l.id_Utilisateur AND r.id = l.id_Role ");
			while (resultat.next()) {
				role.add(Role.valueOf(resultat.getString("type")));
			}
			resultat = st.executeQuery("SELECT * FROM utilisateur WHERE email='" + email + "';");
			while (resultat.next()) {
				user = new Utilisateur(resultat.getInt("id"), resultat.getString("nom"), resultat.getString(prenom),
						resultat.getString(this.email), role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

}
