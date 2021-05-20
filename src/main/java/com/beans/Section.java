package com.beans;

import java.util.ArrayList;
import java.util.List;

public class Section {

	private int id;
	private String titre;
	private List<Question> listeQuestion;
	
	
	public Section() {
		listeQuestion=new ArrayList<>();
	}
	
	//Ajout d'un constructeur ?
	public Section(int id, String titre, List<Question> listeQuestion) {
		this.id = id;
		this.titre = titre;
		this.listeQuestion = listeQuestion;
	}
	
	public Section(int id, String titre) {
		this.id = id;
		this.titre = titre;
		this.listeQuestion = new ArrayList<>();
	}

	public void ajouterQuestion(Question question) {
		this.listeQuestion.add(question);
	}
	
	public void supprimerQuestion(Question question) {
		if(question!=null && listeQuestion.contains(question)) {
				listeQuestion.remove(question);
		}
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


	public List<Question> getListeQuestion() {
		return listeQuestion;
	}

	public void setListeQuestion(List<Question> listeQuestions) {
		this.listeQuestion = listeQuestions;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (this.getClass() != o.getClass())
		    return false;
		
		return ((Section) o).id==this.id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
}
