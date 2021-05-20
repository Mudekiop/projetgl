package com.dao;

import java.util.List;
import com.beans.Ue;
import com.beans.Utilisateur;

public interface UeDao {

	List<Ue> listerUe();
	Ue getUeById(int id);
	List<Ue> listerUeByResponsableUe(Utilisateur utilisateur);
}
