import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionBaseDonnee {

	private Connection db_connection;
	private String salt = "kjasj98398KJIFI--OIJ)93ije..!`^p3ihj3nds!!";
	
	public ConnexionBaseDonnee(String DB_SRV_SQL,String DB_NAME, String DB_USER, String DB_USER_PWD) throws Exception
	{
		db_connection = DriverManager.getConnection(
				"jdbc:mysql://"+ DB_SRV_SQL +
				"/"+DB_NAME+
				"?user="+DB_USER+
				"&password="+ DB_USER_PWD);
	}
	
	public void setScore(String pseudo, long score) throws Exception
	{
		Statement st = db_connection.createStatement();
		st.executeUpdate("UPDATE  `joueur` SET  `score` =  '"+ score +"' WHERE  `joueur`.`pseudo` =  '" + pseudo + "';");
	}

	public void ajouterUtilisateur(String pseudo, String motDePasse, boolean administrateur) throws Exception {		

		Statement st = db_connection.createStatement();
		st.executeUpdate("INSERT INTO `joueur` (`pseudo`, `administrateur`, `hashConnexion`, `score`) VALUES ('"+pseudo+"', '"+(administrateur?1:0)+"', '"+creerHashConnexion(pseudo, motDePasse)+"', '0');");
	}

	public void supprimerUtilisateur(String pseudo) throws SQLException {
		Statement st = db_connection.createStatement();
		st.executeUpdate("DELETE FROM `joueur` WHERE `joueur`.`pseudo` = '"+pseudo+"';");
	}
	
	public String getHashConnexion(String pseudo) throws Exception
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT joueur.hashConnexion FROM joueur WHERE joueur.pseudo =  '" + pseudo + "'");
		if (rs.first())
			return rs.getString(1);
		else
			throw new Exception("Compte inexistant");
	}
	
	public long getScore(String pseudo) throws SQLException
	{
		Statement st = db_connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT joueur.score FROM joueur WHERE joueur.pseudo =  '" + pseudo + "'");
		rs.first();
		return rs.getLong(1);
	}
	
	/**
	 * 
	 * @param pseudo
	 * @param motDePasse
	 * @return
	 * @throws Exception Si impossible de récupérer le hash sur la base de donnée
	 */
	public boolean verifierUtilisateur(String pseudo, String motDePasse) throws Exception
	{
		return creerHashConnexion(pseudo, motDePasse).equals(getHashConnexion(pseudo));
	}
	
	private String creerHashConnexion(String pseudo, String motDePasse)
	{
		// hashConnexion =  hash(pseudo+salt+motdepasse)
		return crypterStringMD5(pseudo+salt+motDePasse);
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
	    
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	    m.update(stringACrypter.getBytes());
	    
	    stringMD5 = (new BigInteger(1,m.digest())).toString(16);
	    
	    // Ajouter d'éventuels 0 au début de la chaine
	    if (stringMD5.length() < 32)
	    {
	    	// On rajoute des 0
	    	for (int i = 0; i < 32 - stringMD5.length(); i++)
	    		stringMD5 = "0" + stringMD5;
	    }
	    
	    return stringMD5;
	}
}
