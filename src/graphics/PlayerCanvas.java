package graphics;

import gamelogic.DrawablePlayerInfo;
import imagehelper.Imagehelper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
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

	private DrawablePlayerInfo info;
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
		this.info = info;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1100,200);
		return d;
	}

	@Override
	public void paint(Graphics g) {
		if(info != null){
			this.drawInventory(g);
			this.drawStatistics(g);
		}
		this.setBackground(statsBorderColor);

	}

	private void drawStatistics(Graphics g) {
		g.drawRect(500, 0, 599, 197);
		g.setColor(border);
		g.fillRect(500, 0, 300, 50);
		g.setColor(Color.RED);
		g.fillRect(505, 5, (int) (2.9*info.getHealthPercentage()), 40);

		//g.drawString(info.getPlayerIrlName(),505,60);
		g.setColor(lightGreenColor);
		Graphics2D g2d = (Graphics2D)g;
        Font font = new Font("Arial Bold", Font.PLAIN, 24);
        FontRenderContext frc = g2d.getFontRenderContext();

        GlyphVector gv = font.createGlyphVector(frc, info.getPlayerIrlName());
        g2d.drawGlyphVector(gv, 505, 70);
        gv = font.createGlyphVector(frc, Integer.toString(info.getCoinsCollected()));
		g2d.drawGlyphVector(gv, 610, 95);
        gv = font.createGlyphVector(frc, "Coins : ");
		g2d.drawGlyphVector(gv, 505, 95);

		g.drawLine(800, 0, 800, 200);
	}

	// draw sprite method here.
	public void drawInventory(Graphics g){
		g.drawImage(playerCanvasImages.get("inventory").getImage(), 0, 0, 500,200, this);
	}
}

