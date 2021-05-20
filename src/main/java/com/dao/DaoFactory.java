package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.configuration.Configuration;

public class DaoFactory {

	private String url;
	private String username;
	private String password;

	DaoFactory(String url2, String username2, String password2) {
		this.url = url2;
		this.username = username2;
		this.password = password2;
	}

	public static DaoFactory getInstance() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new DaoFactory(Configuration.getString("url_bdd"), Configuration.getString("login_bdd"),
				Configuration.getString("password_bdd"));

	}

	// Récupération du Dao
	public UtilisateurDao getUtilisateurDao() throws SQLException {
		return new UtilisateurDaoImpl(this.getConnection());
	}

	public ModeleDao getModeleDao() throws SQLException {
		return new ModeleDaoImpl(this.getConnection());
	}

	public AuditDao getAuditDao() throws SQLException {
		return new AuditDaoImpl(this.getConnection());
	}

	public QuestionDao getQuestionDao() throws SQLException {
		return new QuestionDaoImpl(this.getConnection());
	}

	public ReponseDao getReponseDao() throws SQLException {
		return new ReponseDaoImpl(this.getConnection());
	}

	public UeDao getUeDao() throws SQLException {
		return new UeDaoImpl(this.getConnection());
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

}
