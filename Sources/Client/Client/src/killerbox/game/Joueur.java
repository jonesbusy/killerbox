package killerbox.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.StringTokenizer;


public class Joueur { // extends Observable {

	private static final int HEIGHT_BARRE_VIE = 6;
	private static final int DESSUS_JOUEUR_BARRE_VIE = 5;
	

	private final boolean DEBUG = false;
	
	// attributs
	/**
	 * nom du joueur
	 */
	private String nom;
	
	 /**
	  * coordonnée du joueur
	  */
	private double angleSourisJoueur;
	private int vitesse = 3;

	/**
	 * points de vie du joueur
	 */
	private int pv;
	private int pvMax;
	
	/**
	 * image du joueur
	 */
	private Image imageJoueur;
	private Rectangle rectJoueur;
	
	/**
	 * Pour constuire des chaine représentant le joueur (delim)
	 */
	private char delim = '#';
	private boolean mort;

	// constructeur 
	/**
	 * Constructeur à 4 arguments de la classe joueur
	 * 
	 * @param nom nom du joueur
	 * @param posX coordonnée en x du joueur
	 * @param posY coordonnée en y du joueur
	 * @param pv points de vie du joueur
	 */
	public Joueur(String nom, int posX, int posY, int pv) {
		this.nom = nom;
		this.pv = pv;
		angleSourisJoueur = 0;
		imageJoueur = Toolkit.getDefaultToolkit().getImage("playerOK.gif");
		rectJoueur = new Rectangle(0, 0, 36, 29);
		this.pvMax = pv;
		setPos(posX, posY);
	}
	
	/**
	 * Constructeur prévu pour une chaine de caractère.
	 * Pratique pour le réseau.
	 * @return
	 */
	public Joueur(String joueur)
	{
		StringTokenizer token = new StringTokenizer(joueur, String.valueOf(delim));

		angleSourisJoueur = 0;
		imageJoueur = Toolkit.getDefaultToolkit().getImage("playerOk.gif");
		rectJoueur = new Rectangle(0, 0, 36, 29);
		
		this.nom = token.nextToken();
		setPos(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
		this.pv = Integer.parseInt(token.nextToken());
		this.pvMax = pv;
		this.angleSourisJoueur = Double.valueOf(token.nextToken());
	}
	
	// getters et setters
	
	public int getPv() {
		return pv;
	}
	
	public void setPv(int pv) {
		this.pv = pv;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String name) {
		this.nom = name;
	}
	public int getPosX() {
		return (int) rectJoueur.getCenterX();
	}
	
	public void setPosX(int posX) {
		this.rectJoueur.x = (int)posX - (rectJoueur.width/2);
	}
	
	public int getPosY() {
		return (int) rectJoueur.getCenterY();
	}
	public void setPosY(int posY) {
		this.rectJoueur.y = (int)posY - (rectJoueur.height/2);
	}
	public double getAngleSourisJoueur() {
		return angleSourisJoueur;
	}
	public void setAngleSourisJoueur(double angleSourisJoueur) {
		this.angleSourisJoueur = angleSourisJoueur;
	}
	public Rectangle getRectangle() {
		return rectJoueur;
	}
	
	// méthodes	
	/**
	 * retourn les infos du joueur sous la forme d'un string
	 * 
	 * @return les infos du joueur sous la forme d'un string
	 */
	public String toString() {
		char sep = '#';
		return 	nom + delim +
				Integer.toString(getPosX()) + delim +
				Integer.toString(getPosY()) + delim +
				getPv() + delim +
				angleSourisJoueur;
	}
	
	/**
	 * bouger le joueur dans une direction
	 * 
	 * @param dx deviation en x
	 * @param dy deviation en y
	 */
	public void move(int dx, int dy) {
		rectJoueur.setLocation(rectJoueur.x + dx, rectJoueur.y + dy);
	}
	
	/**
	 * dessine le joueur
	 * 
	 * @param g le panneau où l'on dessine l'image
	 * @param imObs l'objet dans lequel on dessine l'image
	 */
	public void dessiner(Graphics g, ImageObserver imObs) {
		Graphics2D g2D = (Graphics2D) g;
		
		// TRANSFORMATION
		AffineTransform t = new AffineTransform();
		t.translate(getPosX(), getPosY());
		t.rotate(angleSourisJoueur);
		t.translate(-imageJoueur.getWidth(null)/2, -imageJoueur.getHeight(null)/2);
		
		// DESSIN DE L'IMAGE
		g2D.drawImage(imageJoueur, t, imObs);
		//g.drawImage(imageJoueur, rectJoueur.x, rectJoueur.y, null);
		
		if (DEBUG)
		{
			g.setColor(Color.RED);
			g.drawRect(rectJoueur.x, rectJoueur.y, rectJoueur.width, rectJoueur.height);
		}
		
		// DESSIN DE LA BARRE DE VIE
		Rectangle barreVie = new Rectangle();
		barreVie.width = rectJoueur.width;
		barreVie.height = HEIGHT_BARRE_VIE;
		barreVie.x = rectJoueur.x;
		barreVie.y = rectJoueur.y - barreVie.height -  DESSUS_JOUEUR_BARRE_VIE;
		
		Rectangle vie = new Rectangle(barreVie);
		double pourcentageVieRestante = (double)pv / pvMax;
		vie.width = (int)(vie.width * pourcentageVieRestante);
		
		if (vie.width < 0)
			vie.width = 0;
		
		// définir la couleur
		if (pourcentageVieRestante > 0.75)
			g.setColor(Color.GREEN);
		else if (pourcentageVieRestante > 0.4)
			g.setColor(Color.ORANGE);
		else
			g.setColor(Color.RED);
		
		
		g.fillRect(vie.x, vie.y, vie.width, vie.height);
		g.setColor(Color.BLACK);
		g.drawRect(barreVie.x, barreVie.y, barreVie.width, barreVie.height);
	}

	public int getVitesse() {
		return vitesse;
	}
	
	/**
	 * Spécifier la position du joueur
	 * @param posX
	 * @param posY
	 */
	public void setPos(int posX, int posY) {
		this.rectJoueur.x = (int)posX - (rectJoueur.width/2);
		this.rectJoueur.y = (int)posY - (rectJoueur.height/2);
	}

	public void setMort(boolean mort) {
		this.mort = mort;
	}
	
	public boolean isMort() {
		return mort;
	}
}
