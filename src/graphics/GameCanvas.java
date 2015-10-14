package graphics;

import gamelogic.CardinalDirection;
import gamelogic.DrawableRoomState;
import gamelogic.entities.Pylon;
import gamelogic.entities.RenderCoin;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderMazeWall;
import gamelogic.entities.RenderKeyCard;
import gamelogic.entities.RenderNullEntity;
import gamelogic.entities.RenderOuterWall;
import gamelogic.entities.RenderPlayer;
//import gamelogic.events.RenderTeleporterTile;

import gamelogic.entities.RenderPylon;
import gamelogic.entities.RenderSmallCarrier;
import gamelogic.entities.RenderStandardTeleporter;
import gamelogic.entities.RenderPylonAttacker;
import gamelogic.tiles.RenderRoomTile;
import imagehelper.GCImageH;
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
	private GCImageH helper = new GCImageH();

	// offsets for drawing the game
	private int xOffset = 10;
	private int yOffset = 420;

	// size of the images
	int width = 64;
	int height = 32;

	private int roomId;
	private boolean isDark;
	private CardinalDirection roomDir;

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
		this.roomDir = state.getViewingOrientation();
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
		for (int row = 1; row < tiles.length - 1; row++) {
			for (int col = 1; col < tiles.length - 1; col++) {
				RenderRoomTile tile = this.tiles[row][col];
				Point point = IsoHelper.twoDToIso(col, row, width, height);
				Image tileImage = helper.getGCImages().get(tile.toString());
				// int yO = -GCImageH.height;
				int yO = 0;
				g.drawImage(tileImage, xOffset + point.x, yOffset + yO
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
			// not been created

			if (!roomRendered) {
				createRoomImage();
				roomRendered = true;
			}

			// specify which row to start drawing from, for room orienation
			g.drawImage(RoomImage, 0, 0, null, null);

			// drawNEWalls(g);
			// drawing the entities
			switch (roomDir) {
			case NORTH:
				drawNorth(g);
				break;
			case SOUTH:
				drawSouth(g);
				break;
			case WEST:
				drawWest(g);
				break;
			case EAST:
				drawEast(g);
				break;
			}

			// drawSWWalls(g);
		}
	}

	/*
	 * public void drawSWWalls(Graphics g) {
	 *
	 * // rendering south wall for (int col = tiles.length - 1; col >= 0; col--)
	 * { RenderEntity ent = this.entities[col][tiles.length - 1]; Point point =
	 * IsoHelper.twoDToIso(col, tiles.length - 1, width, height); // the offset
	 * for the object int xO = ent.getOffset().x; int yO = ent.getOffset().y; //
	 * getting the image object Image entity =
	 * helper.getGCImages().get(ent.toString()); g.drawImage(entity, xOffset +
	 * point.x + xO, yOffset + point.y + yO, null, null);
	 *
	 * }
	 *
	 * // rendering west wall for (int row = 0; row <= tiles.length - 1; row++)
	 * { RenderEntity ent = this.entities[0][row]; Point point =
	 * IsoHelper.twoDToIso(0, row, width, height); // the offset for the object
	 * int xO = ent.getOffset().x; int yO = ent.getOffset().y; // getting the
	 * image object Image entity = helper.getGCImages().get(ent.toString());
	 * g.drawImage(entity, xOffset + point.x + xO, yOffset + point.y + yO, null,
	 * null);
	 *
	 * }
	 *
	 * }
	 *
	 * public void drawNEWalls(Graphics g) { // rendering north wall for (int
	 * col = tiles.length - 1; col >= 0; col--) { RenderEntity ent =
	 * this.entities[col][0]; Point point = IsoHelper.twoDToIso(col, 0, width,
	 * height); // the offset for the object int xO = ent.getOffset().x; int yO
	 * = ent.getOffset().y; // getting the image object Image entity =
	 * helper.getGCImages().get(ent.toString()); g.drawImage(entity, xOffset +
	 * point.x + xO, yOffset + point.y + yO, null, null);
	 *
	 * }
	 *
	 * // rendering east wall for (int row = 0; row <= tiles.length - 1; row++)
	 * { RenderEntity ent = this.entities[tiles.length - 1][row]; Point point =
	 * IsoHelper.twoDToIso(tiles.length - 1, row, width, height); // the offset
	 * for the object int xO = ent.getOffset().x; int yO = ent.getOffset().y; //
	 * getting the image object Image entity =
	 * helper.getGCImages().get(ent.toString()); g.drawImage(entity, xOffset +
	 * point.x + xO, yOffset + point.y + yO, null, null);
	 *
	 * }
	 *
	 * }
	 */

	public void drawNorth(Graphics g) {
		int x = tiles.length - 1;
		int y = 0;

		// x is now columns, y is now rows
		for (int row = 0; row <= tiles.length - 1; row++) {
			x = tiles.length - 1;
			for (int col = tiles.length - 1; col >= 0; col--) {
				RenderEntity ent = this.entities[col][row];
				if (!(ent instanceof RenderNullEntity)) {
					Point point = IsoHelper.twoDToIso(x, y, width, height);
					// the offset for the object
					int xO = ent.getOffset().x;
					int yO = ent.getOffset().y;
					// getting the image object
					Image entity = helper.getGCImages().get(ent.toString());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	public void drawSouth(Graphics g) {
		int x = tiles.length - 1;
		int y = 0;
		// x is now columns, y is now rows
		for (int row = tiles.length - 1; row >= 0; row--) {
			x = tiles.length - 1;
			for (int col = 0; col <= tiles.length - 1; col++) {
				RenderEntity ent = this.entities[col][row];
				if (!(ent instanceof RenderNullEntity)) {
					Point point = IsoHelper.twoDToIso(x, y, width, height);
					if (ent instanceof RenderOuterWall) {
						switch (((RenderOuterWall) ent).getDir()) {
						case NORTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.SOUTH);
							break;
						case SOUTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.NORTH);
							break;
						case WEST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.EAST);
							break;
						case EAST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.WEST);
							break;
						}

					} else if (ent instanceof RenderPylonAttacker) {
						((RenderPylonAttacker) ent).changeDir(roomDir);
					}
					// the offset for the object
					int xO = ent.getOffset().x;
					int yO = ent.getOffset().y;
					// getting the image object
					Image entity = helper.getGCImages().get(ent.toString());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	public void drawWest(Graphics g) {
		int x = tiles.length - 1;
		int y = 0;
		// x is now rows, y is now columns
		for (int col = tiles.length - 1; col >= 0; col--) {
			x = tiles.length - 1;
			for (int row = tiles.length - 1; row >= 0; row--) {
				RenderEntity ent = this.entities[col][row];
				if (!(ent instanceof RenderNullEntity)) {
					Point point = IsoHelper.twoDToIso(x, y, width, height);
					if (ent instanceof RenderOuterWall) {
						switch (((RenderOuterWall) ent).getDir()) {
						case NORTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.WEST);
							break;
						case SOUTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.EAST);
							break;
						case WEST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.SOUTH);
							break;
						case EAST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.NORTH);
							break;
						}

					} else if (ent instanceof RenderMazeWall) {
						switch (((RenderMazeWall) ent).getDir()) {
						case NORTH:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.WEST);
							break;
						case SOUTH:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.EAST);
							break;
						case WEST:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.SOUTH);
							break;
						case EAST:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.NORTH);
							break;
						}
					} else if (ent instanceof RenderPylonAttacker) {
						((RenderPylonAttacker) ent).changeDir(roomDir);
					}
					// the offset for the object
					int xO = ent.getOffset().x;
					int yO = ent.getOffset().y;
					// getting the image object
					Image entity = helper.getGCImages().get(ent.toString());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	public void drawEast(Graphics g) {
		int x = tiles.length - 1;
		int y = 0;
		// x is now rows, y is now columns
		for (int col = 0; col <= tiles.length - 1; col++) {
			x = tiles.length - 1;
			for (int row = 0; row <= tiles.length - 1; row++) {
				RenderEntity ent = this.entities[col][row];
				if (!(ent instanceof RenderNullEntity)) {
					Point point = IsoHelper.twoDToIso(x, y, width, height);
					if (ent instanceof RenderOuterWall) {
						switch (((RenderOuterWall) ent).getDir()) {
						case NORTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.EAST);
							break;
						case SOUTH:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.WEST);
							break;
						case WEST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.NORTH);
							break;
						case EAST:
							((RenderOuterWall) ent)
									.setDir(CardinalDirection.SOUTH);
							break;
						}

					} else if (ent instanceof RenderMazeWall) {
						switch (((RenderMazeWall) ent).getDir()) {
						case NORTH:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.EAST);
							break;
						case SOUTH:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.WEST);
							break;
						case WEST:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.NORTH);
							break;
						case EAST:
							((RenderMazeWall) ent)
									.setDir(CardinalDirection.SOUTH);
							break;
						}
					} else if (ent instanceof RenderPylonAttacker) {
						((RenderPylonAttacker) ent).changeDir(roomDir);
					}
					// the offset for the object
					int xO = ent.getOffset().x;
					int yO = ent.getOffset().y;
					// getting the image object
					Image entity = helper.getGCImages().get(ent.toString());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}
}
