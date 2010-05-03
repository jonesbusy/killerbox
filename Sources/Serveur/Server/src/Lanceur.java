import killerbox.*;


/**
 * Permet de creer 
 * @author valentin
 *
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
		KillerBoxServer server = new KillerBoxServer(7000, decoder, "valentin", "1234");
		
		@SuppressWarnings("unused")
		ServerConsole s1 = new ServerConsole(server);
		
		// Demarer le serveur
		server.start();
		
		//@SuppressWarnings("unused")
		//ServerConsole s2 = new ServerConsole(server);
		
	

	}
}
