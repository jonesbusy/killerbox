package jeu;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Projectile{
	
	// attributs
	/**
	  * coordonnée du projectile
	  */
	private double posX;
	private double posY;
	
	private double angleSourisProjectile;
	private Polygon pol;
	
	// constructeur
	public Projectile(double posX, double posY, double angleSourisProjectile) {
		this.posX = posX;
		this.posY = posY;
		this.angleSourisProjectile = angleSourisProjectile;
	}
	
	// méthodes
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
	public double getAngleSourisProjectile() {
		return angleSourisProjectile;
	}
	public void setAngleSourisProjectile(double angleSourisProjectile) {
		this.angleSourisProjectile = angleSourisProjectile;
	}
	
	/**
	 * bouger le projectile dans une direction
	 * 
	 * @param dx deviation en x
	 * @param dy deviation en y
	 */
	public void move(double dx, double dy) {
		posX = posX + dx;
		posY = posY + dy;
		//setChanged();
		//notifyObservers();
	}
	
	/**
	 * dessine le projectile
	 * 
	 * @param g le panneau où l'on dessine l'image
	 * @param imObs l'objet dans lequel on dessine l'image
	 */
	public void dessiner(Graphics g, ImageObserver imObs) {
		g.drawPolygon(pol);
	}
}
