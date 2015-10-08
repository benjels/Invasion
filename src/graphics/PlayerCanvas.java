package graphics;

import gamelogic.DrawablePlayerInfo;
import imagehelper.Imagehelper;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
	private final Font LARGEFONT = new Font("Arial Bold", Font.PLAIN, 24); //original Arial Bold ;
	private final Font SMALLFONT = new Font("Arial Bold", Font.PLAIN, 16); //original Arial Bold;

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
		Dimension d = new Dimension(1300,200);
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
			this.drawInventory(g); //done ok
			this.drawHealth(g); // done
			this.drawCoinsCollected(g); //done
			this.drawPlayerCharacter(g); //1/2 done
			this.drawPlayerRoomId(g);
			this.drawPlayerIrlName(g);//done
			this.drawCurrentRoomName(g);
			this.drawCurrentTime(g);
			this.drawPylon0Health(g);
			this.drawgetPylon1Health(g);
			this.drawMap(g);
			this.drawItemSelect(g);
		}
	}

	private void drawItemSelect(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(SMALLFONT);

		g.drawLine(950, 0, 950, 197);
		g.drawString("Item Description", 810, 25);
	}

	private void drawCurrentTime(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		g.drawString("Time : ", 505, 180);
		g.setColor(statsBorderColor);
		g.fillRect(570, 155, 80, 30);

		g.setColor(lightGreenColor);
		g.drawString(gameStats.getCurrentTime(), 575, 180);
	}

	private void drawCurrentRoomName(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
	}

	private void drawPlayerIrlName(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(LARGEFONT);
        g2d.drawString(gameStats.getPlayerIrlName(), 505, 75);
	}

	private void drawPlayerRoomId(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
	}

	private void drawPlayerCharacter(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
//		if(gameStats.getPlayerCharacter().equals(Warrior)){
//
//		}
		g.drawString("Warrior", 505, 118);//gameStats.getPlayerCharacter()
	}

	private void drawCoinsCollected(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		g.drawString("Coins : ", 505, 100); //g.drawString(str, x, y);  g.drawRect(x, y, width, height);
		g.setColor(statsBorderColor);
		//g.setColor(Color.RED); // for testing
		g.fillRect(590, 77, 60, 25);

		g.setColor(lightGreenColor);
		g.drawString(Integer.toString(gameStats.getCoinsCollected()), 620, 100);

	}

	private void drawHealth(Graphics g) {
		g.drawRect(500, 0, 300, 197); // box around bar.
		g.setColor(border);//player health bar outline
		g.fillRect(502, 3, 297, 48);
		g.setColor(Color.RED);
		g.fillRect(506, 6, (int) (2.90*gameStats.getHealthPercentage()), 40);
		//horizontal lines drawn
		g.setColor(lightGreenColor);

	}

	public void drawCharacterStrategy(Graphics g){
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		//if(gameStats.getPlayerCharacter().getClass(){
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
		Stroke stroke = new BasicStroke(3);
		g2d.setStroke(stroke);


		for(int i = 0 ;i <= x ; i++){
			g2d.drawLine(0+100*i, 0, 0+100*i, 200);
		}
		g2d.drawRect(0, 0, 500, 196);
	}

	public void drawMap(Graphics g){
		g.drawRect(800, 0, 500, 197); //box around map
		g.drawLine(950, 197/2, 1300, 197/2);
		for(int i = 0 ;i <= 3 ; i++){
			g.drawLine(950+116*i, 0, 950+116*i, 200);
		}
	}

	private void drawgetPylon1Health(Graphics g) {

	}

	private void drawPylon0Health(Graphics g) {

	}


	/*
	 * 											METHODS TO DRAW
	 * to draw.
	 *
	 *  gameStats.getCarriedEntities();
	 *
	 *
		gameStats.getCoinsCollected();
		gameStats.getHealthPercentage();
		gameStats.getPlayerCharacter();
		gameStats.getPlayerRoomId();
		gameStats.getPlayerIrlName();
		gameStats.getCurrentRoomName();


		gameStats.getCurrentRoomName();
		gameStats.getPylon0Health();
		gameStats.getPylon1Health();

			g.setColor(lightGreenColor);
		Graphics2D g2d = (Graphics2D)g;

        FontRenderContext frc = g2d.getFontRenderContext();

        g2d.setFont(LARGEFONT);
        GlyphVector gv = LARGEFONT.createGlyphVector(frc, gameStats.getPlayerIrlName());
        FontMetrics fm = g2d.getFontMetrics(LARGEFONT);

        // System.out.println(fm.stringWidth(gameStats.getPlayerIrlName())); // prints length of the string.


        g2d.drawString(gameStats.getPlayerIrlName(), 505, 75);


//        gv = font.createGlyphVector(frc, Integer.toString(gameStats.getCoinsCollected()));
//        g.setColor(statsBorderColor);
//        g2d.fillRect(585, 81, 72, 25);
//        g.setColor(lightGreenColor);
//		  g2d.drawGlyphVector(gv, 610, 105);
//        gv = font.createGlyphVector(frc, "Coins : ");
//		  g2d.drawGlyphVector(gv, 505, 105);
	*/
}

