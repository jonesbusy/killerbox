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
	 * Permet de creer le hash de connexion
	 * @param pseudo Nom d'utilisateur
	 * @param motDePasse Mot de passe
	 * @return
	 */
	private String creerHashConnexion(String pseudo, String motDePasse)
	{
		// hashConnexion = hash(pseudo+salt+motdepasse)
		return crypterStringMD5(pseudo + salt + motDePasse);
	}

	/**
	 * Méthode permettant de crypter un string en md5
	 * @param stringACrypter
	 * @return String Hash MD5 du string d'entrée
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
