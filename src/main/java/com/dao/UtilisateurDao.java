package com.dao;

import java.util.List;
import com.beans.Role;
import com.beans.Utilisateur;

public interface UtilisateurDao {

	Utilisateur getAuteurDeAuditById(int id);
	
	Utilisateur getAuteurDeReponseByIdAudit(int id);

	Utilisateur getUtilisateurById(int id);
	
	Utilisateur ajouterRoleUtilisateur(int id, Role role);
	
	Utilisateur supprimerRoleUtilisateur(int id, Role role);

	public List<Utilisateur> getListeUtilisateurByRole(Role role);
	
	Utilisateur getUtilisateurByEmail(String email);

	List<Utilisateur> getListUtilisateurs();
	
	void ajouterUtilisateur(Utilisateur utilisateur);

	boolean exists(Utilisateur p);

}
