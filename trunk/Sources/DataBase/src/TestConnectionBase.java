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
				System.out.println("Création du compte de " + pseudo);
			} catch (Exception e) {
				System.out.println("Création du compte impossible");
			}

			try {
				connection.setScore(pseudo, 2000);
				System.out.println("Set du score de " +pseudo + " à " + score); 
			} catch (Exception e) {
				System.out.println("Impossible de setter le score de " + pseudo);
			}
			
			try {
				System.out.println("Hash de test : " + connection.getHashConnexion(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de récupérer le hash code de connexion du joueur " + pseudo);
			}
			
			try {
				System.out.println("Score de " + pseudo + " : " + connection.getScore(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de récupérer le score de " + pseudo);
			}

			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						pwd+") est accepté : (attendu true) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de vérifier l'utilisateur (pb de connexion) " + pseudo);
			}

			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						"hello"+") est accepté : (attendu false) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de vérifier l'utilisateur (pb de connexion) " + pseudo);
			}
			
			try {
				connection.supprimerUtilisateur(pseudo);
				System.out.println("Suppression de l'utilisateur " + pseudo + " effectuée");
			} catch (Exception e) {
				System.out.println("Impossible de supprimer l'utilisateur " + pseudo);
			}
			
			try {
				System.out.println("L'utilisateur ("+
						pseudo + ") avec mot de passe ("+
						pwd+") est accepté : (attendu false) :"+ 
						connection.verifierUtilisateur(pseudo, pwd));
			} catch (Exception e) {
				System.out.println("Impossible de vérifier l'utilisateur (pb de connexion) " + pseudo);
			}
			
			try {
				System.out.println("Récupération du score de " + pseudo + " afin de voir s'il a bien été supprimer");
				System.out.println("Score de " + pseudo + " : " + connection.getScore(pseudo));
			} catch (Exception e) {
				System.out.println("Impossible de récupérer le score de " + pseudo);
			}
		} catch (Exception e1) {
			System.out.println("Impossible de se connecter à la base");
		}
		
	}

}
