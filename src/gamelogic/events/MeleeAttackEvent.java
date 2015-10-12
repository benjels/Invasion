package gamelogic.events;

public class MeleeAttackEvent extends PlayerEvent implements SpatialEvent {
	
	private final int hitDamage;//the pure damage inflicted by this hit

	public MeleeAttackEvent(int uid, int hitPureDamage) {
		super(uid);
		this.hitDamage = hitPureDamage;
	}

	public int getHitDamage() {
		return hitDamage;
	}

}
