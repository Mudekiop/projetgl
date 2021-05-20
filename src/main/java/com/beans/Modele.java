package com.beans;

import java.util.ArrayList;
import java.util.List;

public class Modele {

	private int id;
	private boolean visibilite;
	private List<Section> listeSection;
	private String titre;

	public Modele() {
		this.listeSection=new ArrayList<Section>();
	}
	
	public Modele(int id, boolean visibilite, String titre, List<Section> listeSection) {
		this.id=id;
		this.visibilite=visibilite;
		this.listeSection=listeSection;
		this.titre=titre;
		this.listeSection=new ArrayList<Section>();
	}

	public Modele(int id, boolean visibilite, String titre) {
		this.id=id;
		this.visibilite=visibilite;		
		this.titre=titre;
		this.listeSection=new ArrayList<Section>();
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void ajouterSection(Section section) {
		this.listeSection.add(section);
	}
	
	//Throw une erreur si la section est null ou pas contenue
	public void supprimerSection(Section section) {
		if(section!=null && listeSection.contains(section)) {
			listeSection.remove(section);
		}
		/*else{
		 * throw new IllegalArgumentException("Vous ne pouvez pas retirer une section qui n'est pas contenue");
		 * }
		 */
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isVisibilite() {
		return visibilite;
	}

	public void setVisibilite(boolean visibilite) {
		this.visibilite = visibilite;
	}

	public List<Section> getListeSection() {
		return listeSection;
	}

	public void setListeSection(List<Section> listeSection) {
		this.listeSection = listeSection;
	}
	
	public String getListeQuestionString() {
		String liste="";
		for(Section section : this.getListeSection()) {
			for(Question question : section.getListeQuestion())
				liste+=question.getContenu()+"\n";
		}
		return liste;
	}
	
}
