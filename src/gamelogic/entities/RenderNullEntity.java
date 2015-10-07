package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;

public class RenderNullEntity extends RenderEntity {



	public RenderNullEntity(CardinalDirection directionFaced){
		super(directionFaced, "null tbh");
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do thisyet fame");
	}



}
