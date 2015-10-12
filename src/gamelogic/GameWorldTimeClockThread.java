package gamelogic;
//UNLIKE THE OTHER CLOCK THREAD THAT TAKES CARE OF THE INTERVAL BETWEEN THE FOLLWOING HAPPENING:
//-scrape all movable entities' events
//-apply events
//-send drawable state out to players to be rendered
//... this class basically takes care of the actual 24 hour military time inside the game world
//currently it is just used to generate the time that is printed on the gui, but in the future it may be used to regulate the interval of 
//cooldowns. e.g. you can only warp every 5 game minutes or some shit
public class GameWorldTimeClockThread extends Thread{
		
	private int seconds = 0;
	private int minutes = 0;
	private int hours = 20; //start game at night now
	
	//basically the cool down timer works by putting a cooldown method and field for each abililty in the class that has that ability. every game second that method is called and if the ability was used , we start ticking the timer up until it reaches e/g/ 50 at which point it is set back down to 0 and that means that the player can use the abililty again.
	
	private final WorldGameState gameState;//used to call coolDownTick() which is then applied to all of the entities registered with teh game
	
	public GameWorldTimeClockThread(WorldGameState gameState){
		this.gameState = gameState;
	}
	
	
	@Override
	public void run() {
		while (true) {
			// we are looping until the program is closed
			try {
				//wait a GAME second and then update everything
				Thread.sleep(3); //so 3 ms is a second lul
				//add a second that can trigger a cascade of changes
				if(this.getSeconds() == 59){
					this.seconds = 0;
					if(this.getMinutes() == 59){
						this.minutes = 0;
						if(this.hours == 23){
							this.hours = 0;
						}else{
							this.hours ++;
						}
					}else{
						this.minutes ++;
					}
				}else{
					this.seconds ++;
				}
				//will prob also call applyGameTickTickToRegisteredEntities method here
	//SHOULD PROB JUST HAVE ONE CALL TO THE WORLD GAME STATE HERE THAT PASSES THE TIME TO THE WORLD GAME STATE ON EVERY GAME SECOND AND THE
			//WORLD GAME STATE DOES ALL THE LOGIC AND CALLS ENTITIES' RESPECTIVE COOLDOWN MAINTAINER METHODS ETC
				//set time
				String updatedTime = createStringTime();
				this.gameState.setTimeOfDay(updatedTime);
				//increment score
				if(this.minutes == 59 && this.seconds == 0){ //add a point every game hour
					this.gameState.incrementPlayerScore();
				}
						
				//set night
				if(hours == 23 && minutes == 00 && seconds == 0){
					this.gameState.setDark(true);
				}
				//set day
				if(hours == 6 && minutes == 00 && seconds == 0){
					this.gameState.setDark(false);
				}
			} catch (InterruptedException e) {
				// dead code imo
			}
		}
	}

//USED TO CREATE A NICE VERSION OF THE CURRENT TIME FOR MAX TO PRINT
	private String createStringTime() {
		String hours =  "" + this.hours;
		if (hours.length() == 1){
			hours = hours.concat("0");
		}
		String minutes = "" + this.minutes;
		if (minutes.length() == 1){
			minutes = "0".concat(minutes);
		}
		return hours.concat(minutes);
	}


	public int getSeconds() {
		return seconds;
	}


	public int getMinutes() {
		return minutes;
	}

	public int getHours(){
		return hours;
	}





	
	
	

}
