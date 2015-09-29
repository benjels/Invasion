package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.CharacterStrategy;
import gamelogic.RoomLocation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class represents the information about one of the players/clients that is specific to an
 * individual player. e.g.  irl name, chosen gameCharacter etc.
 * @author brownmax1
 *
 */



public class Player extends MovableEntity{
	private final int Uid; //the unique id of this player. Impportant so that the server knows which player we are talking about when it receives an event

	private final String irlName;//players actual name

	//private String PlayerOrientation = "North"; //TODO: obvs this will eventually be an enum but idk what orientation rly means atm. Do i need to store orientation of the room or just
	//the player or what. At the moment what this value is is whichever cardinal direction the player is currently treating as "up". So this will prob be North originally and that changes when
	//the player tries to switch the view


	private int healthPercentage; //the percentage of health that this player currently has
	private int coins;// the amount of coins that the player has at the moment


	private CardinalDirection directionCharacterFacing = CardinalDirection.NORTH; //the cardinal direction that the player's avatar is looking in
	private CardinalDirection directionThatIsUp = CardinalDirection.NORTH; //the cardinal direction that is currently "up" for this player. (i.e. that direction is at the top of their screen at the moment).
																		// this is used to modify the user's movement requests. e.g. if the user pressed up and the directionThatIsUp is EAST,
																		//we will move them "right"/EAST on the game-board rather than "up"/NORTH.

	private final CharacterStrategy playerStrategy; // the character that this player chose

	public Player(String irlName, int Uid, CharacterStrategy playerStrategy, CardinalDirection initialDirectionFaced){
		super(initialDirectionFaced);
		this.irlName = irlName;
		this.Uid = Uid;
		this.playerStrategy = playerStrategy;

	}







	///UTILITY///

	 public int getUid() { //TODO: set public for package divison
		return Uid;
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







	public CardinalDirection getDirectionCharacterFacing() {
		return directionCharacterFacing;
	}







	public void setDirectionCharacterFacing(CardinalDirection directionCharacterFacing) {
		this.directionCharacterFacing = directionCharacterFacing;
	}

















}
