package graphics;

import gamelogic.CardinalDirection;
import gamelogic.DrawableRoomState;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderMazeWall;
import gamelogic.renderentities.RenderNullEntity;
import gamelogic.renderentities.RenderOuterWall;
import gamelogic.renderentities.RenderPylonAttacker;
import gamelogic.tiles.RenderRoomTile;
import imagehelper.GCImageH;
import imagehelper.IsoHelper;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.Color;

//import gamelogic.events.RenderTeleporterTile;

/**
 * GameCanvas Game canvas class represents the game state Drawn in isometric
 * form
 *
 * @author Joely Huang 300305742
 *
 */

@SuppressWarnings("serial")
public class GameCanvas extends Canvas {
	private RenderRoomTile[][] tiles;
	private RenderEntity[][] entities;
	private boolean roomRendered = false;
	private Image RoomImage = null;
	private GCImageH helper = new GCImageH();
	private final Font LARGEFONT = new Font("Arial Bold", Font.PLAIN, 24); // original
																			// Arial
																			// Bold
																			// ;

	// offsets for drawing the game
	private int xOffset = 10;
	private int yOffset = 420;

	// size of the images
	int width = 64;
	int height = 32;

	// states of the room
	private int roomId;
	private boolean isDark;
	private CardinalDirection roomDir;
	private boolean gameOver = false;

	public GameCanvas() {

	}

	/**
	 * Gets the state that needs to be drawn
	 *
	 * @param state
	 *            - the state that is drawn
	 */
	public void setDrawableState(DrawableRoomState state) {
		this.tiles = state.getTiles();
		this.entities = state.getEntities();
		if (state.getRoomId() != roomId) {
			roomRendered = false;
			this.roomId = state.getRoomId();
		}
		this.isDark = state.isDark();
		this.roomDir = state.getViewingOrientation();
		this.gameOver = state.isGameOver();
	}

	/**
	 * gets the size of the game
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension d = new Dimension(1500, 800);
		return d;
	}

	/**
	 * draws the game over image
	 * @param g
	 */
	public void gameOver(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Font LARGEFONT = new Font("Arial Bold", Font.PLAIN, 60); // original
																	// Arial
																	// Bold ;

		Color darkBorderColor = new Color(14, 34, 0);
		Color lightGreenColor = new Color(88, 223, 54);

		g2d.setColor(darkBorderColor);
		g2d.setFont(LARGEFONT);

		g2d.setColor(darkBorderColor);
		g2d.fillRect(0, 0, 1500, 800);

		g2d.setColor(lightGreenColor);
		g2d.drawString("Game Over", 560, 400);
	}

	/**
	 * redraws the entire board, does double buffering
	 *
	 * @param g
	 *            - the graphics pane it is drawn to
	 */
	@Override
	public void update(Graphics g) {
		// draws all objects on an Image and then renders entire image to stop
		// flickering
		// http://stackoverflow.com/questions/10508042/how-do-you-double-buffer-in-java-for-a-game

		Graphics offGraphics;
		BufferedImage offImage = null;
		Dimension d = getPreferredSize();

		// Creating the offscreen image to draw on
		offImage = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_ARGB);

		// setting the offScreen graphics to the offscreen Image Graphics
		offGraphics = offImage.getGraphics();
		if(!gameOver){
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
			float[] offsets = new float[4];
			RescaleOp rop = new RescaleOp(scales, offsets, null);
			Graphics2D g2d = (Graphics2D) g;

			// applying filter to image
			g2d.drawImage(bImage, rop, 0, 0);
		} else{
			gameOver(g);
		}

	}

	/**
	 * Creates the room image that is needed to be painted to the screen
	 */
	public void createRoomImage() {
		Graphics roomGraphics;
		Image roomImage = null;
		Dimension d = getPreferredSize();
		roomImage = createImage(d.width, d.height);
		roomGraphics = roomImage.getGraphics();
		roomPaint(roomGraphics);
		RoomImage = roomImage;
	}

	/**
	 * Paints the room to the graphics pane
	 *
	 * @param g
	 *            - graphics pane that needs to be painted to (the image for
	 *            double buffering)
	 */
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

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void paint(Graphics g) {
		if (this.tiles != null && this.entities != null) {
			assert (this.tiles != null && this.entities != null) : "this.tiles cant be null";
			// only creates the room Image at the start of when the image has
			// not been created

			if (!roomRendered) {
				createRoomImage();
				roomRendered = true;
			}

			// specify which row to start drawing from, for room orienation
			g.drawImage(RoomImage, 0, 0, null, null);

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
		}
	}

	/**
	 * Draws the north view of the game
	 *
	 * @param g
	 *            - graphics pane to draw to
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
					Image entity = helper.getGCImages().get(ent.getName());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	/**
	 * Draws the south view of the game
	 *
	 * @param g
	 *            - graphics pane to draw to
	 */
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
						switch (((RenderOuterWall) ent).getDir()) {// changing
																	// the
																	// direction
																	// the walls
																	// are
																	// facing
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
					Image entity = helper.getGCImages().get(ent.getName());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	/**
	 * Draws the west view of the game
	 *
	 * @param g
	 *            - graphics pane to draw to
	 */
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
						switch (((RenderMazeWall) ent).getDir()) {// changing
																	// the
																	// direction
																	// the maze
																	// walls are
																	// facing
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
					Image entity = helper.getGCImages().get(ent.getName());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}

	/**
	 * Draws the east view of the game
	 *
	 * @param g
	 *            - graphics pane to draw to
	 */
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
					Image entity = helper.getGCImages().get(ent.getName());
					g.drawImage(entity, xOffset + point.x + xO, yOffset
							+ point.y + yO, null, null);
				}
				x--;
			}
			y++;
		}
	}
}
