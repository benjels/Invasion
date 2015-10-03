package gamelogic;

import gamelogic.entities.RenderEntity;

import java.util.ArrayList;

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
	private final int playerRoomId; //needed by hud to highlight current room on minimap //TODO: add room id number field to RoomState class
	private final String playerIrlName; //needed by hud to display player's actual name
	private final int score;
	private final ArrayList<RenderEntity> carriedEntities;



	DrawablePlayerInfo(int playerRoomId, int healthPercentage, int coinsCollected, CharacterStrategy playerCharacter, String realName, int score, ArrayList<RenderEntity> inventory){
		this.healthPercentage = healthPercentage;
		this.coinsCollected = coinsCollected;
		this.playerCharacter = playerCharacter;
		this.playerRoomId = playerRoomId;
		this.playerIrlName = realName;
		this.score = score;
		this.carriedEntities = inventory;
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

	public int getPlayerRoomId() {
		return playerRoomId;
	}

	public String getPlayerIrlName() {
		return playerIrlName;
	}
}
