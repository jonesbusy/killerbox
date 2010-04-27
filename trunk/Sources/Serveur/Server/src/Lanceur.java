import network.Decoder;
import killerbox.KillerBoxDecoder;
import killerbox.KillerBoxServer;;


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
		Decoder decoder = new KillerBoxDecoder();
		KillerBoxServer server = new KillerBoxServer(7000, decoder);
		
		@SuppressWarnings("unused")
		ServerGui s1 = new ServerGui(server);
		
		//@SuppressWarnings("unused")
		//ServerConsole s2 = new ServerConsole(server);
		
		
		// Demarer le serveur
		server.start();

	}
}
