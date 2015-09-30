package graphics;

import gamelogic.DrawablePlayerInfo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

	public PlayerCanvas(){}

	public void setDrawableState(DrawablePlayerInfo info){
		this.info = info;
	}

	@Override
	public Dimension getPreferredSize() {
		//playerCanvas.setBounds(500, 0, 400, 200);
		Dimension d = new Dimension(1100,200);
		return d;
	}

	public void paint(Graphics g) {
		//System.out.println(info.getCoinsCollected());// 0
		if(info != null){
			System.out.println("health = "+info.getHealthPercentage());
			String coins = Integer.toString(info.getCoinsCollected());
			//inventory borders
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 100, 197);
			g.drawRect(100, 0, 100, 197);
			g.drawRect(200, 0, 100, 197);
			g.drawRect(300, 0, 100, 197);
			g.drawRect(400, 0, 100, 197);

			//borders
			g.drawRect(500, 0, 599, 197);
			//health bar
			g.setColor(Color.BLACK);
			g.fillRect(500, 0, 300, 50);
			g.setColor(Color.RED);
			g.fillRect(505, 5, 3*info.getHealthPercentage(), 40);

			//player real name
			g.drawString(info.getPlayerIrlName(),505,60);

			//amount of coins.
			g.setColor(Color.BLACK);
			g.drawString("Coins : ", 505, 75);
			g.drawString(coins,555,75); //very rough drawing displaying contents.


			//g.drawString(info.getPlayerRoom(),10,70);
			//g.drawString(info.getPlayerCharacter(),10,90);
			g.drawLine(800, 0, 800, 200);


		}

	}

	// draw sprite method here.




}


/**
 *	healthPercentage
 *	coinsCollected
 *	CharacterStrategy
 *	RoomState
 *  Player Name;
 *	score;
 **/