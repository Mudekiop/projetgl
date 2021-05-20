package com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nom;
	private String prenom;
	private String email;
	private List<Role> listeRoles;
	//Si l'utilisateur est responsable d'ue, il prend en attribut plusieurs ue
	private List<Ue> listeUe;
	


	public Utilisateur() {
		this.listeRoles=new ArrayList<>();
		this.listeUe=new ArrayList<Ue>();
	}

	public Utilisateur(int id, String nom, String prenom, String email, List<Role> listeRoles) {
		this.id=id;
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
		this.listeRoles=listeRoles;
		this.listeUe=new ArrayList<Ue>();
	}
	
	public Utilisateur(int id, String nom, String prenom, String email) {
		this.id=id;
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
		this.listeUe=new ArrayList<Ue>();
	}
	
	public Utilisateur(String email) {
		this.email = email;
		this.listeUe=new ArrayList<Ue>();
	}
	
	public Utilisateur(String nom, String prenom, String email) {
		this.nom=nom;
		this.prenom=prenom;
		this.email=email;
	}
	
	public void ajouterRole(Role role) {
		this.listeRoles.add(role);
	}
	
	public void supprimerRole(Role role) {
		if(role!=null && listeRoles.contains(role)) {
			listeRoles.remove(role);
		}
	}
	
	public List<Role> getListeRoles() {
		return listeRoles;
	}
	
	public void setListeRoles(List<Role> listeRoles) {
		this.listeRoles = listeRoles;
	}

	public void ajouterUe(Ue ue) {
		this.listeUe.add(ue);
	}
	
	public void supprimerUe(Ue ue) {
		if(ue!=null && listeUe.contains(ue)) {
			listeUe.remove(ue);
		}
	}
	
	public List<Ue> getListeUe() {
		return listeUe;
	}

	public void setListeUe(List<Ue> listeUe) {
		this.listeUe = listeUe;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isAdministrateur() {
		boolean resultat = false;		
		for(Role role : this.listeRoles) {
			if(role==Role.ADMINISTRATEUR)
				resultat=true;
		}		
		return resultat;
	}
	
	public boolean isResponsableOption() {
		boolean resultat = false;		
		for(Role role : this.listeRoles) {
			if(role==Role.RESPONSABLE_OPTION)
				resultat=true;
		}		
		return resultat;
	}

	public boolean isResponsableUe() {
		boolean resultat = false;		
		for(Role role : this.listeRoles) {
			if(role==Role.RESPONSABLE_UE)
				resultat=true;
		}		
		return resultat;
	}
	
	public boolean isEncadrantMatiere() {
		boolean resultat = false;		
		for(Role role : this.listeRoles) {
			if(role==Role.ENCADRANT_MATIERE)
				resultat=true;
		}		
		return resultat;
	}
	
	public boolean isEleve() {
		boolean resultat = false;		
		for(Role role : this.listeRoles) {
			if(role==Role.ELEVE)
				resultat=true;
		}		
		return resultat;
	}
	
	public boolean isInviteOnly() {
		boolean resultat = false;
		if(listeRoles.size()==1)
			if(listeRoles.get(0)==Role.INVITE)
				resultat=true;
		return resultat;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (this.getClass() != o.getClass())
		    return false;
		
		return ((Utilisateur) o).id==this.id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
