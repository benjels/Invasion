package gamelogic;
/**
 * the wrapper class for all the information the HUD/player's UI needs to display.
 *e.g. coins collected, HP etc
 * @author brownmax1
 *
 */
public class DrawablePlayerInfo {

	private final int healthPercentage; //needed by hud to draw health bar
	private final int coinsCollected; //needed by hud to draw coin amount
	private final CharacterStrategy playerCharacter; //needed by hud to draw player hud avatar
	private final RoomState playerRoom; //needed by hud to highlight current room on minimap //TODO: add room id number field to RoomState class
	private final String playerIrlName; //needed by hud to display player's actual name



	DrawablePlayerInfo(RoomState playerRoom, int healthPercentage, int coinsCollected, CharacterStrategy playerCharacter, String realName){
		this.healthPercentage = healthPercentage;
		this.coinsCollected = coinsCollected;
		this.playerCharacter = playerCharacter;
		this.playerRoom = playerRoom;
		this.playerIrlName = realName;
	}




	///UTILITY//

	public int getHealthPercentage() {
		return healthPercentage;
	}

	public int getCoinsCollected() {
		return coinsCollected;
	}

	public CharacterStrategy getPlayerCharacter() {
		return playerCharacter;
	}

	public RoomState getPlayerRoom() {
		return playerRoom;
	}

	public String getPlayerIrlName() {
		return playerIrlName;
	}
}
