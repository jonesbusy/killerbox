package killerbox;

import java.math.*;
import java.security.*;
import java.sql.*;

/**
 * Permet de representer une connexion a la base de donnee killerbox. La classe
 * fourni egalement differentes methodes pour agir avec la base de donnee MySQL.
 * @author Jonas Berdoz
 * @author Valentin Delaye
 * @version 1.0
 */
public class DataBase
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
	 * Constructeur. Permet de creer une nouvelle connexion vers la base
	 * @param url URL vers la base de donnee
	 * @param name Nom de la base de donnee
	 * @param user Nom d'utilisateur de la base
	 * @param pass Mot de pass de la base de donee
	 * @throws SQLException Si la connection est refusee
	 */
	public DataBase(String url, String name, String user, String pass)
			throws SQLException
	{
		db_connection = DriverManager.getConnection("jdbc:mysql://" + url + "/" + name
				+ "?user=" + user + "&password=" + pass + "&autoReconnect=true");
	}

	/**
	 * Permet d'ajouter un utilisateur
	 * @param user Nouvel utilisateur
	 * @param pass Mot de passe
	 * @param admin Pour indiquer si c'est un administrateur
	 * @throws SQLException Si erreur grave
	 */
	public void addUser(String user, String pass, boolean admin)
			throws SQLException
	{

		Statement st = db_connection.createStatement();
		st
				.executeUpdate("INSERT INTO `joueur` (`pseudo`, `administrateur`, `hashConnexion`, `score`) VALUES ('"
						+ user
						+ "', '"
						+ (admin ? 1 : 0)
						+ "', '"
						+ createHash(user, pass) + "', '0');");
	}

	/**
	 * Indique si un utilisateur est un administrateur ou non.
	 * @param user Le nom d'utilisateur.
	 * @return True c'est un administrateur, false si le nom c'est pas trouve ou ce n'est
	 *         pas un administrateur.
	 */
	public boolean isAdmin(String user)
	{

		try
		{
			Statement st = db_connection.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT `administrateur` FROM `joueur` WHERE `joueur`.`pseudo` = '"
							+ user + "';");
			// Si trouve
			if (rs.first())
				return rs.getInt(1) == 1;

			// Non trouve
			else
				return false;
		}
		catch (SQLException e)
		{
			return false;
		}

	}

	/**
	 * Permet de supprimer un utilisateur.
	 * @param user Nom d'utilisateur a supprimer
	 * @throws SQLException Si erreur grave
	 */
	public void deleteUser(String user) throws SQLException
	{
		Statement st = db_connection.createStatement();
		st
				.executeUpdate("DELETE FROM `joueur` WHERE `joueur`.`pseudo` = '" + user
						+ "';");
	}

	/**
	 * Permet de retourne le hash de connexion d'un certain utilisateur
	 * @param user Nom d'utilisateur
	 * @return Le hash de connexion ou null si le nom d'utilisateur n'est pas trouve
	 * @throws SQLException Si erreur grave
	 */
	public String getHash(String user) throws SQLException
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT joueur.hashConnexion FROM joueur WHERE joueur.pseudo =  '"
						+ user + "'");
		
		if (rs.first())
			return rs.getString(1);
		
		else
			return null;
	}
	
	/**
	 * Permet de setter le score d'un joueur.
	 * @param user Le nom d'utilisateur
	 * @param score Nouveau score
	 * @throws SQLException Si erreur grave
	 */
	public void setScore(String user, int score) throws SQLException
	{
		Statement st = db_connection.createStatement();
		st.executeUpdate("UPDATE  `joueur` SET  `score` =  '" + score
				+ "' WHERE  `joueur`.`pseudo` =  '" + user + "';");
	}

	/**
	 * Permet de retourner le score d'un joueur.
	 * @param user Le nom d'utilisateur
	 * @return Le score du joueur. -1 Si l'utilisateur n'est pas trouve.
	 * @throws SQLException Si erreur grave
	 */
	public int getScore(String user) throws SQLException
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st
				.executeQuery("SELECT joueur.score FROM joueur WHERE joueur.pseudo =  '"
						+ user + "'");
		
		if(rs.first())
			return rs.getInt(1);
		else
			return -1;
	}

	/**
	 * Indique si les information de login sont valides
	 * @param user Nom d'utilisateur
	 * @param pass Mot de passe
	 * @return True les informations sont correctes, false sinon
	 * @throws Exception Si impossible de recuperer le hash sur la base de donnee
	 */
	public boolean isUserValid(String user, String pass)
	{
		try
		{
			return createHash(user, pass).equals(getHash(user));
		}

		catch (SQLException e)
		{
			return false;
		}

	}

	/**
	 * Permet de retourner les scores de tout les utilisateurs. C'est a dire
	 * le nom d'utilisateur, le score et si l'utilisateur est admin.
	 * Les informations sont mises a la suite separe par des tokens.
	 * @throws SQLException Si erreur grave
	 */
	public String getInfos() throws SQLException
	{
		// Execute la requete
		ResultSet rs = null;
		Statement st = db_connection.createStatement();
		rs = st
				.executeQuery("SELECT joueur.pseudo, joueur.administrateur, joueur.score FROM joueur");

		StringBuilder build = new StringBuilder();

		// Creer la chaine
		while (rs.next())
			build.append("#" + rs.getString(1) + "#" + rs.getString(2) + "#"
					+ rs.getString(3));

		return build.toString();

	}

	/**
	 * Permet de modifier le mot-de-passe d'un utilisateur.
	 * @param pseudo Pseudo de l'utilisateur
	 * @param pass Nouveau mot-de-passe
	 * @return True si tout s'est bien passe, false sinon
	 */
	public boolean modifyPass(String pseudo, String pass)
	{
		try
		{
			Statement st = db_connection.createStatement();
			st.executeUpdate("UPDATE `joueur` SET `hashConnexion` = '"
					+ createHash(pseudo, pass) + "' WHERE `joueur`.`pseudo` = '"
					+ pseudo + "';");
		}
		catch (SQLException e)
		{
			return false;
		}

		return true;
	}

	/**
	 * Permet de creer le hash de connexion. Permet de stocket de facon securisee
	 * le mot de passe de l'utilisateur dans la base de donnee.
	 * @param user Nom d'utilisateur
	 * @param pass Mot de passe
	 * @return Le hash de connexion
	 */
	private String createHash(String user, String pass)
	{
		return crypt(user + this.salt + pass, "MD5");
	}

	/**
	 * Methode permettant de crypter une String a l'aide de l'algorithme MD5
	 * @param string Chaine a crypter
	 * @return Hash MD5 du string d'entree. Null si cryptage impossible
	 */
	private String crypt(String string, String algorithm)
	{
		String encode = null;

		MessageDigest m = null;

		try
		{
			m = MessageDigest.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}

		m.update(string.getBytes());

		encode = (new BigInteger(1, m.digest())).toString(16);

		// Ajouter d'eventuels 0 au debut de la chaine
		if (encode.length() < 32)
		{
			// On rajoute des 0
			for (int i = 0; i < 32 - encode.length(); i++)
				encode = "0" + encode;
		}

		return encode;
	}
}
