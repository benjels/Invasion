package graphics;

import gamelogic.DrawablePlayerInfo;
import imagehelper.Imagehelper;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
/**
 * The player canvas is responsible for displaying player statistics,
 * these are,
 *
 *	healthPercentage
 *	coinsCollected
 *	CharacterStrategy
 *	RoomState
 *  Player Name;
 *	score;
 *
 * @date 30 Sep 2015
 * @author maxcopley
 */

@SuppressWarnings("serial")
public class PlayerCanvas extends Canvas{

	private DrawablePlayerInfo gameStats;
	private HashMap<String, ImageIcon> playerCanvasImages;
	private Color border;
	private Color statsBorderColor;
	private Color lightGreenColor;

	public PlayerCanvas(){
		Imagehelper helper = new Imagehelper();
		playerCanvasImages = helper.getPlayerCanvasImages();
		this.border = new Color(44,37,31);
		this.statsBorderColor = new Color(14,34,0);
		this.lightGreenColor = new Color(88,223,54);
	}

	public void setDrawableState(DrawablePlayerInfo info){
		this.gameStats = info;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1100,200);
		return d;
	}

	/**
	 * Allows double buffering of the PlayerCanvas. This is called by the repaint method in dummy slave class.
	 */
	@Override
	public void update(Graphics g) {
		Graphics offGraphics;
		BufferedImage offImage = null;
		Dimension d = this.getPreferredSize();
		offImage = new BufferedImage(d.width, d.height,BufferedImage.TYPE_INT_ARGB);
		offGraphics = offImage.getGraphics();
		paint(offGraphics);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(offImage, null, this);
	}

	public void paint(Graphics g) {
		if(gameStats != null){
			this.drawInventory(g);
			this.drawStatistics(g);
		}
	}

	private void drawStatistics(Graphics g) {
		g.drawRect(500, 0, 599, 197);
		g.setColor(border);//player health bar outline
		g.fillRect(502, 3, 297, 50);
		g.setColor(Color.RED);
		g.fillRect(510, 10, (int) (2.85*gameStats.getHealthPercentage()), 40);


		g.setColor(lightGreenColor);
		Graphics2D g2d = (Graphics2D)g;
        Font font = new Font("Arial Bold", Font.PLAIN, 24); //original Arial Bold
        FontRenderContext frc = g2d.getFontRenderContext();

        GlyphVector gv = font.createGlyphVector(frc, gameStats.getPlayerIrlName());
        g2d.drawGlyphVector(gv, 505, 75);
        gv = font.createGlyphVector(frc, Integer.toString(gameStats.getCoinsCollected()));
		g2d.drawGlyphVector(gv, 610, 105);
        gv = font.createGlyphVector(frc, "Coins : ");
		g2d.drawGlyphVector(gv, 505, 105);

		g.drawLine(800, 0, 800, 200);
		g.drawLine(1099, 0, 1099, 200);
	}

	// draw sprite method here.
	public void drawInventory(Graphics g){
		//g.drawImage(playerCanvasImages.get("inventory").getImage(), 0, 0, 500,200, this); // canvas image
		drawInventoryBoxs(g,5);//passing list length as second parameter.


	}

	public void drawInventoryBoxs(Graphics g, int x){
		// Lines below draw solid inventory bars.
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(lightGreenColor);
		//Stroke stroke = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 50, 5 }, 0);
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);
		for(int i = 0 ;i <= x ; i++){
			g2d.drawLine(0+100*i, 0, 0+100*i, 200);
		}
		g2d.drawRect(0, 0, 500, 196);
	}

	public void drawMap(){


	}
}

