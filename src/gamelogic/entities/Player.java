package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.RoomLocation;
import gamelogic.events.ClientGeneratedEvent;
import gamelogic.events.PlayerEvent;

/**
 * This class represents the information about one of the players/clients that is specific to an
 * individual player. e.g.  irl name, chosen gameCharacter etc.
 * @author brownmax1
 *
 */



public class Player extends MovableEntity implements Damageable{

	private final String irlName;//players actual name

	//private String PlayerOrientation = "North"; //TODO: obvs this will eventually be an enum but idk what orientation rly means atm. Do i need to store orientation of the room or just
	//the player or what. At the moment what this value is is whichever cardinal direction the player is currently treating as "up". So this will prob be North originally and that changes when
	//the player tries to switch the view


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
	

	public Player(String irlName, int Uid, CharacterStrategy playerStrategy, CardinalDirection initialDirectionFaced){
		super(initialDirectionFaced, Uid);
		this.irlName = irlName;
		this.playerStrategy = playerStrategy;

	}











	public int getMoveDistance() { //used to determine the offset of the desired move on the board (i.e. current location + this player move distance)
			return 1; //TODO: this should use this player's character strategy e.g. this.characterStrategy.getMovementDistance() ... maybe in future could have a separate movementStrategy so that when you pickup powerups and shit it changes. Would rather do the dynamic strategy thing for the zombie ai tho (e.g. they can become enraged)
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
	RenderEntity generateDrawableCopy() { //TODO: set public for package divison
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










//USED TO SET THE CURRENT NIGHT VISION STATUS OF THIS PLAYER
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









//USED TO APPLY SELECTION EVENTS TO THE PLAYER'S INVENTORY AND TO GENERATE A DRAWABLE LIST VERSINO OF IT FOR THE GUI
public Carrier getCurrentInventory() {
	return this.currentInventory;
}

public void setCurrentInventory(Carrier newInventory){
	this.currentInventory = newInventory;
}



//USED SO THAT THE GAME LOOP CAN USE A PLAYER'S STRATEGY TO CONVERT CLIENT EVENTS INTO GAME EVENTS
public CharacterStrategy getStrategy(){
	return this.playerStrategy;
}











@Override
public void takeDamage(int pureDamageAmount) {
	//the player is quite resilient, so it only takes a small amount of the pure damage
	double dmgDone = (double)pureDamageAmount;  //(have to convert to double here because attacks should always do SOME damage. if we just have ints, we might divide the damage and then round down to 0)
	dmgDone = Math.ceil(dmgDone / 10);
	this.healthPercentage -= (int)dmgDone;
	//if the player just took damage that caused it to die, it's game over
	if(this.healthPercentage <= 0){
		throw new RuntimeException("GAME OVER: one of the players reached the following health: " + this.healthPercentage);
	}
}

//JOSH ADDED THIS
@Override
public String toString(){
	return "Player";
}










//USED TO CHANGE WHICH DIRECTION THE PLAYER CURRENTLY CONSIDERS "UP"
public boolean attemptClockwiseRotationEvent(PlayerEvent eventWeNeedToUpdateStateWith) {

	if(this.directionThatIsUp == CardinalDirection.NORTH){
		this.directionThatIsUp = CardinalDirection.EAST;
		System.out.println("changed direction up to east");
	}else if(this.directionThatIsUp == CardinalDirection.EAST){
		this.directionThatIsUp = CardinalDirection.SOUTH;
		System.out.println("changed direction up to south");
	}else if(this.directionThatIsUp == CardinalDirection.SOUTH){
		this.directionThatIsUp = CardinalDirection.WEST;
		System.out.println("changed direction up to west");
	}else{ // in case that we facing west
		this.directionThatIsUp = CardinalDirection.NORTH;
		System.out.println("changed direction up to north");
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









}
