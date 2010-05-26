package killerbox.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class CarteBase {
	
	private ArrayList<Rectangle> murs = new ArrayList<Rectangle>();
	private Image imageFond = Toolkit.getDefaultToolkit().getImage("carte.jpg");
	private int width = 400;
	private int height = 400;
	
	public ArrayList<Rectangle> getMurs() {
		return new ArrayList<Rectangle>(murs);
	}

	public CarteBase()
	{
		// initialisation des murs du jeu
		murs.add(new Rectangle(0, 0, 15, 400));
		murs.add(new Rectangle(0, 0, 400, 15));
		murs.add(new Rectangle(385, 0, 15, 400));
		murs.add(new Rectangle(0, 385, 400, 15));
		murs.add(new Rectangle(300, 300, 45, 45));
		murs.add(new Rectangle(90, 90, 40, 40));
		murs.add(new Rectangle(345, 15, 40, 35));
		murs.add(new Rectangle(15, 345, 40, 40));
	}
	
	public void dessiner(Graphics g)
	{
		g.drawImage(imageFond, 0, 0, null);
	}

	public Image getBackImage() {
		return imageFond;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
