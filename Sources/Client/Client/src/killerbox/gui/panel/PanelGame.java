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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.resources.ModelerMessages;

import killerbox.game.*;
import killerbox.gui.BaseWindow;

public class PanelGame extends AbstractPanel implements KeyListener, MouseMotionListener,
		Runnable, MouseListener, Observer
{

	private static final int HAUTEUR_CHAT = 100;
	private final int FPS = 10;
	private double angleSourisJoueur;
	private Image imageDeFond;		 // Images de fond
	private int PG_X = 400; // Taille en X du panneau graphique
	private int PG_Y = 400; // Taille en Y du panneau graphique
	private Image imageDeFond; // Images de fond
	private CarteBase carte = new CarteBase();
	private EtatCommandes etatCommandes = new EtatCommandes(KeyEvent.VK_W, KeyEvent.VK_S,
			KeyEvent.VK_D, KeyEvent.VK_A, MouseEvent.BUTTON1);
	private Thread refresh = new Thread(this);
	private Chat chat;

	private Thread action = new Thread(new Runnable() {
		
		public void run() {
	private Thread action = new Thread(new Runnable()
	{

		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(1000 / 10);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				window.getControllerGame().gestionCommandes(etatCommandes);
			}
		}
	});

	public PanelGame(BaseWindow base)
	{
		super(base);

		// Cr�ation du mod�le et du controller s'ils n'existe pas
		if (window.getModelGame() == null)
			window.setModelGame(new ModelGame());

		if (window.getControllerGame() == null)
			window.setControllerGame(new ControllerGame(window.getModelGame()));

		// V�rifier si le controller poss�de d�j� le controller r�seau
		if (window.getControllerGame().getNetworkController() == null)
			window.getControllerGame().setNetworkController(controller);

		// Ajout des �couteurs
		base.setFocusable(true);
		base.addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		base.getModelGame().addObserver(this);

		// On modifie l'image du curseur
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("curseurViseur.gif");
		Cursor curseurViseur = tk.createCustomCursor(img, new Point(16, 16), "viseur");
		setCursor(curseurViseur);
		
		// chat
		chat = new Chat(window.getModelGame());
		

		this.setSize(new Dimension(PG_X, PG_Y));

		refresh.start();
		action.start();
	}

	/**
	 * M�thode dessinant le mod�le de jeu sur le panel. C'est � dire le fond
	 * de la carte, les murs et les joueurs.
	 */

	private void calculerAngle(double x1, double y1, double x2, double y2)
	{
		double a = (x2 - x1);
		double b = (y2 - y1);
		double length = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

		angleSourisJoueur = Math.asin(Math.abs(b) / length);
		if (b < 0)
		{
			angleSourisJoueur = -angleSourisJoueur;
		}
		if (a < 0)
		{
			angleSourisJoueur = Math.PI - angleSourisJoueur;
		}
		// Rescaled into [0, 2*PI[.
		angleSourisJoueur %= 2 * Math.PI;
		if (angleSourisJoueur < 0)
		{
			angleSourisJoueur += 2 * Math.PI;
		}

		repaint();
	}

	@Override
	public JButton getDefaultButton()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// Vue passive, pour affichage uniquement

	public void paintComponent(Graphics g)
	{
		ModelGame modelGame = window.getModelGame();
		ControllerGame controllerGame = window.getControllerGame();

		super.paintComponent(g);

		try
		{
			switch (modelGame.getEtat())
			{
				case Chargement:
					g.drawString("Chargement en cours ...", 10, 10);
					break;
				case Demarrer:
					// Dessiner la carte
					modelGame.getCarte().dessiner(g);

					// dessiner les tirs
					for (Tir tir : modelGame.getTirs())
					{
						tir.dessiner(g);
					}

					// dessiner les joueurs
					for (Joueur joueur : modelGame.getJoueurs())
					{
						if (!joueur.isMort())
							joueur.dessiner(g, null);
					}

					// dessiner le chat
					chat.dessiner(g);
					// dessiner messages
					int hauteurTexte = g.getFont().getSize();
					int posY = 10;
					int posX = 10;
					for (Message message : modelGame.getMessages())
					{
						g.setColor(message.getColor());
						g.drawString(message.getMessage(), posX, posY);
						posY = posY + hauteurTexte;
					}

					break;
			}
		}
		catch(Exception e){}
    }
	
	public Dimension getPreferredSize() {
		// Retourne la taille souhait�e pour le composant (remplace le "getSize"
		return getSize();
		catch (Exception e)
		{
		}
	}

	public Dimension getPreferredSize()
	{
		// Retourne la taille souhait�e pour le composant (remplace le "getSize"
		return new Dimension(PG_X, PG_Y);
	}

	public void keyPressed(KeyEvent e)
	{
		etatCommandes.setKeyPressed(e);
	}

	public void keyReleased(KeyEvent e)
	{
		etatCommandes.setKeyReleased(e);
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void keyTyped(KeyEvent e)
	{
		
	}

	public void mouseDragged(MouseEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		window.getControllerGame().angleJoueurSouris(e);

	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(1000 / FPS);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}

	}

	/**
	 * Model notifie ses changement d'�tat chargement -> demarrer
	 */
	public void update(Observable o, Object arg)
	{
		// On adapte la taille de la fen�tre et du panel � celle de la carte
		Dimension carteDim = window.getModelGame().getCarte().getSize();
		setSize(carteDim);
		chat.y = carteDim.height;
		chat.width = carteDim.width;
		chat.height = HAUTEUR_CHAT;
		window.setSize(carteDim.width,carteDim.height + window.getHeightMenu()+ 20 + chat.height);
		window.setSize(carteDim.width, carteDim.height + window.getHeightMenu() + 20);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		etatCommandes.setMouseClick(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
