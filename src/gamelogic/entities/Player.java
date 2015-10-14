package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.RoomLocation;
import gamelogic.RoomState;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;
import gamelogic.renderentities.RenderEntity;
import gamelogic.renderentities.RenderPlayer;

/**
 * This class represents the information about one of the players/clients that is specific to an
 * individual player. e.g.  name, chosen gameCharacter, health etc
 * @author brownmax1
 *
 */



public class Player extends MovableEntity implements Damageable{

	private final String irlName;//players actual name


	private int healthPercentage = 100; //the percentage of health that this player currently has TODO: this will be managed through the strategy shit
	private int coins = 0;// the amount of coins that the player has at the moment
	private Carrier currentInventory = new StandardInventory(CardinalDirection.NORTH, this);//the carrier that is currently displayerd on the player's screen as their "inventory"


	private CardinalDirection directionThatIsUp = CardinalDirection.NORTH; //the cardinal direction that is currently "up" for this player. (i.e. that direction is at the top of their screen at the moment).
																		// this is used to modify the user's movement requests. e.g. if the user pressed up and the directionThatIsUp is EAST,
																		//we will move them "right"/EAST on the game-board rather than "up"/NORTH.

	private final CharacterStrategy playerStrategy; // the character that this player chose


	//ITEM/INV STATUSES

	private boolean nightVisionEnabled = false;
	private boolean keyEnabled = false;
	private boolean hasGun = false;
	private boolean hasTeleGun = false;
	private int healthKitAmount = 0;
	private boolean isDead = false;

	public Player(String irlName, int Uid, CharacterStrategy playerStrategy, CardinalDirection initialDirectionFaced, RoomState spawnRoom){
		super(initialDirectionFaced, Uid, spawnRoom);
		this.irlName = irlName;
		this.playerStrategy = playerStrategy;

	}




@Override
public void takeDamage(int pureDamageAmount) {
	//the player is quite resilient, so it only takes a small amount of the pure damage
	double dmgDone = (double)pureDamageAmount;  //(have to convert to double here because attacks should always do SOME damage. if we just have ints, we might divide the damage and then round down to 0)
	dmgDone = Math.ceil(dmgDone / 100);
	this.healthPercentage -= (int)dmgDone;
	//if the player just took damage that caused it to die, it's game over
	if(this.healthPercentage <= 0){
		this.isDead = true;
	}
}

	public int getMoveDistance() { //used to determine the offset of the desired move on the board (i.e. current location + this player move distance)
			return 1;
	}



	public RoomLocation getLocation(){
		return new RoomLocation(this.getxInRoom(), this.getyInRoom());
	}




	public CardinalDirection getDirectionThatIsUp() {
		return directionThatIsUp;
	}



	public void setDirectionThatIsUp(CardinalDirection directionThatIsUp) {
		this.directionThatIsUp = directionThatIsUp;
	}





	@Override
	public
	RenderEntity generateDrawableCopy() {
		return new RenderPlayer( this.playerStrategy, this.getFacingCardinalDirection(), this.healthPercentage);
	}



	public int getHealthPercentage() {
		return healthPercentage;
	}



	public void setHealthPercentage(int healthPercentage) {
		this.healthPercentage = healthPercentage;
	}



	public int getCoins() {
		return coins;
	}




	public void setCoins(int coins) {
		this.coins = coins;
	}







	public String getIrlName() {
		return irlName;
	}



	public CharacterStrategy getCharacter(){ //TODO: set public for package divison
		return this.playerStrategy;
	}






/**
 * add 1 to the count of coins that this player has at the moment.
 * this is called when the player steps over a coin on the game board
 */
public void addCoin() {
	this.coins ++;

}






public void setNightVision(boolean hasNightVision) {
	this.nightVisionEnabled  = hasNightVision;

}

public boolean hasNightVisionEnabled(){
	return this.nightVisionEnabled;
}

public void setKeyEnabled(boolean hasKey){
	this.keyEnabled = hasKey;
}

public boolean hasKeyEnabled(){
	return this.keyEnabled;
}





public Carrier getCurrentInventory() {
	return this.currentInventory;
}

public void setCurrentInventory(Carrier newInventory){
	this.currentInventory = newInventory;
}


public CharacterStrategy getStrategy(){
	return this.playerStrategy;
}






//JOSH ADDED THIS
public String toXMLString(){
	return "Player-" + this.irlName + "-" + getUniqueId() + "-" + this.healthPercentage + "-" + this.coins + "-" + this.getCharacter() + "-" + this.nightVisionEnabled + "-"
			+ this.keyEnabled + "-" + this.hasGun + "-" + this.hasTeleGun + "-" + this.healthKitAmount + "-" + this.getCurrentRoom().getId();
}

public void setHealthKit(int amount){
	this.healthKitAmount = amount;
}










//USED TO CHANGE WHICH DIRECTION THE PLAYER CURRENTLY CONSIDERS "UP"
public boolean attemptClockwiseRotationEvent(PlayerEvent eventWeNeedToUpdateStateWith) {

	if(this.getDirectionThatIsUp() == CardinalDirection.NORTH){
		this.setDirectionThatIsUp(CardinalDirection.EAST);
	}else if(this.getDirectionThatIsUp() == CardinalDirection.EAST){
		this.setDirectionThatIsUp(CardinalDirection.SOUTH);
	}else if(this.getDirectionThatIsUp() == CardinalDirection.SOUTH){
		this.setDirectionThatIsUp(CardinalDirection.WEST);
	}else{ // in case that we facing west
		this.setDirectionThatIsUp(CardinalDirection.NORTH);
	}

	return true;
}











public void setHasGun(boolean hasGun) {
	this.hasGun = hasGun;

}











public void setHasTeleGun(boolean hasTele) {
	this.hasTeleGun = hasTele;

}











public boolean hasGun() {
	return hasGun;
}











public boolean hasTeleGun() {
	return hasTeleGun;
}










//used when health kit picked up
public void addHealthKit() {
	this.healthKitAmount ++;

}

//used when health kit picked up
//either used or dropped
public void removeHealthKit() {
	this.healthKitAmount --;

}











public int getHealthKitsAmount() {
	return this.healthKitAmount;
}



public boolean useHealthKit() {
	//can only heal if health kit in current inventory
	if(!(this.currentInventory.attemptExpendHealthKit())){
		return false;
	}

	//update health amount
	if(this.healthPercentage + 10 > 100){
		this.healthPercentage = 100;
	}else{
		this.healthPercentage += 10;
	}


	//decrement count of health kits
	healthKitAmount --;


	return true;
}











public boolean isDead() {
	return this.isDead;
}















}
