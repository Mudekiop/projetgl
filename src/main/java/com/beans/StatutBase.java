package com.beans;

public enum StatutBase {

	OUVERT,
	FERME;
	
	public static int getIdByStatut(StatutBase statut) {
		switch(statut) {
		case OUVERT:
			return 1;
		case FERME:
			return 2;
		}
		return -1;
	}
	
	
}
