package killerbox.gui.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import jeu.Joueur;

import killerbox.gui.BaseWindow;

public class PanelJeu extends AbstractPanel implements KeyListener, MouseMotionListener {

	private Joueur joueur;
	private double angleSourisJoueur;
	private Rectangle murs[] = new Rectangle[8];
	private int PG_X = 400;   // Taille en X du panneau graphique
	private int PG_Y = 400;   // Taille en Y du panneau graphique
	private Image imageDeFond;		 // Images de fond

	public PanelJeu(BaseWindow base) {
		super(base);
		
		this.joueur = new Joueur("hello",50,50,100);
		//joueur.addObserver(this);
		setBackground(new Color (0, 0, 65));
		// Chargement de l'image Montagne.gif, située dans le répertoire
		// des classes.  Image de taille 315*44 pixels
		imageDeFond = Toolkit.getDefaultToolkit().getImage ("carte.jpg");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("curseurViseur.gif");
		base.setFocusable(true);
		base.addKeyListener(this);
		base.addMouseMotionListener(this);
		Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "mon viseur");
		setCursor(monCurseur);
		this.setSize(new Dimension(PG_X,PG_Y));
		

		// initialisation des murs du jeu
		murs[0] = new Rectangle(0, 0, 15, 400);
		murs[1] = new Rectangle(0, 0, 400, 15);			
		murs[2] = new Rectangle(385, 0, 15, 400);
		murs[3] = new Rectangle(0, 385, 400, 15);
		murs[4] = new Rectangle(300, 300, 45, 45);
		murs[5] = new Rectangle(90, 90, 40, 40);
		murs[6] = new Rectangle(345, 15, 40, 35);
		murs[7] = new Rectangle(15, 345, 40, 40);
		
		// Redimensionnement de la fenetre
		base.setSize(this.getSize());
	}
	
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

	@Override
	public JButton getDefaultButton() {
		// TODO Auto-generated method stub
		return null;
	}
	// Vue passive, pour affichage uniquement
	
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageDeFond, 0, 0, this);
		joueur.dessiner(g, this);
    }
	
	public Dimension getPreferredSize() {
	// Retourne la taille souhaitée pour le composant (remplace le "getSize"
		return new Dimension (PG_X, PG_Y);
	}
	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		calculerAngle(e.getX(), e.getY(), joueur.getPosX()-4, joueur.getPosY()-25);
		joueur.setAngleSourisJoueur(angleSourisJoueur);
		repaint();
		System.out.println("\nSposX = " + (e.getX()-4));
		System.out.println("JposX = " + joueur.getPosX());
		System.out.println("SposY = " + (e.getY()-25));
		System.out.println("JposY = " + joueur.getPosY());
		
	}

	public void keyPressed(KeyEvent e) {
		System.out.println("hello");
		final int deplacement = 2;
		
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			joueur.move(0, -deplacement);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, deplacement);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			joueur.move(0, deplacement);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(0, -deplacement);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			joueur.move(-deplacement, 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(deplacement, 0);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			joueur.move(deplacement, 0);
			
			for(Rectangle mur : murs)
				if(mur.intersects(joueur.getColision()))
					joueur.move(-deplacement, 0);
		}
		
		repaint();
		
		System.out.println("hello");
		
	}

}
