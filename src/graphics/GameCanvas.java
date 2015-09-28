package graphics;

import gamelogic.DrawableRoomState;
import gamelogic.RenderDoorTile;
import gamelogic.RenderEntity;
import gamelogic.RenderPlayer;
import gamelogic.RenderRoomTile;
import imagehelper.Imagehelper;
import imagehelper.IsoHelper;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * Board Canvas representing the board.
 */

@SuppressWarnings("serial")
public class GameCanvas extends Canvas {

	private final Image grass = Imagehelper.loadImage2("green-0.gif");

	private RenderRoomTile[][] tiles;
	private RenderEntity[][] entities;

	public GameCanvas() {

	}

	public void setDrawableState(DrawableRoomState state) {
		this.tiles = state.getTiles();
		this.entities = state.getEntities();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1500, 800);
		return d;
	}

	//draws all objects on an Image and then renders entire image to stop flickering
	//http://stackoverflow.com/questions/10508042/how-do-you-double-buffer-in-java-for-a-game
	@Override
	public void update(Graphics g){
		Graphics offGraphics;
		Image offImage = null;
		Dimension d = getPreferredSize();

		//Creating the offscreen image to draw on
		offImage = createImage(d.width,d.height);
		//setting the offScreen graphics to the offscreen Image Graphics
		offGraphics = offImage.getGraphics();
		//painting all objects onto the offGraphics
		paint(offGraphics);
		//draw the offscreen image onto the window
		g.drawImage(offImage,0,0,this);

	}

	@Override
	public void paint(Graphics g) {
		if (this.tiles != null && this.entities != null) {// TODO: gross
															// solution to only
															// painting if we
															// havent
															// initialised the
															// tiles

			assert (this.tiles != null && this.entities != null) : "this.tiles cant be null";

			int xOffset = 250;
			int yWallOffset = 64;
			int yOffset = 100;
			int width = 64;
			int height = 32;
			for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles.length; col++) {
					RenderRoomTile tile = this.tiles[row][col];
					RenderEntity ent = this.entities[row][col];
					Point point = IsoHelper.twoDToIso(col, row, width, height);
					if (tile != null) { // @Joely note that there should not be
										// any nulls in the program (from my
										// code). the lack of some object is
										// indicated by a "NullInstance" type
										// object e.g. NullEntity
						Image tileImage = Imagehelper.loadImage2("stone64.png");// TODO:
																				// hardcoded
																				// only
																				// drawing
																				// stones
						if (false) {// TODO: "tile instanceof WallTile"
							g.drawImage(tileImage, xOffset + point.x,
									yWallOffset + point.y, null, null);
						} else {
							g.drawImage(tileImage, xOffset + point.x, yOffset
									+ point.y, null, null);
						}

					}// HARDCODED GROSS SHIT FOR TESTING PLAYER MOVEMENT/MAX
						// (just drawing a different tile to represnet the
						// player
					if (ent instanceof RenderPlayer) {
						Image tileImage = Imagehelper.loadImage2("grass64.png");
						g.drawImage(tileImage, xOffset + point.x, yWallOffset
								+ point.y, null, null);
					}if (tile instanceof RenderDoorTile){
						Image tileImage = Imagehelper.loadImage2("dirt64.png");
						g.drawImage(tileImage, xOffset + point.x, yWallOffset
								+ point.y, null, null);
					}

				}
			}
			// g.drawImage(grass,50,50,null,null);
		}
	}

}
