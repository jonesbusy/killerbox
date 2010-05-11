package jeu;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Observable;

public class Joueur { // extends Observable {

	// attributs
	/**
	 * nom du joueur
	 */
	private String nom;
	
	 /**
	  * coordonnée du joueur
	  */
	private double posX;
	private double posY;
	private double angleSourisJoueur;

	/**
	 * points de vie du joueur
	 */
	private int pv;
	
	/**
	 * image du joueur
	 */
	private Image imageJoueur;
	private Rectangle colision;
	
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
		this.posX = posX;
		this.posY = posY;
		this.pv = pv;
		angleSourisJoueur = 0;
		imageJoueur = Toolkit.getDefaultToolkit().getImage("playerOk.gif");
		colision = new Rectangle(posX-3, posY-7, 36, 36);
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
	public double getPosX() {
		return posX;
	}
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public double getPosY() {
		return posY;
	}
	public void setPosY(double posY) {
		this.posY = posY;
	}
	public double getAngleSourisJoueur() {
		return angleSourisJoueur;
	}
	public void setAngleSourisJoueur(double angleSourisJoueur) {
		this.angleSourisJoueur = angleSourisJoueur;
	}
	public Rectangle getColision() {
		return colision;
	}
	
	// méthodes	
	/**
	 * retourn les infos du joueur sous la forme d'un string
	 * 
	 * @return les infos du joueur sous la forme d'un string
	 */
	public String toString() {
		return "nom : " + nom + "\nposition : (" + posX + ", " + posY
				+ ") \npv : " + pv + "\n";
	}
	
	/**
	 * bouger le joueur dans une direction
	 * 
	 * @param dx deviation en x
	 * @param dy deviation en y
	 */
	public void move(double dx, double dy) {
		posX += dx;
		posY += dy;
		colision.x = (int)posX;
		colision.y = (int)posY;
		//setChanged();
		//notifyObservers();
	}
	
	/**
	 * dessine le joueur
	 * 
	 * @param g le panneau où l'on dessine l'image
	 * @param imObs l'objet dans lequel on dessine l'image
	 */
	public void dessiner(Graphics g, ImageObserver imObs) {
		AffineTransform tx = new AffineTransform();
		tx.translate(posX/*imageJoueur.getWidth(imObs)/2*/, 
					 posY/*+imageJoueur.getHeight(imObs)/2*/);
		tx.rotate(angleSourisJoueur);
		tx.translate(-imageJoueur.getWidth(imObs)/2, 
				 	 -imageJoueur.getHeight(imObs)/2);
		
		((Graphics2D)g).drawImage (imageJoueur, tx, imObs);
		//g.drawRect(colision.x, colision.y, colision.width, colision.height);
		System.out.println("Angle : " + angleSourisJoueur*180/Math.PI);
		g.drawLine((int)(posX - 200*Math.cos(angleSourisJoueur)), (int)(posY - 200*Math.sin(angleSourisJoueur)),(int)posX, (int)posY);
	}
}
