package killerbox;

import network.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


/**
 *
 * @author	Valentin Delaye
 * @version	1.0
 */
@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements Observer
{
	
	/**
	 * Controlleur
	 */
	private ClientListener listener;
	
	/**
	 * Bouton pour envoyer le message
	 */
	private JButton btnEnvoyer;

	/**
	 * Pour se connecter au serveur
	 */
	private JButton btnConnecter;
	
	/**
	 * Pour se deconnecter
	 */
	private JButton btnDeco;
	
	/**
	 * Pour que l'utilisateur entre son message
	 */
	private JTextField texMessage;
	
	/**
	 * Les log client
	 */
	private JTextArea log;
	
	/**
	 * Permet de creer une nouvelle fenetre client
	 * @param client Le client
	 */
	public ClientGUI(final Client client)
	{
		
		// Creer un nouvel ecouteur sur le client
		this.listener = new ClientListener(client, this);
		
		// Pour indiquer le changemnent d'etat (Connecte, non connecte)
		client.addObserver(listener);
		
		// Pour indiquer les message directement erreurs a la vue
		client.addObserver(this);
		
		// Creation des elements de la vue
		this.btnEnvoyer = new JButton("Envoyer");
		this.btnConnecter = new JButton("Se connecter");
		this.btnDeco = new JButton("Se deconnecter");
		
		this.texMessage = new JTextField(30);
		this.log = new JTextArea(20, 50);
		this.log.setEditable(false);
		this.btnEnvoyer.setEnabled(false);
		this.btnDeco.setEnabled(false);
		
		this.btnEnvoyer.addActionListener(new ActionListener()
		{
			/**
			 * Action lors du clique du bouton
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				client.send(texMessage.getText());
			}
		});
		
		this.btnConnecter.addActionListener(new ActionListener()
		{
			
			/**
			 * Action lors du clique du bouton
			 */	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(client.connect())
				{
					btnEnvoyer.setEnabled(true);
					btnConnecter.setEnabled(false);
					btnDeco.setEnabled(true);
				}
				
			}
		});
		
		this.btnDeco.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				client.send("@logout");
				btnConnecter.setEnabled(true);
				btnEnvoyer.setEnabled(false);
				btnDeco.setEnabled(false);
			}
		});
		
		this.setLayout(new FlowLayout());
		this.getContentPane().add(btnConnecter);
		this.getContentPane().add(btnDeco);
		this.getContentPane().add(texMessage);
		this.getContentPane().add(btnEnvoyer);
		this.getContentPane().add(log);
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setSize(600, 500);
		this.setVisible(true);
	}
	
	/**
	 * Reinitialiser la fenetre dans son etat de base
	 */
	public void init()
	{
		this.btnConnecter.setEnabled(true);
		this.btnDeco.setEnabled(false);
		this.btnEnvoyer.setEnabled(false);
	}
	
	/**
	 * Permet d'ecrire une ligne sur la zone de "log"
	 */
	public void ecrireLigne(String message)
	{
		this.log.setText(this.log.getText() + message + '\n');
	}
	

	/**
	 * 
	 */
	public void update(Observable o, Object obj)
	{
		// Si la vue recoit un message (generalement d'erreur)
		if(String.class.isInstance(obj))
		{
			String message = (String)obj;
			this.ecrireLigne(message);
		}
		
	}
}
