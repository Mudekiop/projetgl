package com.forms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.beans.Audit;
import com.beans.Question;
import com.dao.AuditDao;
import com.dao.DaoFactory;
import com.dao.ReponseDao;
import com.dao.QuestionDao;
import com.dao.UtilisateurDao;

public class FormModifierReponseAudit {

	private ReponseDao reponseDB;
	private QuestionDao questionDB;
	private AuditDao auditDB;
	private UtilisateurDao utilisateurDB;
	
	public FormModifierReponseAudit (HttpServletRequest r) {
		DaoFactory daoFactory = DaoFactory.getInstance();

		try {
			this.reponseDB = daoFactory.getReponseDao();
			this.questionDB = daoFactory.getQuestionDao();
			this.auditDB = daoFactory.getAuditDao();
			this.utilisateurDB = daoFactory.getUtilisateurDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int idAudit = Integer.parseInt(r.getParameter("id"));
		
		Audit audit = this.auditDB.getAuditById(idAudit, this.utilisateurDB.getAuteurDeAuditById(idAudit));
		
		List<Question> questions = new ArrayList<>();
		if (this.questionDB != null) {
			questions = this.questionDB.getQuestionsByAudit(audit);
		}
		
		for(int i=1; i<=questions.size(); i++) {
			String reponseModifiee = r.getParameter("reponseModifiee"+i);
			if (r.getParameter("reponseID"+i) != null) {
				reponseDB.modifierReponse(reponseModifiee, Integer.parseInt(r.getParameter("reponseID"+i)));
			}
		}
	}
	
}
