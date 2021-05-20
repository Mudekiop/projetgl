package com.beans;

public class Ue {

	private int id;
	private String titre;
	
	public Ue() {
		super();
	}
	

	public Ue(int id, String titre) {
		this.id=id;
		this.titre=titre;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	
}
