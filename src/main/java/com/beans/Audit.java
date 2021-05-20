package com.beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Audit {

	private String titre;
	private int id;
	private Utilisateur auteur;
	private Modele modele;
	private Statut statut;
	private StatutBase statutBase; //Prend le statut en base
	private Date dateOuverture;
	private Date dateCloture;
	private List<Utilisateur> listeConcerne;
	private Ue uniteEnseignement;
	//test
	public Audit() {
		super();
		this.listeConcerne=new ArrayList<Utilisateur>();
	}
	
	public Audit(int id, String titre, Utilisateur auteur, StatutBase statutBase, Modele modele, Date dateOuverture, Date dateCloture, List<Utilisateur> listeConcerne) {
		this.id=id;
		this.titre=titre;
		this.auteur=auteur;
		this.statutBase=statutBase;
		this.modele=modele;
		this.dateOuverture=dateOuverture;
		this.dateCloture=dateCloture;
		this.listeConcerne=listeConcerne;
		this.setStatutCourrant();
	}
	
	public Audit(int id, String titre, StatutBase statutBase, Date dateOuverture, Date dateCloture) {
		this.id=id;
		this.titre=titre;
		this.statutBase=statutBase;
		this.dateOuverture=dateOuverture;
		this.dateCloture=dateCloture;
		this.listeConcerne=new ArrayList<Utilisateur>();
		this.setStatutCourrant();
	}
	
	public Audit(int id, String titre, Date dateOuverture, Date dateCloture) {
		this.id=id;
		this.titre=titre;
		this.dateOuverture=dateOuverture;
		this.dateCloture=dateCloture;
		this.listeConcerne=new ArrayList<Utilisateur>();
	}

	public void ajouterUtilisateurConcerne(Utilisateur utilisateur) {
		this.listeConcerne.add(utilisateur);
	}
	
	public void supprimerUtilisateurConcerne(Utilisateur utilisateur) {
		if(utilisateur!=null && this.listeConcerne.contains(utilisateur)) {
				this.listeConcerne.remove(utilisateur);
		}
	}
	
	public Ue getUniteEnseignement() {
		return uniteEnseignement;
	}

	public void setUniteEnseignement(Ue uniteEnseignement) {
		this.uniteEnseignement = uniteEnseignement;
	}
	
	public List<Utilisateur> getListeConcerne() {
		return listeConcerne;
	}

	public void setListeConcerne(List<Utilisateur> listeConcerne) {
		this.listeConcerne = listeConcerne;
	}

	public Date getDateOuverture() {
		return dateOuverture;
	}

	public void setDateOuverture(Date dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public Date getDateCloture() {
		return dateCloture;
	}

	public void setDateCloture(Date dateCloture) {
		this.dateCloture = dateCloture;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Utilisateur getAuteur() {
		return auteur;
	}

	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}

	public Modele getModele() {
		return modele;
	}

	public void setModele(Modele modele) {
		this.modele = modele;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}
	
	public StatutBase getStatutBase() {
		return statutBase;
	}

	public void setStatutBase(StatutBase statutBase) {
		this.statutBase = statutBase;
	}
	
	public void setStatutCourrant() {
        if(this.dateOuverture!=null && this.dateCloture!=null) {
        	if(this.statutBase!=null && this.statutBase==StatutBase.FERME) {
        		this.statut=Statut.CLOTURE;
        	}
        	else {
        		long millisCourrant = System.currentTimeMillis();
        		Date dateCourrant = new Date(millisCourrant);
        		if(dateCourrant.before(dateOuverture))
        			this.statut=Statut.A_VENIR;
        		else if(dateCourrant.after(dateCloture))
        			this.statut=Statut.COMPLETE;
        		else
        			this.statut=Statut.EN_COURS;        		
        	}

        }
    }
	
	public void setStatutBaseFromStatut() {
		if(this.getStatut()!=null) {
			if(this.getStatut()==Statut.COMPLETE) {
				this.setStatutBase(StatutBase.FERME);
			}
			else {
				this.setStatutBase(StatutBase.OUVERT);
			}
		}
	}
	
}