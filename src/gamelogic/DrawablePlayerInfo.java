package gamelogic;

import gamelogic.entities.RenderEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * the wrapper class for all the information the HUD/player's UI needs to display.
 *e.g. coins collected, HP etc
 * @author brownmax1
 *
 */
public class DrawablePlayerInfo implements Serializable{

	private final int healthPercentage; //needed by hud to draw health bar
	private final int coinsCollected; //needed by hud to draw coin amount
	private final CharacterStrategy playerCharacter; //needed by hud to draw player hud avatar
	private final int playerRoomId; //needed by hud to highlight current room on minimap //TODO: add room id number field to RoomState class
	private final String playerIrlName; //needed by hud to display player's actual name
	private final int score;
	private final ArrayList<RenderEntity> carriedEntities;
	private final int pylon0Health; //current health percentage of pylon at top of map
	private final int pylon1Health; //current health percentage of pylon at bottom of map
	private final String currentRoomName; //name description of the room that the player is currently in
	private final String currentTime; //current time in game world in 24 hour time


	DrawablePlayerInfo(int playerRoomId, int coinsCollected, int healthPercentage,  CharacterStrategy playerCharacter, String realName, int score, ArrayList<RenderEntity> inventory,int pylon0Health, int pylon1Health, String roomName, String currentTime){
		this.healthPercentage = healthPercentage;
		this.coinsCollected = coinsCollected;
		this.playerCharacter = playerCharacter;
		this.playerRoomId = playerRoomId;
		this.playerIrlName = realName;
		this.score = score;
		this.carriedEntities = inventory;
		this.pylon0Health = pylon0Health;
		this.pylon1Health = pylon1Health;
		this.currentRoomName = roomName;
		this.currentTime = currentTime;
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




	public String getCurrentRoomName() {
		return currentRoomName;
	}




	public int getPylon0Health() {
		return pylon0Health;
	}




	public int getPylon1Health() {
		return pylon1Health;
	}




	public String getCurrentTime() {
		return currentTime;
	}




	public ArrayList<RenderEntity> getCarriedEntities() {
		return carriedEntities;
	}




	public int getScore() {
		return score;
	}
}
