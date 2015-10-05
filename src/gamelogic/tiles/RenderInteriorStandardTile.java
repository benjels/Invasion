package gamelogic.tiles;

import java.awt.Image;

public class RenderInteriorStandardTile extends RenderRoomTile {



	RenderInteriorStandardTile(boolean isBloody) {
		super(isBloody);
	}


	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported yet");
	}



}
