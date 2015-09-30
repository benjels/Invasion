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


		String coins = Integer.toString(info.getCoinsCollected());

		g.drawString(coins,10,150); //very rough drawing displaying contents.
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 300, 50);
		g.setColor(Color.RED);
		g.fillRect(5, 5, 3*info.getHealthPercentage(), 40);
		g.drawString(info.getPlayerIrlName(),10,60);

		//g.drawString(info.getPlayerRoom(),10,70);
		//g.drawString(info.getPlayerCharacter(),10,90);


	}
}


/**
 *	healthPercentage
 *	coinsCollected
 *	CharacterStrategy
 *	RoomState
 *  Player Name;
 *	score;
**/