/*
Fichier:  Noel.java
Rem:     
	Exemple d'utilisation du modèle Observer/Observable
	Exécuter le programme en application autonome
          Ou éxécuter le programme en mode Applet en ouvrant une fenêtre de 315*315

          Le répertoire contenant les classes à exécuter doit contenir les 3 fichiers image:
          "Lune.gif", "Montagne.gif" et "PNoel.gif"


Date :	  Eric Lefrançois, Janvier 2002
Modification : Michaël Sanodz Février 2010
*/

package jeu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Inteface definissant les grandeur de la fenetre et le nombre maximum 
 * de flocons
 */
// -----------------------------------------------------------------------------
interface Parametres {
	// Constantes globales:  taille des composants

	int PG_X = 315;   // Taille en X du panneau graphique
	int PG_Y = 315;   // Taille en Y du panneau graphique

	// Flocons
	int MAX_FLOCONS = 300;   // Nombre max. de flocons
 
}

/**
 * Classe definissant une couche de flocons avec un tableau repertoriant la
 * hauteur de la couche pour chaque pixel dans la largeur de la fenetre
 * 
 * implemente les intefaces Parametres et Observer et derive d'Observable
 */
// -----------------------------------------------------------------------------
class CoucheNeige extends Observable implements Parametres, Observer{
	
	private int[] couche = new int[PG_X];
	
	/**
	 * Constructeur de la couche qui initialise la hauteur a 0
	 */
	public CoucheNeige(){
		for (int i = 0; i < PG_X; i++) {
			couche[i] = 0;
		}
	}
	
	/**
	 * dessine la couche dans la fenetre
	 * 
	 * @param g object graphique sur lequel on dessine
	 */
	public void dessiner(Graphics g) {
		lisser();
		for(int i=0; i< PG_X; i++){
			g.setColor(Color.white);
			g.fillRect(i, PG_Y - couche[i]-1, 1, couche[i]-1);
			g.setColor(Color.lightGray);
			g.drawRect(i, PG_Y - couche[i]-1, 1, couche[i]-1);
		}
	}
	
	/**
	 * permet d'aplanir la couche et d'eviter ainsi des gros pics
	 */
	public void lisser(){
		for(int i = 1; i < PG_X-1; i++)
		{	
			while(couche[i]> couche[i-1]+ 1)
			{
				couche[i]--;
				couche[i-1]++;
			}
			while(couche[i]> couche[i+1]+ 1)
			{
				couche[i]--;
				couche[i+1]++;
			}
		}
	}
	
	/**
	 * mise a jour a faire lorsque un flocon bouge, on regarde s'il a atteint la
	 * couche et on augment sa hauteur si c'est le cas
	 */
	public void update(Observable o, Object arg) {
		if(((Flocon)arg).getX() > 0 && ((Flocon)arg).getX() < PG_X)
			if(couche[((Flocon)arg).getX()] + ((Flocon)arg).getHauteur() >= PG_Y - ((Flocon)arg).getY() -((Flocon)arg).getHauteur())
			{
				for(int i = 0; i <((Flocon)arg).getLargeur(); i++)
					couche[((Flocon)arg).getX() + i] += ((Flocon)arg).getHauteur()/2;
				setChanged();
				notifyObservers();
			}
	}
	
}

/**
 * Classe definissant la chute des flocons
 */
class ChuteNeige extends Observable implements Runnable, Parametres, Observer { 
   
	
	/**
	 * @label possede
	 */
	
	private Flocon[] flocons  = new Flocon [MAX_FLOCONS];
	private int nbFlocons;    // Nombre de flocons créés
	private Thread activite;

	/**
	 * Constructeur qui initialise un thread pour la chute
	 */
	public ChuteNeige () {
		activite = new Thread (this);
		activite.start();
	}

	/**
	 * Cree les flocons au debut et lorsqu'un flocon meurt
	 */
	public void run () {
	// Activité limitée à la création des flocons
		while (nbFlocons < MAX_FLOCONS) {
			try {Thread.sleep(10);}catch (InterruptedException e) {}
			flocons[nbFlocons]=new Flocon(); // Créer un nouveau flocon
			flocons[nbFlocons].addObserver(this);
			nbFlocons++;	
		}
	}

