package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.beans.Modele;
import com.beans.Question;
import com.beans.Section;
import com.beans.TypeQuestion;

public class ModeleDaoImpl implements ModeleDao {

	private Connection connexion;

	private String requeteSQL = "SELECT qu.*, tq.type FROM question qu inner join contientquestion c on c.id_Question=qu.id inner join typequestion tq on tq.id=qu.typeQuestion where c.id_Section=?";

	public ModeleDaoImpl(Connection connexion) {
		this.connexion = connexion;
	}

	@Override
	public Modele getModeleById(int id) {

		Connection connexion = null;
		Modele modele = new Modele();
		try {
			connexion = this.connexion;
			// Récupérer modèle
			PreparedStatement statement = connexion.prepareStatement("SELECT " + "* " + "from modele " + "where id=?");
			statement.setString(1, String.valueOf(id));
			ResultSet resultat = statement.executeQuery();
			while (resultat.next()) {
				modele.setId(id);
				modele.setTitre(resultat.getString("titre"));
				modele.setVisibilite("1".equals(resultat.getString("visibilite"))); // Les valeurs de ce champ prennent
																					// '1' ou '0' en BDD
			}
			// Récupère les sections

			PreparedStatement statementSection = connexion.prepareStatement("SELECT s.* " + "FROM section s "
					+ "inner join contientsection cs on cs.id_Section=s.id " + "where cs.id_Modele=?");
			statementSection.setInt(1, id);
			ResultSet resultatSection = statementSection.executeQuery();
			while (resultatSection.next()) {
				Section section = new Section();
				section.setId(resultatSection.getInt("id"));
				section.setTitre(resultatSection.getString("titre"));
				PreparedStatement statementQuestion = connexion.prepareStatement(requeteSQL);
				statementQuestion.setInt(1, section.getId());
				ResultSet resultatQuestion = statementQuestion.executeQuery();
				while (resultatQuestion.next()) {
					section.ajouterQuestion(new Question(Integer.valueOf(resultatQuestion.getString("id")),
							TypeQuestion.valueOf(resultatQuestion.getString("type")),
							resultatQuestion.getString("contenu")));
				}
				modele.ajouterSection(section);

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return modele;
	}

	@Override
	public List<Modele> getListeModeles() {
		Connection connexion = null;
		List<Modele> liste = new ArrayList<>();
		try {
			connexion = this.connexion;
			PreparedStatement statement = connexion.prepareStatement("SELECT * FROM modele");
			ResultSet resultat = statement.executeQuery();
			while (resultat.next()) {
				Modele modele = new Modele();
				modele.setId(resultat.getInt("id"));
				modele.setTitre(resultat.getString("titre"));
				modele.setVisibilite("1".equals(resultat.getString("visibilite")));
				PreparedStatement statementSection = connexion.prepareStatement("SELECT s.* " + "FROM section s "
						+ "inner join contientsection cs on cs.id_Section=s.id " + "where cs.id_Modele=?");
				statementSection.setInt(1, modele.getId());
				ResultSet resultatSection = statementSection.executeQuery();
				while (resultatSection.next()) {
					Section section = new Section();
					section.setId(resultatSection.getInt("id"));
					section.setTitre(resultatSection.getString("titre"));
					PreparedStatement statementQuestion = connexion.prepareStatement(requeteSQL);
					statementQuestion.setInt(1, section.getId());
					ResultSet resultatQuestion = statementQuestion.executeQuery();
					while (resultatQuestion.next()) {
						section.ajouterQuestion(new Question(Integer.valueOf(resultatQuestion.getString("id")),
								TypeQuestion.valueOf(resultatQuestion.getString("type")),
								resultatQuestion.getString("contenu")));
					}
					modele.ajouterSection(section);

				}
				liste.add(modele);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return liste;
	}

}
