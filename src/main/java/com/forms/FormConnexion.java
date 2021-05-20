package com.forms;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;

import com.beans.Role;
import com.beans.Utilisateur;
import com.configuration.Configuration;
import com.dao.DaoFactory;
import com.dao.UtilisateurDao;

public class FormConnexion {
	private Utilisateur connectedUtilisateur;
	private String status;
	private UtilisateurDao daoUtilisateur;
	private String pssword = null;

	public FormConnexion(HttpServletRequest request) throws Exception {

		String email = request.getParameter("email");
		String mdp = request.getParameter("mdp");

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, Configuration.getString("host_adresse_ldap"));
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, Configuration.getString("login_ldap"));
		env.put(Context.SECURITY_CREDENTIALS, Configuration.getString("password_ldap"));

		// Create the initial context
		NamingEnumeration namingEnumeration = null;
		try {
			DirContext ctx = new InitialDirContext(env);
			try {
				SearchControls controls = new SearchControls();
				controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				namingEnumeration = ctx.search("ou=people,ou=projet,dc=openstack,dc=etudis,dc=eseo,dc=fr",
						"objectClass=person", controls);
				while (namingEnumeration.hasMore()) {
					SearchResult searchResult = (SearchResult) namingEnumeration.next();
					Attributes attributes = searchResult.getAttributes();
					String emailTemp = "";
					if (attributes.get("mail") != null) {
						emailTemp = String.valueOf(attributes.get("mail")).substring(6);
					}
					if (emailTemp.equals(email)) {
						this.pssword = new String((byte[]) attributes.get("userpassword").get());
						this.connectedUtilisateur = new Utilisateur(String.valueOf(attributes.get("sn")).substring(4),
								String.valueOf(attributes.get("givenname")).substring(11),
								String.valueOf(attributes.get("mail")).substring(6));
					}
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		if (this.connectedUtilisateur != null) {
			byte[] salt = this.getSalt(this.pssword);
			String passwordEncrypt = this.getSshaDigestFor(mdp, salt);
			if (passwordEncrypt.equals(this.pssword)) {
				this.status = "good";
				try {
					this.daoUtilisateur = DaoFactory.getInstance().getUtilisateurDao();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (!this.daoUtilisateur.exists(this.connectedUtilisateur)) {
					this.daoUtilisateur.ajouterUtilisateur(this.connectedUtilisateur);
					List<Utilisateur> listeUtilisateur = this.daoUtilisateur.getListUtilisateurs();
					int idUtilisateur = listeUtilisateur.get(listeUtilisateur.size() - 1).getId();
					this.daoUtilisateur.ajouterRoleUtilisateur(idUtilisateur, Role.INVITE);
					this.connectedUtilisateur = daoUtilisateur.getUtilisateurByEmail(email);
				} else {
					this.connectedUtilisateur = daoUtilisateur.getUtilisateurByEmail(email);
				}
			} else {
				this.status = "wrongPwd";
			}
		} else {
			this.status = "notFound";
		}
	}

	public Utilisateur getConnectedUser() {
		return this.connectedUtilisateur;
	}

	public String getStatus() {
		return this.status;
	}

	private byte[] getSalt(String encodedPasswordWithSSHA) {
		byte[] data = Base64.getMimeDecoder().decode(encodedPasswordWithSSHA.substring(6));
		return Arrays.copyOfRange(data, 20, data.length);
	}

	private String getSshaDigestFor(String password, byte[] salt) throws Exception {
		// create a SHA1 digest of the password + salt
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(password.getBytes(Charset.forName("UTF-8")));
		crypt.update(salt);
		byte[] hash = crypt.digest();

		// concatenate the hash with the salt
		byte[] hashPlusSalt = new byte[hash.length + salt.length];
		System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
		System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

		// prepend the SSHA tag + base64 encode the result
		return "{SSHA}" + Base64.getEncoder().encodeToString(hashPlusSalt);
	}
}
