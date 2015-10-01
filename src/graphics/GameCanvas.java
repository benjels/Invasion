package graphics;

import gamelogic.DrawableRoomState;
import gamelogic.entities.KeyCard;
import gamelogic.entities.RenderEntity;
import gamelogic.entities.RenderImpassableColomn;
import gamelogic.entities.RenderKeyCard;
import gamelogic.entities.RenderPlayer;
//import gamelogic.events.RenderTeleporterTile;

import gamelogic.entities.RenderTeleporter;
import gamelogic.tiles.RenderRoomTile;
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
	private boolean roomRendered = false;
	private Image RoomImage = null;
	private Image tileImage = Imagehelper.loadImage2("stone64.png");

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

	// draws all objects on an Image and then renders entire image to stop
	// flickering
	// http://stackoverflow.com/questions/10508042/how-do-you-double-buffer-in-java-for-a-game

	@Override
	public void update(Graphics g) {
		Graphics offGraphics;
		Image offImage = null;
		Dimension d = getPreferredSize();

		// Creating the offscreen image to draw on
		offImage = createImage(d.width, d.height);
		// setting the offScreen graphics to the offscreen Image Graphics
		offGraphics = offImage.getGraphics();
		// painting all objects onto the offGraphics
		paint(offGraphics);
		// draw the offscreen image onto the window
		g.drawImage(offImage, 0, 0, this);

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
		int xOffset = 10;
		int yWallOffset = 64;
		int yOffset = 350;
		int width = 64;
		int height = 32;
		System.out.println("tilesLength" + tiles.length);
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles.length; col++) {
				RenderRoomTile tile = this.tiles[row][col];
				Point point = IsoHelper.twoDToIso(col, row, width, height);
				g.drawImage(tileImage, xOffset + point.x, yOffset + point.y,
						null, null);
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

			int xOffset = 10;
			int yWallOffset = 64;
			int yOffset = 350;
			int width = 64;
			int height = 32;
			int xWall = 1;
			int yWall = -76;
			// only creates the room Image at the start of when the image has
			// tnot been created
			if (!roomRendered) {
				createRoomImage();
				roomRendered = true;
			}
			g.drawImage(RoomImage, 0, 0, null, null);
			for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles.length; col++) {
					RenderEntity ent = this.entities[col][row];
					Point point = IsoHelper.twoDToIso(col, row, width, height);

					if (ent instanceof RenderPlayer) {
						Image tileImage = Imagehelper.loadImage2("ConcreteWall.png");
						g.drawImage(tileImage, xOffset + point.x + xWall, yOffset
								+ point.y + yWall, null, null);
						/*Image tileImage = Imagehelper.loadImage2("grass64.png");
						//System.out.println("Player position:");
						//System.out.println("ROW: " + row);
						//System.out.println("COL: " + col);
						g.drawImage(tileImage, xOffset + point.x, yOffset
								+ point.y, null, null);*/
					}

					if (ent instanceof RenderKeyCard) { // MORE GROSS SHITT
						/*Image tileImage = Imagehelper.loadImage2("wall64.png");
						g.drawImage(tileImage, xOffset + point.x, yOffset
								+ point.y, null, null);*/
					}

					if (ent instanceof RenderTeleporter) { // MORE GROSS SHITT
						Image tileImage = Imagehelper.loadImage2("Wall64.png");
						g.drawImage(tileImage, xOffset + point.x, yOffset
								+ point.y, null, null);
					}

					if (ent instanceof RenderImpassableColomn) { // MORE GROSS SHITT
						
						Image tileImage = Imagehelper.loadImage2("ConcreteWall.png");
						g.drawImage(tileImage, xOffset + point.x + xWall, yOffset
								+ point.y + yWall, null, null);
					}

				}
			}
		}
	}
}
