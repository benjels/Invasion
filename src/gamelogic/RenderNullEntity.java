package gamelogic;

import java.awt.Image;

public class RenderNullEntity extends RenderEntity {



	RenderNullEntity(CardinalDirection directionFaced){
		super(directionFaced);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do thisyet fame");
	}



}
