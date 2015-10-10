package gamelogic.events;

import gamelogic.entities.Portal;

//A PLAYER ATTEMPTING TO PLACE A PORTAL. WILL ONLY BE ALLOWED IF THE THAT PLAYER .hasTeleGun
public class useTeleGunEvent extends PlayerEvent implements SpatialEvent {
	private final Portal myPortal; //the portal that will have one of its gates placed
	
	
	public useTeleGunEvent(int uid, Portal telegunPortal) {
		super(uid);
		this.myPortal = telegunPortal;
	}


	public Portal getMyPortal() {
		return myPortal;
	}

}
