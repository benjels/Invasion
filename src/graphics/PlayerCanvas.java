package graphics;

import gamelogic.DrawablePlayerInfo;
import imagehelper.Imagehelper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

	public PlayerCanvas(){
		Imagehelper helper = new Imagehelper();
		playerCanvasImages = helper.getPlayerCanvasImages();
	}



	public void setDrawableState(DrawablePlayerInfo info){
		this.info = info;
	}

	@Override
	public Dimension getPreferredSize() {
		//playerCanvas.setBounds(500, 0, 400, 200);
		Dimension d = new Dimension(1100,200);
		return d;
	}
	/**
	 * code to be refactored into individual draw components.
	 */
	@Override
	public void paint(Graphics g) {
		//System.out.println(info.getCoinsCollected());// 0
		if(info != null){
			this.drawInventory(g);
			this.drawStatistics(g);
		}
	}

	private void drawStatistics(Graphics g) {
		String coins = Integer.toString(info.getCoinsCollected());
		g.drawRect(500, 0, 599, 197);
		//health bar
		g.setColor(Color.BLACK);
		g.fillRect(500, 0, 300, 50);
		g.setColor(Color.RED);
		g.fillRect(505, 5, 3*info.getHealthPercentage(), 40);
		g.drawString(info.getPlayerIrlName(),505,60);

		//amount of coins.
		g.setColor(Color.BLACK);
		g.drawString("Coins : ", 505, 75);
		g.drawString(coins,555,75); //very rough drawing displaying contents.

		//g.drawString(info.getPlayerRoom(),10,70);
		//g.drawString(info.getPlayerCharacter(),10,90);
		g.drawLine(800, 0, 800, 200);
	}


	// draw sprite method here.
	public void drawInventory(Graphics g){
		g.drawImage(playerCanvasImages.get("inventory").getImage(), 0, 0, 500,200, this);
	}
}


/**
 * TODO Remove comments - added only for reference
 *
 *	healthPercentage
 *	coinsCollected
 *	CharacterStrategy
 *	RoomState
 *  Player Name;
 *	score;
 **/