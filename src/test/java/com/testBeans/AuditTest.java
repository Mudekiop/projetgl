package com.testBeans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.beans.Audit;
import com.beans.Modele;
import com.beans.Statut;
import com.beans.StatutBase;
import com.beans.Utilisateur;

@RunWith(EasyMockRunner.class)
public class AuditTest {

	private static final Utilisateur utilisateur = new Utilisateur();
	private static final Modele modele = new Modele();

	@Mock
	private Utilisateur userMock;
	private Modele modeleMock;

	@Test
	public void testConstructeur8parametres() {
		Audit audit = new Audit(1, "Audit pour test 8 parametres", utilisateur, StatutBase.FERME, modele,
				Date.valueOf("2021-04-20"), Date.valueOf("2021-04-23"), null);

		assertNotNull("L'audit n'a pas été créée", audit);
		assertEquals("Les id sont différents", 1, audit.getId());
		assertEquals("Les dates d'ouverture sont différentes", "2021-04-20", audit.getDateOuverture().toString());
		assertEquals("Les dates de clôture sont différentes", "2021-04-23", audit.getDateCloture().toString());
		assertEquals("Les titres sont différents", "Audit pour test 8 parametres", audit.getTitre());
		assertEquals("Le statut n'est pas le bon", StatutBase.FERME, audit.getStatutBase());
		assertSame("Les utilisateurs ne sont pas identiques", utilisateur, audit.getAuteur());
		assertSame("Les modeles ne sont pas identiques", modele, audit.getModele());
		assertNull("La liste des personnes concernées n'est pas nulle", audit.getListeConcerne());
	}

	@Test
	public void testConstructeur5parametres() {
		Audit audit = new Audit(2, "Audit pour test 5 parametres", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));

		assertNotNull("L'audit n'a pas été créée", audit);
		assertEquals("Les id sont différents", 2, audit.getId());
		assertEquals("Les dates d'ouverture sont différentes", "2021-04-29", audit.getDateOuverture().toString());
		assertEquals("Les dates de clôture sont différentes", "2021-06-23", audit.getDateCloture().toString());
		assertEquals("Les titres sont différents", "Audit pour test 5 parametres", audit.getTitre());
		assertEquals("Le statut n'est pas le bon", StatutBase.FERME, audit.getStatutBase());
	}

	@Test
	public void testConstructeur4parametres() {
		Audit audit = new Audit(3, "Audit pour test 4 parametres", Date.valueOf("2021-12-31"),
				Date.valueOf("2022-04-23"));

		assertNotNull("L'audit n'a pas été créée", audit);
		assertEquals("Les id sont différents", 3, audit.getId());
		assertEquals("Les dates d'ouverture sont différentes", "2021-12-31", audit.getDateOuverture().toString());
		assertEquals("Les dates de clôture sont différentes", "2022-04-23", audit.getDateCloture().toString());
		assertEquals("Les titres sont différents", "Audit pour test 4 parametres", audit.getTitre());
	}

	@Test
	public void testSetId() {
		Audit audit = new Audit(1, "Audit pour test identifiant", StatutBase.FERME, Date.valueOf("2021-04-20"),
				Date.valueOf("2021-04-23"));
		audit.setId(2);
		assertEquals("Les id sont différents", 2, audit.getId());
	}

	@Test
	public void testSetListeConcerne() {
		List<Utilisateur> liste = new ArrayList<Utilisateur>();
		liste.add(this.userMock);
		liste.add(utilisateur);
		Audit audit = new Audit(2, "Audit pour test liste concerne", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));
		audit.setListeConcerne(liste);
		assertEquals("Les tailles des listes ne sont pas identiques", 2, audit.getListeConcerne().size());
		
	}

	@Test
	public void testSetDateOuverture() {
		Audit audit = new Audit(1, "Audit pour test date ouverture", StatutBase.FERME, Date.valueOf("2021-04-20"),
				Date.valueOf("2021-04-23"));
		audit.setDateOuverture(Date.valueOf("2021-04-22"));

		assertEquals("Les dates d'ouverture sont différentes", "2021-04-22", audit.getDateOuverture().toString());
	}

	@Test
	public void testSetDateCloture() {
		Audit audit = new Audit(1, "Audit pour test date cloture", StatutBase.FERME, Date.valueOf("2021-04-20"),
				Date.valueOf("2021-04-23"));
		audit.setDateCloture(Date.valueOf("2021-04-30"));

		assertEquals("Les dates de clôture sont différentes", "2021-04-30", audit.getDateCloture().toString());
	}

	@Test
	public void testSetTitre() {
		Audit audit = new Audit(1, "Audit pour test titre", StatutBase.FERME, Date.valueOf("2021-04-20"),
				Date.valueOf("2021-04-23"));
		audit.setTitre("Test");
		assertEquals("Les titres sont différents", "Test", audit.getTitre());
	}
	
	@Test
	public void testSetAuteur() {
		Audit audit = new Audit(2, "Audit pour test auteur", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));
		audit.setAuteur(this.userMock);
		assertSame("Les auteurs sont différents", this.userMock, audit.getAuteur());
	}
	
	@Test
	public void testSetModele() {
		Audit audit = new Audit(2, "Audit pour test modele", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));
		audit.setModele(this.modeleMock);
		assertSame("Les modèles sont différents", this.modeleMock, audit.getModele());
	}
	
	@Test
	public void testSetStatut() {
		Audit audit = new Audit(2, "Audit pour test modele", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));
		audit.setStatut(Statut.EN_COURS);;
		assertSame("Les modèles sont différents", Statut.EN_COURS, audit.getStatut());
	}

	@Test
	public void testAjoutUtilisateurConcerne() {
		Audit audit = new Audit(2, "Audit pour test ajout utilisateur", StatutBase.FERME, Date.valueOf("2021-04-29"),
				Date.valueOf("2021-06-23"));
		audit.ajouterUtilisateurConcerne(this.userMock);
		assertSame("Les utilisateurs sont différents", this.userMock, audit.getListeConcerne().get(0));
	}

	@Test
	public void testSuppressionUtilisateurConcerne() {
		List<Utilisateur> liste = new ArrayList<Utilisateur>();
		liste.add(this.userMock);
		Audit audit = new Audit(1, "Audit pour test suppression utilisateur", utilisateur, StatutBase.FERME, modele, Date.valueOf("2021-04-20"),
				Date.valueOf("2021-04-23"), liste);
		audit.supprimerUtilisateurConcerne(this.userMock);
		assertTrue("La liste n'est pas vide", audit.getListeConcerne().isEmpty());
	}

}