	/**
	 * dessine la chute de flocons
	 */
	public void dessiner(Graphics g) {
		for (int i = 0; i < nbFlocons; i++) {
			flocons[i].dessiner(g);
		}
	}

	/**
	 * mise a jour a faire lorsque un flocon bouge, on le signale a l'observeur
	 */
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(o);
	}
}


// -----------------------------------------------------------------------------
/**
 * Classe definisant un flocon avec sa position (x,y), sa largeur, sa hauteur et
 * sa vitesse
 */
class Flocon extends Observable implements Runnable, Parametres{
	
	private int x, y; 			// Coordonnées courantes du flocon
	private int largeur; 		// largeur du flocon (en pixels)
	private int hauteur; 		// hauteur du flocon (en pixels)
	private int vitesse; 		// DeltaT entre deux déplacements en Y

	private Thread activite;
	  
	// Un générateur de nombres aléatoires  
	public static Random rdGen = new Random();

	/**
	 * Constructeur qui indique initialise les attributs du flocon
	 */
	public Flocon () {
		x = rdGen.nextInt(PG_X);
		y = 0;
		largeur = 1+rdGen.nextInt(3);    // Génération nb aléat. entre 1 et 4
		hauteur = largeur + rdGen.nextInt(2);
		vitesse = hauteur;                   // Vitesse directement fonction de la taillE
			  
		activite = new Thread(this);
		activite.start();
	}

	/**
	 *  retourne la coordonée en X du flocon
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * retourne la coordonée en Y du flocon
	 */
	public int getY() {
		return y;
	}
	
	/**
	 *  retourne la largeur du flocon
	 */
	public int getLargeur() {
		return largeur;
	}
	
	/**
	 *  retourne la hauteur du flocon
	 */
	public int getHauteur() {
		return hauteur;
	}

	public synchronized void deplacer () {
	// Synchronisation nécessaire:  les coordonnées du flocon sont une
	// ressource critique (dessin et mise à jour simultanés)
		boolean aDroite = rdGen.nextInt(2)==1;
		x = aDroite ? x+1 : x-1;
		y += vitesse ;
		if (y+hauteur> PG_Y) {
			x = rdGen.nextInt(PG_X);
			y = 0;
		}
	}

	/**
	 * gere le deplacement des flocons
	 */
	public void run () {
		while (true) {
			try {Thread.sleep(5*(7-vitesse));}catch(InterruptedException e) {}
			setChanged();
			notifyObservers();
			deplacer();
		}
	}

	public synchronized void dessiner (Graphics g) {
	// Synchronisation nécessaire:  les coordonnées du flocon sont une
	// ressource critique  (dessin et mise à jour simultanés)	
		g.setColor(Color.white);
		g.fillRect(x, y, largeur, hauteur);
		g.setColor(Color.lightGray);
		g.drawRect(x, y, largeur, hauteur);
	}

}

/**
 * Classe qui definit un pere noel
 */
// -----------------------------------------------------------------------------
class PereNoel extends Observable implements Runnable, Parametres {	
	private int x;
	private Thread activite;
	private int y;
           // Coordonnées courantes du flocon
	private  Image imagePereNoel;		 

    //private Noel lnkNoel;
	
    /**
     * Constructeur par default
     */
	public PereNoel () {
		imagePereNoel = Toolkit.getDefaultToolkit().getImage ("PNoel.gif");
		activite = new Thread (this);
		activite.start();
	}

	/**
	 * gere les deplacement du pere noel
	 */
	public void run () {
		x = 0;
		y = PG_Y/4;
		while (true) {
			setChanged();
			notifyObservers();
			try {Thread.sleep(50);}catch (InterruptedException e) {}
			x += 2;
			if (x > PG_X+100) x = -200;
			
		}
	}

	/**
	 * dessine le pere noel
	 */
	public void dessiner(Graphics g, ImageObserver imObs) {
		g.drawImage (imagePereNoel, x, y, imObs);
	}

}
//------------------------------------------------------------------------------
class PanneauGraphique extends JPanel implements Parametres, Observer {
// Vue passive, pour affichage uniquement


	
	/**
	 * @label affiche
	 */
	
