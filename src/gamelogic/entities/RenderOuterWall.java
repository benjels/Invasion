package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderOuterWall extends RenderEntity{

	RenderOuterWall(CardinalDirection directionFaced){
		super(directionFaced);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do this yet");
	}

}
