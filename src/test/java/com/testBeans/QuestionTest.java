package com.testBeans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.beans.Question;
import com.beans.TypeQuestion;

public class QuestionTest {

	@Test
	public void testConstructeur3parametres() {
		Question question = new Question(1, TypeQuestion.BOOLEAN, "Souhaitez-vous répondre à ce questionnaire ?");

		assertNotNull("La question n'a pas été créée", question);
		assertEquals("Les id sont différents", 1, question.getId());
		assertEquals("Le type de question ne correspond pas", TypeQuestion.BOOLEAN, question.getTypeQuestion());
		assertEquals("Le contenu de la question n'est pas le bon", "Souhaitez-vous répondre à ce questionnaire ?",
				question.getContenu());
	}

	@Test
	public void testSetId() {
		Question question = new Question(1, TypeQuestion.BOOLEAN, "L'identifiant a-t-il été modifié ?");
		question.setId(2);

		assertEquals("Les identifiants sont différents", 2, question.getId());
	}

	@Test
	public void testSetTypeQuestion() {
		Question question = new Question(1, TypeQuestion.BOOLEAN, "Le type de la question a-t-il été modifié ?");
		question.setTypeQuestion(TypeQuestion.LIBRE);

		assertEquals("Le type de question ne correspond pas", TypeQuestion.LIBRE, question.getTypeQuestion());
	}

	@Test
	public void testSetContenu() {
		Question question = new Question(1, TypeQuestion.BOOLEAN, "Question comprenant une erreur");
		question.setContenu("Le contenu de la question a-t-il été modifié ?");

		assertEquals("Le contenu de question ne correspond pas", "Le contenu de la question a-t-il été modifié ?",
				question.getContenu());
	}

}
