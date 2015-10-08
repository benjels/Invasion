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
	private int hours = 12; //start game at midday
	
	//basically the cool down timer works by putting a cooldown method and field for each abililty in the class that has that ability. every game second that method is called and if the ability was used , we start ticking the timer up until it reaches e/g/ 50 at which point it is set back down to 0 and that means that the player can use the abililty again.
	
	@Override
	public void run() {
		while (true) {
			// we are looping until the program is closed
			try {
				//wait a second and then update everything
				Thread.sleep(50); //so 50 ms is a second lul
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
			} catch (InterruptedException e) {
				// dead code imo
			}
		}
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
