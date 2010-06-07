package killerbox.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;


public class CarteBase {
	
	private ArrayList<Rectangle> murs = new ArrayList<Rectangle>();
	private Image imageFond = Toolkit.getDefaultToolkit().getImage("fond.jpg");
	private int width = 400;
	private int height = 400;
	
	public ArrayList<Rectangle> getMurs() {
		return new ArrayList<Rectangle>(murs);
	}

	public CarteBase()
	{
		// initialisation des murs du jeu
		/*murs.add(new Rectangle(0, 0, 15, 400));
		murs.add(new Rectangle(0, 0, 400, 15));
		murs.add(new Rectangle(385, 0, 15, 400));
		murs.add(new Rectangle(0, 385, 400, 15));*/
		murs.add(new Rectangle(280, 280, 45, 45));
		murs.add(new Rectangle(90, 90, 45, 45));
		murs.add(new Rectangle(345, 15, 45, 45));
		murs.add(new Rectangle(15, 345, 45, 45));
		murs.add(new Rectangle(200, 200, 45, 45));
	}
	
	public void dessiner(Graphics g)
	{
		g.drawImage(imageFond, 0, 0, null);
		g.setColor(Color.black);
		for(Rectangle mur : murs)
			g.fillRect(mur.x, mur.y, mur.width, mur.height);
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

	public Dimension getSize() {
		return new Dimension((int)getWidth(),(int)getHeight());
	}
}
