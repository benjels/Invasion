package gamelogic;

import java.awt.Image;
import java.awt.Point;

import gamelogic.entities.RenderEntity;

public class RenderZombieAttacker extends RenderEntity {
	
	//textual desc
		private static final String INV_IMAGE_TEXTUAL_DESC = "an unfriendly zombie.";
		//image file names:
		private static final String INV_IMAGE_FILE_NAME = "NA";
		private static final String GAME_IMAGE_NAME = "joely fill this in tbh";


	public RenderZombieAttacker(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);

	}

	@Override
	public Image getImg() {
		throw new RuntimeException("no ooo");//TODO: all these ened to be deleted
	}

	@Override
	public Point getOffset() {
		// TODO Auto-generated method stub
		//throw new RuntimeException("no ooo");//TODO: all these ened to be deleted i think joely using the map for these now?
		//return null;
		return new Point(0, 0);
	}

}
