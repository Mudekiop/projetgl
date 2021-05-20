package com.dao;

import java.util.List;

import com.beans.Audit;
import com.beans.Modele;
import com.beans.Question;

public interface QuestionDao {

	List<Question> getQuestionByModele(Modele modele);
	
	List<Question> getQuestionsByAudit(Audit audit);
	
	Question getQuestionById(int id);
}
