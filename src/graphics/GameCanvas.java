package graphics;

import gamelogic.CardinalDirection;
import gamelogic.DrawableRoomState;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderImpassableColomn;
import gamelogic.entities.RenderKeyCard;
import gamelogic.entities.RenderOuterWall;
import gamelogic.entities.RenderPlayer;
//import gamelogic.events.RenderTeleporterTile;

import gamelogic.entities.RenderTeleporter;
import gamelogic.entities.RenderZombie;
import gamelogic.tiles.RenderRoomTile;
import imagehelper.Imagehelper;
import imagehelper.IsoHelper;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * Board Canvas representing the board.
 */

@SuppressWarnings("serial")
public class GameCanvas extends Canvas {
	private RenderRoomTile[][] tiles;
	private RenderEntity[][] entities;
	private boolean roomRendered = false;
	private Image RoomImage = null;

	// offsets for drawing the game
	private int xOffset = 10;
	private int yOffset = 380;
	// wall offsets as they have different width and height
	private int xWall = -32 - 4;
	private int yWall = -32 * 6 + 16;
	// size of the images
	private int width = 64;
	private int height = 32;

	private int roomId;
	private boolean isDark;

	public GameCanvas() {

	}

	public void setDrawableState(DrawableRoomState state) {
		this.tiles = state.getTiles();
		this.entities = state.getEntities();
		if (state.getRoomId() != roomId) {
			roomRendered = false;
			this.roomId = state.getRoomId();
		}
		this.isDark = state.isDark();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1500, 800);
		return d;
	}

	// draws all objects on an Image and then renders entire image to stop
	// flickering
	// http://stackoverflow.com/questions/10508042/how-do-you-double-buffer-in-java-for-a-game

	@Override
	public void update(Graphics g) {
		Graphics offGraphics;
		BufferedImage offImage = null;
		Dimension d = getPreferredSize();

		// Creating the offscreen image to draw on
		offImage = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_ARGB);
		// setting the offScreen graphics to the offscreen Image Graphics
		offGraphics = offImage.getGraphics();
		// painting all objects onto the offGraphics
		paint(offGraphics);
		// draw the offscreen image onto the window
		BufferedImage bImage = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = bImage.getGraphics();
		g2.drawImage(offImage, 0, 0, null);
		// how to darken image
		// https://docs.oracle.com/javase/tutorial/2d/images/drawimage.html
		float[] scales = { 1f, 1f, 1f, 1f };
		if (isDark) {
			scales[0] = 0.2f;
			scales[1] = 0.2f;
			scales[2] = 0.2f;
			scales[3] = 1f;
		}
		// float[] scales = { 0.2f, 0.2f, 0.2f, 1f };// orginal 6 Oct 13:54
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);

		Graphics2D g2d = (Graphics2D) g;
		/* Draw the image, applying the filter */
		g2d.drawImage(bImage, rop, 0, 0);
		// rescaling the image to darken it
		// RescaleOp op = new RescaleOp(1.1f, 0.0f, null);
		// offImage = op.filter(offImage, null);
		// g.drawImage(offImage, 0, 0, this);

	}

	public void createRoomImage() {
		Graphics roomGraphics;
		Image roomImage = null;
		Dimension d = getPreferredSize();
		roomImage = createImage(d.width, d.height);
		roomGraphics = roomImage.getGraphics();
		roomPaint(roomGraphics);
		RoomImage = roomImage;
	}

	public void roomPaint(Graphics g) {
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles.length; col++) {
				RenderRoomTile tile = this.tiles[row][col];
				Point point = IsoHelper.twoDToIso(col, row, width, height);
				g.drawImage(Imagehelper.testStone, xOffset + point.x, yOffset
						+ point.y, null, null);
			}
		}
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
			// only creates the room Image at the start of when the image has
			// tnot been created

			if (!roomRendered) {
				createRoomImage();
				roomRendered = true;
			}
			/*for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles.length; col++) {
					RenderRoomTile tile = this.tiles[row][col];
					Point point = IsoHelper.twoDToIso(col, row, width, height);
					// g.drawImage(Imagehelper.Stone, xOffset + point.x, yOffset
					// // orginal line 5 Oct 19:55
					g.drawImage(Imagehelper.testStone, xOffset + point.x,
							yOffset + point.y, null, null);
				}
			}*/
			g.drawImage(RoomImage, 0, 0, null, null);
			for (int row = 0; row < tiles.length; row++) {
				for (int col = tiles.length-1; col >= 0; col--) {
					RenderEntity ent = this.entities[col][row];
					Point point = IsoHelper.twoDToIso(col, row, width, height);

					if (ent instanceof RenderPlayer) {

						 g.drawImage(Imagehelper.Grass, xOffset + point.x,yOffset + point.y,null,null);
								 // //original line here. changed 5 Oct 19:26
						/*g.drawImage(Imagehelper.testPlayer, xOffset + point.x,
								yOffset + point.y, null, null);*/
					}

					if (ent instanceof RenderKeyCard) { // MORE GROSS SHITT
						// //System.out.println("RENDER KEY CARD");
						g.drawImage(Imagehelper.key, xOffset + point.x, yOffset
								+ point.y, null, null);

					}

					if (ent instanceof RenderTeleporter) {
						int xOff = width / 3;
						int yOff = 0;
						g.drawImage(Imagehelper.coin, xOffset + xOff + point.x,
								yOffset + yOff + point.y, null, null);
					}

					if (ent instanceof RenderZombie) {
						g.drawImage(Imagehelper.Zombie, xOffset + point.x,
								yOffset + point.y, null, null);
					}
					if (ent instanceof RenderOuterWall) {
						CardinalDirection dir = ent
								.getFacingCardinalDirection();
						int xW = 0;
						int yW = 0;

						Image wall = null;
						switch (dir) {
						case NORTH:
							xW = -7;
							yW = -(Imagehelper.WallNS.getHeight(null) - height / 2);
							wall = Imagehelper.WallNS;
							break;
						case SOUTH:
							xW = (width / 2) - 7;
							yW = -(Imagehelper.WallNS.getHeight(null) - height);
							wall = Imagehelper.WallNS;
							break;
						case WEST:
							xW = 0;
							yW = -(Imagehelper.WallEW.getHeight(null) - height);
							wall = Imagehelper.WallEW;
							break;
						case EAST:
							xW = width / 2;
							yW = -(Imagehelper.WallEW.getHeight(null) - height / 2);
							wall = Imagehelper.WallEW;
							break;
						}

						g.drawImage(wall, xOffset + point.x + xW, yOffset
								+ point.y + yW, null, null);

						/*
						 * bot wall: xW = (width/2)-7 yW = -(wallHeigh - height)
						 * left wall: xW = width/2 yW = -(wallheight -
						 * (height/2) rightWall xW = 0 yW = -(wallheight -
						 * height)
						 */

/*
						  int xW = -7; int yW =
						  -(Imagehelper.Wall.getHeight(null) - height / 2);
						  g.drawImage(Imagehelper.Wall, xOffset + point.x + xW,
						  yOffset + point.y + yW, null, null);*/

					}

				}
			}
		}
	}
}
