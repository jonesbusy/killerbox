package killerbox.gui.panel;


import java.awt.*;
import java.awt.event.*;

import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


import killerbox.game.*;
import killerbox.gui.BaseWindow;

@SuppressWarnings("serial")
public class PanelGame extends AbstractPanel implements KeyListener, MouseMotionListener,
		Runnable, MouseListener, Observer, ActionListener
{

	private static final int HAUTEUR_CHAT = 100;
	private final int FPS = 25;
	private EtatCommandes etatCommandes = new EtatCommandes(KeyEvent.VK_W, KeyEvent.VK_S,
			KeyEvent.VK_D, KeyEvent.VK_A, MouseEvent.BUTTON1);
	private Thread refresh = new Thread(this);
	private Chat chat;

	private Thread action = new Thread(new Runnable()
	{
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
					
				}
				
				// TODO Verifier NullPointer
				if (window.getControllerGame() != null)
					window.getControllerGame().gestionCommandes(etatCommandes);
			}
		}
	});
	
	private Thread checkFinJeu = new Thread(new Runnable() {

		public void run() {
			boolean finJeu = false;
			while (!finJeu)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					
				}
				
				// TODO Verifier NullPointer
				if (window.getControllerGame() != null)
					finJeu = window.getControllerGame().checkFinJeu();
			}
		}
	});

	public PanelGame(BaseWindow base)
	{
		super(base);

		// Création du modèle et du controller s'ils n'existe pas
		if (window.getModelGame() == null)
			window.setModelGame(new ModelGame());

		if (window.getControllerGame() == null)
			window.setControllerGame(new ControllerGame(window.getModelGame()));

		// Vérifier si le controller possède déjà le controller réseau
		if (window.getControllerGame().getNetworkController() == null)
			window.getControllerGame().setNetworkController(controller);

		// Ajout des écouteurs
		base.setFocusable(true);
		base.addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		base.getModelGame().addObserver(this);
		base.getQuitGame().addActionListener(this);

		// On modifie l'image du curseur
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.createImage("curseurViseur.gif");
		Cursor curseurViseur = tk.createCustomCursor(img, new Point(16, 16), "viseur");
		setCursor(curseurViseur);
		
		// chat
		chat = new Chat(window.getModelGame());
		refresh.start();
		action.start();
		checkFinJeu.start();
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
				case AFFICHAGE_SCORE:
					dessinerJeu(g, modelGame);
					controllerGame.afficherScores(g,new Dimension(getSize().width, getSize().height - chat.height));
					break;
				case Demarrer:
					dessinerJeu(g, modelGame);
			}
		}
		catch(Exception e){}
    }

	private void dessinerJeu(Graphics g, ModelGame modelGame) {
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
	}
	
	public Dimension getPreferredSize() {
		// Retourne la taille souhaitée pour le composant (remplace le "getSize"
		return getSize();
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
				
			}
			repaint();
		}

	}

	/**
	 * Model notifie ses changement d'état
	 *  - chargement -> demarrer
	 *  - démarrer -> résultats
	 * 
	 */
	public void update(Observable o, Object arg)
	{
		if (window.getModelGame().getEtat().equals(EtatModel.Demarrer))
		{
			// On adapte la taille de la fenêtre et du panel à celle de la carte
			Dimension carteDim = window.getModelGame().getCarte().getSize();
			setSize(carteDim);
			chat.y = carteDim.height;
			chat.width = carteDim.width;
			chat.height = HAUTEUR_CHAT;
			window.setSize(carteDim.width,carteDim.height + window.getHeightMenu()+ 20 + chat.height);
		}
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// S'il s'agit du bouton quitter
		if (e.getSource() == window.getQuitGame())
		{
			// Informer le serveur qu'on quitte la partie
			controller.requestQuitGame(window.getId());
			
			// Supprimer les écouteurs
			window.removeKeyListener(this);
			window.getQuitGame().removeActionListener(this);
			removeMouseMotionListener(this);
			removeMouseListener(this);
			
			// Arrêter toutes les threads
			action.interrupt();
			refresh.interrupt();
			checkFinJeu.interrupt();
			window.getControllerGame().arreterPartie();
			
			// Supprimer le modèle et le controller
			window.setControllerGame(null);
			window.setModelGame(null);
			
			// Redimensionner la fenêtre
			window.setSize(new Dimension(BaseWindow.getDefaultWidth(),BaseWindow.getDefaultHeight()));
		}
		
	}

}
