package killerbox.game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class EtatCommandes {
	
	private final int kHaut,kBas,kDroite,kGauche,mTir;
	private boolean haut,bas,droite,gauche, tir;

	public EtatCommandes(int kHaut, int kBas, int kDroite, int kGauche,
			int mTir) {
		super();
		this.kHaut = kHaut;
		this.kBas = kBas;
		this.kDroite = kDroite;
		this.kGauche = kGauche;
		this.mTir = mTir;
	}

	/**
	 * Pour le clavier
	 * @param e La commande pressée ou relachée
	 */
	public void setKeyPressed(KeyEvent e)
	{
		int k = e.getKeyCode();
		
		if (k == kHaut)
			haut = true;
		else if (k == kBas)
			bas = true;
		else if (k == kDroite)
			droite = true;
		else if (k == kGauche)
			gauche = true;
	}
	
	public void setKeyReleased(KeyEvent e)
	{
		int k = e.getKeyCode();
		
		if (k == kHaut)
			haut = false;
		else if (k == kBas)
			bas = false;
		else if (k == kDroite)
			droite = false;
		else if (k == kGauche)
			gauche = false;
	}
	
	public void setMouseClick(MouseEvent e)
	{
		if (e.getButton() == mTir)
			tir = true;
	}

	public boolean isTir() {
		return tir;
	}

	public void setTir(boolean tir) {
		this.tir = tir;
	}

	public boolean isHaut() {
		return haut;
	}

	public boolean isBas() {
		return bas;
	}

	public boolean isDroite() {
		return droite;
	}

	public boolean isGauche() {
		return gauche;
	}
}
