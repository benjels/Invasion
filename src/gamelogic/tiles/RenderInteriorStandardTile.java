package gamelogic.tiles;

import java.awt.Image;
import java.awt.Point;

public class RenderInteriorStandardTile extends RenderRoomTile {



	RenderInteriorStandardTile() {
	}


	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported yet");
	}


	@Override
	public Point getOffset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(){
		return "tile";
	}



}
