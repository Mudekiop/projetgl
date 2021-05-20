package com.testBeans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.beans.Question;
import com.beans.Section;

public class SectionTest {

	private static final Question question = new Question();
	private static final Question question2 = new Question();

	@Test
	public void testConstructeur3parametres() {
		List<Question> liste = new ArrayList<Question>();
		liste.add(question);
		Section section = new Section(1, "Section 1", liste);

		assertNotNull("La section n'a pas été créée", section);
		assertEquals("Les id sont différents", 1, section.getId());
		assertEquals("Les titres sont différents", "Section 1", section.getTitre());
		assertFalse("La liste de question est vide", section.getListeQuestion().isEmpty());
		assertEquals("Les questions sont différentes", question, section.getListeQuestion().get(0));
		assertEquals("Le nombre questions est différente", 1, section.getListeQuestion().size());
	}

	@Test
	public void testConstructeur2parametres() {
		Section section = new Section(2, "Section 2");

		assertNotNull("La section n'a pas été créée", section);
		assertEquals("Les id sont différents", 2, section.getId());
		assertEquals("Les titres sont différents", "Section 2", section.getTitre());
		assertTrue("La liste de question est vide", section.getListeQuestion().isEmpty());
	}

	@Test
	public void testSetId() {
		Section section = new Section(3, "Section 3");

		section.setId(4);
		assertEquals("Les id sont différents", 4, section.getId());
	}

	@Test
	public void testSetTitre() {
		Section section = new Section(3, "Section 3");

		section.setTitre("Section 4");
		assertEquals("Les titres sont différents", "Section 4", section.getTitre());
	}

	@Test
	public void testSetListQuestion() {
		List<Question> liste = new ArrayList<Question>();
		liste.add(question);
		liste.add(question2);
		Section section = new Section(3, "Section 3");

		section.setListeQuestion(liste);
		assertFalse("La liste de question est vide", section.getListeQuestion().isEmpty());
		assertEquals("Les premières questions sont différentes", question, section.getListeQuestion().get(0));
		assertEquals("Les deuxièmes questions sont différentes", question2, section.getListeQuestion().get(1));
		assertEquals("Le nombre questions est différente", 2, section.getListeQuestion().size());
	}
	
	@Test
	public void testAjoutQuestion() {
		Section section = new Section(3, "Section 3");

		section.ajouterQuestion(question2);
		assertFalse("La liste de question est vide", section.getListeQuestion().isEmpty());
		assertEquals("Les questions sont différentes", question2, section.getListeQuestion().get(0));
		assertEquals("Le nombre questions est différente", 1, section.getListeQuestion().size());
	}
	
	@Test
	public void testSuppressionQuestion() {
		Section section = new Section(3, "Section 3");
		section.ajouterQuestion(question2);
		section.ajouterQuestion(question);
		section.supprimerQuestion(question2);
		
		assertFalse("La liste de question est vide", section.getListeQuestion().isEmpty());
		assertEquals("Les questions sont différentes", question, section.getListeQuestion().get(0));
		assertEquals("Le nombre questions est différente", 1, section.getListeQuestion().size());
	}
}
