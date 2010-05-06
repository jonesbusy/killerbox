package killerbox.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Represente la boite de dialogue About de KillerBox
 * @author Valentin Delaye
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog
{
	
	/**
	 * Largeur de la boite de dialogue
	 */
	public static final int WIDTH = 350;
	
	/**
	 * Hauteur de la boite de dialogue
	 */
	public static final int HEIGHT = 200;
	
	/**
	 * La fenetre parente
	 */
	private JFrame parent;
	
	/**
	 * Panel pour contenir la partie sud
	 */
	private JPanel panel = new JPanel();
	
	/**
	 * Le bouton ok
	 */
	private JButton btnOk = new JButton("Ok");
	
	/**
	 * Permet de creer la boite about
	 * @param parent La fenetre parente
	 */
	public AboutDialog(JFrame parent)
	{
		super(parent, "A propos de KillerBox", true);
		this.parent = parent;
		
		this.add(new JLabel("<html><h1><i>KillerBox</i></h1><hr>By Fabrizio Beretta Piccoli <br />" +
																	 "Jonas Berdoz<br />" +
																    "Valentin Delaye<br />" +
																    "Michael Sandoz<html>"), BorderLayout.CENTER);
		
		this.panel.add(this.btnOk);
		this.add(this.panel, BorderLayout.SOUTH);
		
		// Taille
		this.setSize(AboutDialog.WIDTH, AboutDialog.HEIGHT);
		this.setResizable(false);
		
		this.setLocationRelativeTo(this.parent);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Masquer la fenetre lors du clique sur le bouton
		btnOk.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
	}
}
