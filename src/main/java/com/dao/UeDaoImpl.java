package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.beans.Ue;
import com.beans.Utilisateur;

public class UeDaoImpl implements UeDao{

	private Connection connexion;
	
	public UeDaoImpl(Connection connexion) {
		super();
		this.connexion = connexion;
	}
	
	@Override
	public List<Ue> listerUe(){
		List<Ue> liste = new ArrayList<Ue>();
		
		try {
			PreparedStatement statement = connexion.prepareStatement("select * from ue");
			ResultSet resultat = statement.executeQuery();
			while(resultat.next()) {
				Ue ue = new Ue(resultat.getInt("id"), resultat.getString("titre"));
				liste.add(ue);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return liste;
	}
	
	@Override
	public Ue getUeById(int id) {
		Ue ue = null;
		try {
			PreparedStatement statement = connexion.prepareStatement("select * from ue where id=?");
			statement.setInt(1, id);
			ResultSet resultat = statement.executeQuery();
			while(resultat.next()) {
				ue = new Ue(resultat.getInt("id"), resultat.getString("titre"));
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return ue;
	}
	
	//RECUPERER QUE LES UE DU RESP UE
	@Override
	public List<Ue> listerUeByResponsableUe(Utilisateur utilisateur){
		List<Ue> liste = new ArrayList<Ue>();
		
		try {
			PreparedStatement statement = connexion.prepareStatement("SELECT "
					+ "ue.* "
					+ "FROM ue ue "
					+ "inner join estresponsableue erue on erue.id_Ue=ue.id "
					+ "inner join utilisateur u on u.id=erue.id_Utilisateur "
					+ "where u.id=?");
			statement.setInt(1, utilisateur.getId());
			ResultSet resultat = statement.executeQuery();
			while(resultat.next()) {
				Ue ue = new Ue(resultat.getInt("id"), resultat.getString("titre"));
				liste.add(ue);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return liste;
	}
	
}
