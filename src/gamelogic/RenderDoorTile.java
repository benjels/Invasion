package gamelogic;

import java.awt.Image;

public class RenderDoorTile extends RenderRoomTile {


	RenderDoorTile(boolean isBloody) {
		super(isBloody);
	}

	@Override
	public Image getImg() {
	throw new RuntimeException("this not supported atm");
	}

}
