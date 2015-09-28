package gamelogic;

import java.awt.Image;

public class RenderTeleporterTile extends RenderRoomTile {


	RenderTeleporterTile(boolean isBloody) {
		super(isBloody);
	}

	@Override
	public Image getImg() {
	throw new RuntimeException("this not supported atm");
	}

}
