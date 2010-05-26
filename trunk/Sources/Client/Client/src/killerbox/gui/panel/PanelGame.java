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
import javax.swing.JLabel;

import killerbox.game.*;
import killerbox.gui.BaseWindow;

public class PanelGame extends AbstractPanel implements KeyListener, MouseMotionListener {

	private ModelGame modelGame;
	private ControllerGame controllerGame;
	
	private double angleSourisJoueur;
	private int PG_X = 400;   // Taille en X du panneau graphique
	private int PG_Y = 400;   // Taille en Y du panneau graphique
	private Image imageDeFond;		 // Images de fond
	private CarteBase carte = new CarteBase();

	public PanelGame(BaseWindow base) {
		super(base);
		
		// Création du modèle et du controller s'ils n'existe pas
		if (window.getModelGame() == null)
			window.setModelGame(new ModelGame());
		
		if (window.getControllerGame() == null)
			window.setControllerGame(new ControllerGame(window.getModelGame()));
		
		// Récupérer le model et le controller
		modelGame = window.getModelGame();
		controllerGame = window.getControllerGame();
		
		// Ajout des écouteurs
		base.setFocusable(true);
		base.addKeyListener(this);
		base.addMouseMotionListener(this);
		
		// On modifie l'image du curseur
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("curseurViseur.gif");
		Cursor curseurViseur = tk.createCustomCursor(img, new Point(16, 16), "viseur");
		setCursor(curseurViseur);
		
		this.setSize(new Dimension(PG_X,PG_Y));
	}
	
	/**
	 * Méthode dessinant le modèle de jeu sur le panel. C'est à dire le fond
	 * de la carte, les murs et les joueurs.
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
		
		repaint();
	}

	@Override
	public JButton getDefaultButton() {
		// TODO Auto-generated method stub
		return null;
	}
	// Vue passive, pour affichage uniquement
	
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		try
		{
			switch (modelGame.getEtat()) {
				case Chargement:
					g.drawString("Chargement en cours ...", 10, 10);
					break;
				case Demarrer:
					// Dessiner la carte
					modelGame.getCarte().dessiner(g);
					
					// dessiner les joueurs
				
				break;
			}
		}
		catch(Exception e){}
    }
	
	public Dimension getPreferredSize() {
	// Retourne la taille souhaitée pour le composant (remplace le "getSize"
		return new Dimension (PG_X, PG_Y);
	}
	

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		//calculerAngle(e.getX(), e.getY(), modele.getJoueurActif().getPosX()-4, modele.getJoueurActif().getPosY()-25);
		//modele.getJoueurActif().setAngleSourisJoueur(angleSourisJoueur);
		repaint();
		
	}

	public void keyPressed(KeyEvent e) {
		
		// Créer un controller pour gérer ce genre d'action
		//controller.gestionDeplacement(carte,modele.getJoueurActif(),e);
		repaint();
		
	}

}
