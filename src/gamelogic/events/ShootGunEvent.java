package gamelogic.events;
//GENERATED WHEN THE PLAYER PRESSES ACTION 1 AND IS PLAYING AS THE FIGHTER. note that it will only result in shooting in the room if the player hasGun
public class ShootGunEvent extends PlayerEvent implements SpatialEvent{

	private final int shotDamage;//the pure damage inflicted by this shot

	public ShootGunEvent(int uid, int shotPureDamage) {
		super(uid);
		this.shotDamage = shotPureDamage;
	}

	public int getShotDamage() {
		return shotDamage;
	}

}
