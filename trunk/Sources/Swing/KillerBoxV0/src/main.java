
public class main 
{
	private static int hauteur = 400;
	private static int largeur = 400;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		FenetreBase fb 			= new FenetreBase(hauteur,largeur);
		FenetrePrincipale fp 		= new FenetrePrincipale(hauteur,largeur);
		FenetreUser fu			= new FenetreUser(hauteur,largeur);
		FenetreAdmin fa 			= new FenetreAdmin(hauteur, largeur);
		FenetreConnection fc		= new FenetreConnection(hauteur,largeur);
		FenetreComptes fcptes		= new FenetreComptes(hauteur,350);
	}
}
