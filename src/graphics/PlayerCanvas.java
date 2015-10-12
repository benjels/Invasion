package graphics;

import gamelogic.DrawablePlayerInfo;
import gamelogic.FighterPlayerStrategy;
import gamelogic.SorcererPlayerStrategy;
import gamelogic.entities.RenderEntity;
import imagehelper.Imagehelper;
import imagehelper.PlayerCanvasImagehelper;

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
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
	private Color darkBorderColor;
	private Color lightGreenColor;
	private final Font LARGEFONT = new Font("Arial Bold", Font.PLAIN, 24); //original Arial Bold ;
	private final Font SMALLFONT = new Font("Arial Bold", Font.PLAIN, 16); //original Arial Bold;
	private final PlayerCanvasImagehelper HELPER = new PlayerCanvasImagehelper();
	private final static BasicStroke ARCSTROKE = new BasicStroke(0f);
	private int offset;
	private ArrayList<RenderEntity> playerInventory = null;

	public PlayerCanvas(){
		playerCanvasImages = HELPER.getPlayerCanvasImages();
		this.border = new Color(44,37,31);
		this.darkBorderColor = new Color(14,34,0);
		this.lightGreenColor = new Color(88,223,54);
	}

	public void setDrawableState(DrawablePlayerInfo info){
		this.gameStats = info;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1500,200);
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

			this.drawCoinsCollected(g); //done
			this.drawPlayerCharacter(g); // done

			this.drawPlayerRoomId(g);
			this.drawPlayerIrlName(g);//done
			this.drawCurrentRoomName(g);
			this.drawCurrentTime(g);

			this.drawMap(g); //needs to edit for arc
			this.drawItemSelect(g);
			this.drawShop(g);
			this.drawHealth(g); // done


			this.drawPylon0Health(g);
			this.drawPylon1Health(g);

			this.drawSelectedItem(g); // TODO
			this.drawScore(g);
			this.drawInventory(g);
		}
	}

	private void drawScore(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		g.setColor(darkBorderColor);
		//g.setColor(Color.RED); // for testing
		g.fillRect(590, 122, 60, 25);

		g.setColor(lightGreenColor);
		g.drawString(Integer.toString(gameStats.getScore()), 620, 143);
		g.drawString("Score : ", 505, 143);

	}

	private void drawSelectedItem(Graphics g) {


	}

	private void drawShop(Graphics g) {
		g.drawImage(playerCanvasImages.get("playerCanvasButtons").getImage(), 680, 50, 118, 146, this);
		g.setColor(darkBorderColor);

		g.fillRect(717, 50, 81, 147);
		g.setColor(lightGreenColor);
		for(int i = 0 ;i <= 6 ; i++){
			g.drawLine(717, 50+30*i, 798,50+30*i );
		}
	}

	private void drawItemSelect(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(SMALLFONT);

//		g.drawLine(950, 0, 950, 197); //nonresizable
		g.drawLine(950, 0, 950, 200);
		g.drawString("Item Description", 810, 25);
	}

	private void drawCurrentTime(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		g.drawString("Time : ", 505, 180);
		g.setColor(darkBorderColor);
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
		if(gameStats.getPlayerCharacter() instanceof SorcererPlayerStrategy){
			g.drawImage(playerCanvasImages.get("priestIcon").getImage(), 1300, 0, 200, 200,this);//orignal 197
		}else if(gameStats.getPlayerCharacter() instanceof FighterPlayerStrategy){
			g.drawImage(playerCanvasImages.get("warriorIcon").getImage(), 1300, 0, 200, 200,this);
		}
		g.setColor(lightGreenColor);
		g.drawRect(1297, 0, 197, 200);//border around the player
	}

	private void drawCoinsCollected(Graphics g) {
		g.setColor(lightGreenColor);
		g.setFont(LARGEFONT);
		g.drawString("Coins : ", 505, 110); //g.drawString(str, x, y);  g.drawRect(x, y, width, height);
		g.setColor(darkBorderColor);
		//g.setColor(Color.RED); // for testing
		g.fillRect(590, 89, 60, 25);

		g.setColor(lightGreenColor);
		g.drawString(Integer.toString(gameStats.getCoinsCollected()), 620, 110);

	}

	private void drawHealth(Graphics g) {
		g.drawRect(500, 0, 300, 197); // box around bar. orignal 197
		g.setColor(border);//player health bar outline
		g.fillRect(502, 3, 297, 48);
		g.setColor(Color.RED);
		g.fillRect(506, 6, (int) (2.90*gameStats.getHealthPercentage()), 40);
		//horizontal lines drawn
		g.setColor(lightGreenColor);

	}

	// draw sprite method here.
	public void drawInventory(Graphics g){
		//g.drawImage(playerCanvasImages.get("inventory").getImage(), 0, 0, 500,200, this); // canvas image
		drawInventoryBoxs(g,5);//passing list length as second parameter.
		playerInventory = gameStats.getCarriedEntities();
		int size = 0;
		for(RenderEntity re : playerInventory){
			//System.out.println(" text desc : "+re.getGameImageName()+" "+re.getClass());

			g.drawImage(playerCanvasImages.get(re.getGameImageName()).getImage(), 3+100*size, 3,94,190,this);

			if(playerCanvasImages.containsKey(re.getGameImageName()+"inv")){
				//System.out.println("called here.");
				g.drawImage(playerCanvasImages.get(re.getGameImageName()+"inv").getImage(),803, 34,143,161,this);
			}
			size++;
		}
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
		g.setColor(darkBorderColor);
		g.fillRect(800, 0, 499, 197);
		g.setColor(lightGreenColor);
		g.drawRect(800, 0, 499, 197); //box around map removed to include arc
		g.drawLine(950, 197/2, 1297, 197/2);
		for(int i = 0 ;i <= 3 ; i++){

			g.drawLine(950+116*i, 0, 950+116*i, 200);
		}
		int roomID = gameStats.getPlayerRoomId();
		g.setColor(Color.RED);

		if(roomID > 2){
			//need to define y depth
			offset=5%roomID;
			g.drawRect(605+116*roomID,197/2+3, 111, 197/2-5);
		}else{
			g.drawRect(953+116*roomID,3, 110, 197/2-6);
		}

	}

	private void drawPylon1Health(Graphics g) {
		g.setFont(LARGEFONT);
		g.drawString(Integer.toString(gameStats.getPylon1Health()),1221, 157);
	}
	private void drawPylon0Health(Graphics g) {
		g.setFont(LARGEFONT);
		g.drawString(Integer.toString(gameStats.getPylon0Health()), 989, 60);
	}
}

