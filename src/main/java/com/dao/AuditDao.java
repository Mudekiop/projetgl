package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.beans.Audit;
import com.beans.Utilisateur;

public interface AuditDao {

	boolean publierAudit(Audit audit);

	boolean auditAlreadyExists(Audit audit);

	List<Audit> getAuditByUtilisateur(Utilisateur user);

	Audit getAuditById(int id, Utilisateur user);

	List<Audit> getAuditTermineByUtilisateur(int idUtilisateur, UtilisateurDao daoUser, ModeleDao daoModele);

	List<Audit> getAuditEnCoursByUtilisateur(int idUtilisateur, UtilisateurDao daoUser, ModeleDao daoModele);

	List<Audit> listerMesAudits(Utilisateur utilisateur) throws SQLException;

	List<Audit> listerAudits();

	List<Audit> listerAuditsUe(Utilisateur utilisateur);
}
