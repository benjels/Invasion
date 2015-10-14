package gamelogic;
/**
 * this clock takes care of the actual time in the game world. This is used to calculate what time it is for the day/night cycles and also for incrementing the score.
 * This needs to be distinct from the clock thread because the game time and score should not be dependent on how the tick rate which is dependent on things like how quickly
 * we can redraw and send frames over the network.
 * @author brownmax1
 *
 */
public class GameWorldTimeClockThread extends Thread{

	private int seconds = 0;
	private int minutes = 0;
	private int hours = 20; //starting time is 8pm


	private final WorldGameState gameState;

	public GameWorldTimeClockThread(WorldGameState gameState){
		this.gameState = gameState;
	}


	@Override
	public void run() {
		while (true) {
			// we are looping until the program is closed
			try {
				//wait a GAME second (3 ms) and then update everything
				Thread.sleep(3);
				//add a second that can trigger a cascade of changes to the time
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

				//set time
				String updatedTime = createStringTime();
				this.gameState.setTimeOfDay(updatedTime);

				//increment score
				if(this.minutes == 59 && this.seconds == 0){ //add a point every game hour
					this.gameState.incrementPlayerScore();
				}

				//set if night
				if(hours == 23 && minutes == 00 && seconds == 0){
					this.gameState.setDark(true);
				}
				//set if day
				if(hours == 6 && minutes == 00 && seconds == 0){
					this.gameState.setDark(false);
				}
			} catch (InterruptedException e) {
				// dead code
			}
		}
	}

	/**
	 * creates a pretty printable version of the game time to be eventually printed on the gui
	 * @return string the printable version of the current time
	 */
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
