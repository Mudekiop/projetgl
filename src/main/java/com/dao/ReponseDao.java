package com.dao;

import java.util.List;

import com.beans.Reponse;

public interface ReponseDao {

	List<Reponse> getReponse(QuestionDao questionDao, AuditDao auditDao, UtilisateurDao userDao);
	
	List<Reponse> getReponseByUtilisateurAndAudit(QuestionDao questionDao, AuditDao auditDao, UtilisateurDao utilisateurDao, int idAudit, int idUtilisateur);

	List<Reponse> getReponseByQuestionAndAudit(QuestionDao questionDao, AuditDao auditDao, UtilisateurDao userDao, int idAudit);
	
	public Reponse modifierReponse(String contenuReponse, int idReponse);
}
