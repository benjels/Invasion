package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderImpassableColomn extends RenderEntity{


	RenderImpassableColomn(CardinalDirection directionFaced){
		super(directionFaced, "I can't move through this.");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do this yet");
	}

}
