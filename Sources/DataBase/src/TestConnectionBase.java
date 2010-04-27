import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;

public class TestConnectionBase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String DB_SRV_SQL = "localhost";
		final String DB_NAME = "killerbox";
		final String DB_USER = "admin_killerbox";
		final String DB_USER_PWD = "8p2TbGQ5DAWmSAy6";
		String pseudo = "User";
		String pwd = "User";
		long score = 2000;
		ConnexionBaseDonnee connection;
		
		
		try {
			connection = new ConnexionBaseDonnee(DB_SRV_SQL, DB_NAME, DB_USER, DB_USER_PWD);
			
			try {
				connection.ajouterUtilisateur(pseudo, pwd, false);
				System.out.println("Cr�ation du compte de " + pseudo);
			} catch (Exception e) {
				System.out.println("Cr�ation du compte impossible");
			}

			try {
				connection.setScore(pseudo, 2000);
				System.out.println("Set du score de " +pseudo + " � " + score); 
			} catch (Exception e) {
				System.out.println("Impossible de setter le score de " + pseudo);
			}
			
			try {
				System.out.println("Hash de test : " + connection.getHashConnexion(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de r�cup�rer le hash code de connexion du joueur " + pseudo);
			}
			
			try {
				System.out.println("Score de " + pseudo + " : " + connection.getScore(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de r�cup�rer le score de " + pseudo);
			}

			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						pwd+") est accept� : (attendu true) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de v�rifier l'utilisateur (pb de connexion) " + pseudo);
			}

			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						"hello"+") est accept� : (attendu false) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de v�rifier l'utilisateur (pb de connexion) " + pseudo);
			}
			
			try {
				connection.supprimerUtilisateur(pseudo);
				System.out.println("Suppression de l'utilisateur " + pseudo + " effectu�e");
			} catch (Exception e) {
				System.out.println("Impossible de supprimer l'utilisateur " + pseudo);
			}
			
			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						pwd+") est accept� : (attendu false) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de v�rifier l'utilisateur (pb de connexion) " + pseudo);
			}
			
			try {
				System.out.println("R�cup�ration du score de " + pseudo + " afin de voir s'il a bien �t� supprimer");
				System.out.println("Score de " + pseudo + " : " + connection.getScore(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de r�cup�rer le score de " + pseudo);
			}
		} catch (Exception e1) {
			System.out.println("Impossible de se connecter � la base");
		}
		
	}

}
