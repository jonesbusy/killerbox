package jeu;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

class PanneauGraphique extends JPanel {//implements Observer {
	

		/*public void update (Observable arg0, Object arg1) {
			//repaint();
		}*/
}

class Plateau extends JFrame implements KeyListener, MouseMotionListener{
	
	//private PanneauGraphique panneau;
	
	public Plateau(Joueur joueur){
		setTitle("Test KillerBox");
		setBounds(0, 0, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("curseurViseur.gif");
		Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "mon viseur");
		setCursor(monCurseur);
		addMouseMotionListener(this);
		this.joueur = joueur;
		
		panneau = new PanneauGraphique(joueur);
		getContentPane().add (panneau, "Center");
		setResizable(false);
		setVisible(true);
		pack();
		
		// initialisation des murs du jeu
		murs[0] = new Rectangle(0, 0, 15, 400);
		murs[1] = new Rectangle(0, 0, 400, 15);			
		murs[2] = new Rectangle(385, 0, 15, 400);
		murs[3] = new Rectangle(0, 385, 400, 15);
		murs[4] = new Rectangle(300, 300, 45, 45);
		murs[5] = new Rectangle(90, 90, 40, 40);
		murs[6] = new Rectangle(345, 15, 40, 35);
		murs[7] = new Rectangle(15, 345, 40, 40);
	}
	

	/**
	    * calculer l'angle entre 2 points
	    * 
	    * @param x1 la coordonnée x du point 1
	    * @param y1 la coordonnée y du point 1
	    * @param x2 la coordonnée x du point 2
	    * @param y2 la coordonnée y du point 2
	    */
	private void calculerAngle(double x1, double y1, double x2, double y2) {
		double a = (x2 - x1);
		double b = (y2 - y1);
		double length = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
      
		angleSourisJoueur = Math.asin(Math.abs(b) / length);
		if (b < 0) {
			angleSourisJoueur = -angleSourisJoueur;
		}
		if (a < 0) {
			angleSourisJoueur = Math.PI - angleSourisJoueur;
		}
		// Rescaled into [0, 2*PI[.
		angleSourisJoueur %= 2 * Math.PI;
		if (angleSourisJoueur < 0) {
			angleSourisJoueur += 2 * Math.PI;
		}
	}

	
	public void keyPressed(KeyEvent arg0) {
		
		final int deplacement = 2;
		
		if (arg0.getKeyCode() == KeyEvent.VK_W)
		{
			joueur.move(0, -deplacement);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, deplacement);
		}
		
		if (arg0.getKeyCode() == KeyEvent.VK_S)
		{
			joueur.move(0, deplacement);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, -deplacement);
		}
		
		if (arg0.getKeyCode() == KeyEvent.VK_A)
		{
			joueur.move(-deplacement, 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(deplacement, 0);
		}
		
		if (arg0.getKeyCode() == KeyEvent.VK_D)
		{
			joueur.move(deplacement, 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(-deplacement, 0);
		}
		
		repaint();
	}

	public void keyReleased(KeyEvent arg0) {
		
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

	
	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		calculerAngle(e.getX(), e.getY(), joueur.getPosX()-4, joueur.getPosY()-25);
		joueur.setAngleSourisJoueur(angleSourisJoueur);
		repaint();
		System.out.println("\nSposX = " + (e.getX()-4));
		System.out.println("JposX = " + joueur.getPosX());
		System.out.println("SposY = " + (e.getY()-25));
		System.out.println("JposY = " + joueur.getPosY());
	}
}

public class Jeu {

	private ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
	private Plateau plateau;
	
	public void init() {
		joueurs.add(new Joueur("toto", 50, 50, 100));
		joueurs.add(new Joueur("titi", 0, 0, 100));
		plateau = new Plateau(joueurs.get(0));
		
	}
	
	public void afficherJoueur(){
		for(Joueur joueur : joueurs)
			System.out.println("Joueur :\n" + joueur);
	}
	/**
	 * point d'entrée du jeu
	 * 
	 * @param args tableau de String transmis au programme (souvent null)
	 */
	public static void main(String[] args) {
		Jeu jeu = new Jeu();
		
		jeu.init();
		//jeu.afficherJoueur();
	}
}