	private ChuteNeige a_chuteNeige;
	
	 // Chute de neige
	
	
	/**
	 * @label affiche
	 */
	
	private CoucheNeige a_coucheNeige;

	
	/**
	 * @label affiche
	 */
	
	private PereNoel a_lePereNoel;           	// Père Noël
	
	private Image imageDeFond, imageLune;		 // Images de fond



// Constructeur
	public PanneauGraphique (ChuteNeige chNg, PereNoel lePereNoel, CoucheNeige coucheNeige){
		a_chuteNeige = chNg;
		a_lePereNoel = lePereNoel;
		a_coucheNeige = coucheNeige;
		
		a_lePereNoel.addObserver(this);
		a_chuteNeige.addObserver(this);
		a_coucheNeige.addObserver(this);
		
		a_chuteNeige.addObserver(a_coucheNeige);
		
		setBackground(new Color (0, 0, 65));
		// Chargement de l'image Montagne.gif, située dans le répertoire
		// des classes.  Image de taille 315*44 pixels
		imageDeFond = Toolkit.getDefaultToolkit().getImage ("Montagne.gif");

		// Chargement des images Lune etPère Noel
	       imageLune = Toolkit.getDefaultToolkit().getImage ("Lune.gif");
       

	}

	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.drawImage (imageDeFond, 0, PG_Y-44, this);
		g.drawImage (imageLune, PG_X-100, PG_Y/4, this);
		a_chuteNeige.dessiner(g);
		a_coucheNeige.dessiner(g);
		a_lePereNoel.dessiner(g, this); 
		/*Note .......
		Le "this", 4ème paramètre de drawImage représente "l'image observer"".
		Cet objet contrôle le chargement de l'image en mémoire (chargée
		habituellement depuis un fichier).  Il est responsable de dessiner
		cette image de manière asynchrone au reste du programme, au fur et à
		mesure que l'image se charge.
		Ainsi, le programmeur peut donner l'ordre de charger une image ("getImage"),
		puis il peut la dessiner aussitôt (drawImage), sans attendre qu'elle
		soit chargée.  La procédure drawImage retourne aussitôt.
		L'image observer est implémenté par la classe Component (dont hérite
		la classe Canvas).
		Le cas échéant, il est possible de redéfinir cet objet, ce qui permettrait
		de contrôler le chargement de l'image, d'attendre qu'elle soit entièrement
		chargée avant de l'afficher, etc...
		*/
     }

   public Dimension getPreferredSize() {
   // Retourne la taille souhaitée pour le composant (remplace le "getSize")
        return new Dimension (PG_X, PG_Y);
   }

	public void update(Observable o, Object arg) {
	// Mise à jour a effectuer lorsque l'objet bouge
		repaint();
	}
}
//------------------------------------------------------------------------------
public class Noel extends JApplet {
// Programme "Principal" -->  mise en place du programme
// Création des "modèles", des "vues"
// Associations diverses


	// Vue
   private PanneauGraphique panneauGraphique;     			

   private ChuteNeige chuteDeNeige = new ChuteNeige();

   // Modèle ("observé")
   
/**
 * @link composition
 */

   private PereNoel lePereNoel = new PereNoel();
   
   private CoucheNeige coucheNeige = new CoucheNeige();
	   
   public Noel () {}

   public void init () {
        getContentPane().setLayout (new BorderLayout ());

        // Création des vues, eventuellement associées aux modèles
        panneauGraphique = new PanneauGraphique (chuteDeNeige, lePereNoel, coucheNeige);

        // Positionnement des composants
        getContentPane().add (panneauGraphique, "Center");    // Ajout du panneau graphique au centre
   }
   public void start() {}

   public static void main (String[] arg) {
   // Point d'entrée du programme
	System.out.println("Bonjour !");
	JFrame f = new JFrame ();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setTitle("Chablon");
	Noel a = new Noel();
	f.getContentPane().add (a, "Center");
	a.init();
	f.pack();
	f.setResizable(false);
	f.setVisible(true);
	a.start();
  }
}
//------------------------------------------------------------------------------


