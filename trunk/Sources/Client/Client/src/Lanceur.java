/**
 * Permet de lancer les differents clients
 * @author	Valentin Delaye
 * @version	1.0
 */
public class Lanceur
{
	/**
	 * Programme principal
	 * @param args Les arguments du programme
	 */
	public static void main(String[] args)
	{
		Client c1 = new Client("127.0.0.1", 7000);
		Client c2 = new Client("127.0.0.1", 7000);
		
		new ClientGUI(c1);
		new ClientGUI(c2);
	}
}
