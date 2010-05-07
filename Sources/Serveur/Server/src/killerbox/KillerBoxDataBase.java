package killerbox;

import java.math.*;
import java.security.*;
import java.sql.*;

public class KillerBoxDataBase
{

	/**
	 * Driver de connecion vers la base
	 */
	private Connection db_connection;

	/**
	 * Pour permettre de crypter le mot de passe MD5
	 */
	private String salt = "kjasj98398KJIFI--OIJ)93ije..!`^p3ihj3nds!!";

	/**
	 * Permet de creer une nouvelle connexion vers la base
	 * @param url URL vers la base de donnee
	 * @param name Nom de la base de donnee
	 * @param user Nom d'utilisateur de la base
	 * @param pass Mot de pass de la base de donee
	 * @throws Exception Si la connection est refusee
	 */
	public KillerBoxDataBase(String url, String name, String user, String pass)
			throws Exception
	{
		db_connection = DriverManager.getConnection("jdbc:mysql://" + url + "/" + name
				+ "?user=" + user + "&password=" + pass);
	}

	/**
	 * Permet de setter le score d'un jour
	 * @param pseudo Pseudo du joueur
	 * @param score Nouveau score
	 * @throws Exception Si erreur grave
	 */
	public void setScore(String pseudo, int score) throws Exception
	{
		Statement st = db_connection.createStatement();
		st.executeUpdate("UPDATE  `joueur` SET  `score` =  '" + score
				+ "' WHERE  `joueur`.`pseudo` =  '" + pseudo + "';");
	}

	/**
	 * Permet d'ajouter un utilisateur
	 * @param pseudo Nouvel utilisateur
	 * @param motDePasse Mot de passe
	 * @param administrateur True c'est un adminisatrateur, False sinon
	 * @throws Exception Si erreur grave
	 */
	public void ajouterUtilisateur(String pseudo, String motDePasse, boolean administrateur)
			throws Exception
	{

		Statement st = db_connection.createStatement();
		st
				.executeUpdate("INSERT INTO `joueur` (`pseudo`, `administrateur`, `hashConnexion`, `score`) VALUES ('"
						+ pseudo
						+ "', '"
						+ (administrateur ? 1 : 0)
						+ "', '"
						+ creerHashConnexion(pseudo, motDePasse) + "', '0');");
	}

	/**
	 * Indique si un utilisateur est un admin ou non
	 * @param pseudo
	 * @return
	 */
	public boolean isAdmin(String pseudo)
	{

		try
		{
			Statement st = db_connection.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT `administrateur` FROM `joueur` WHERE `joueur`.`pseudo` = '"
							+ pseudo + "';");
			// Si trouve
			if (rs.first())
				return rs.getInt(1) == 1;

			// Non trouve
			else
				return false;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	/**
	 * Permet de supprimer un utilisateur
	 * @param pseudo Pseudo a supprimer
	 * @throws SQLException Si erreur grave
	 */
	public void supprimerUtilisateur(String pseudo) throws SQLException
	{
		Statement st = db_connection.createStatement();
		st
				.executeUpdate("DELETE FROM `joueur` WHERE `joueur`.`pseudo` = '" + pseudo
						+ "';");
	}

	/**
	 * Permet de retourne le hash de connexion
	 * @param pseudo Pseudo
	 * @return Le hash de connexion
	 * @throws Exception Si erreur grave
	 */
	public String getHashConnexion(String pseudo) throws Exception
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT joueur.hashConnexion FROM joueur WHERE joueur.pseudo =  '"
						+ pseudo + "'");
		if (rs.first())
			return rs.getString(1);
		else
			throw new Exception("Compte inexistant");
	}

	/**
	 * Permet de retourner le score d'un jour
	 * @param pseudo Le pseudo
	 * @return Le score du joueur
	 * @throws SQLException Si erreur grave
	 */
	public int getScore(String pseudo) throws SQLException
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT joueur.score FROM joueur WHERE joueur.pseudo =  '"
						+ pseudo + "'");
		rs.first();

		return rs.getInt(1);
	}

	/**
	 * Indique si l'utilisateur est valide
	 * @param pseudo Nom d'utilisateur
	 * @param motDePasse Mot de passe
	 * @return True ok, False sinon
	 * @throws Exception Si impossible de recuperer le hash sur la base de donnee
	 */
	public boolean verifierUtilisateur(String pseudo, String motDePasse)
	{
		try
		{
			return creerHashConnexion(pseudo, motDePasse).equals(getHashConnexion(pseudo));
		}

		catch (Exception e)
		{
			return false;
		}

	}

	/**
	 * Permet de retourner les scores de tout les utilisateurs. C'est a dire
	 * le nom d'utilisateur, le score et si l'utilisateur est admin.
	 * Les informations sont mises a la suite separe par des tokens
	 */
	public String getAll() throws SQLException
	{
		// Execute la requete
		ResultSet rs = null;
		Statement st = db_connection.createStatement();
		rs = st
				.executeQuery("SELECT joueur.pseudo, joueur.administrateur, joueur.score FROM joueur");

		StringBuilder build = new StringBuilder();

		while (rs.next())
			build.append("#" + rs.getString(1) + "#" + rs.getString(2) + "#"
					+ rs.getString(3));

		return build.toString();

	}
	
	/**
	 * Permet a un utilisateur de modifier son mot de passe
	 * @param pseudo Pseudo de l'utilisateur
	 * @param pass
	 * @return
	 */
	public boolean modifierPass(String pseudo, String pass)
	{
		try
		{
			Statement st = db_connection.createStatement();			
			st.executeUpdate("UPDATE `joueur` SET `hashConnexion` = '" + creerHashConnexion(pseudo, pass) + "' WHERE `joueur`.`pseudo` = '" + pseudo + "';");
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
		
		return true;
	}

	/**
	 * Permet de creer le hash de connexion
	 * @param pseudo Nom d'utilisateur
	 * @param motDePasse Mot de passe
	 * @return Le hash de connexion
	 */
	private String creerHashConnexion(String pseudo, String motDePasse)
	{
		return crypterStringMD5(pseudo + salt + motDePasse);
	}

	/**
	 * Methode permettant de crypter un string en md5
	 * @param stringACrypter
	 * @return String Hash MD5 du string d'entree
	 * @throws Exception
	 */
	private String crypterStringMD5(String stringACrypter)
	{
		String stringMD5 = null;

		MessageDigest m = null;

		try
		{
			m = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		m.update(stringACrypter.getBytes());

		stringMD5 = (new BigInteger(1, m.digest())).toString(16);

		// Ajouter d'eventuels 0 au debut de la chaine
		if (stringMD5.length() < 32)
		{
			// On rajoute des 0
			for (int i = 0; i < 32 - stringMD5.length(); i++)
				stringMD5 = "0" + stringMD5;
		}

		return stringMD5;
	}
}
