package com.testBeans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.beans.Ue;

public class UeTest {

	@Test
	public void testConstructeur2Parametres() {
		Ue ue = new Ue(1, "TWIC");

		assertNotNull("L'ue n'a pas été créée", ue);
		assertEquals("Les id sont différents", 1, ue.getId());
		assertEquals("Les titres sont différents", "TWIC", ue.getTitre());
	}

	@Test
	public void testSetId() {
		Ue ue = new Ue(1, "TWIC");
		ue.setId(2);

		assertEquals("Les id sont différents", 2, ue.getId());
	}

	@Test
	public void testSetTitre() {
		Ue ue = new Ue(1, "TWIC");
		ue.setTitre("Administration systeme Linux");

		assertEquals("Les titres sont différents", "Administration systeme Linux", ue.getTitre());
	}

}
