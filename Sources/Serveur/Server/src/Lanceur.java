import killerbox.*;


/**
 * Programme principal.
 * Permet de creer et de lancer un serveur.
 * @author Valentin Delaye
 * @version 1.0
 * @see KillerBoxServer
 * @see KillerBoxDecoder
 */
public class Lanceur
{
	/**
	 * Programme principal
	 * @param args Les arguements du programme
	 */
	public static void main(String[] args)
	{
		
		KillerBoxDecoder decoder = new KillerBoxDecoder();
		KillerBoxServer server = new KillerBoxServer(7000, decoder, "killerbox", "1234");
		
		// Affichage sur la console et GUI
		new ServerGui(server);
		new ServerConsole(server);
		
		// Demarer le serveur
		server.start();

	}
}
