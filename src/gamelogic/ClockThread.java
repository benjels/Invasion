package gamelogic;

/**
 * maintains a consistent "pulse". On this pulse, we call the clockTick()
 * method on our instance of ServerTrueGameState which will gather all of the events from the players and independent actors
 * and apply them to the game state, then send a drawable version of that game state out over the network to all of the players. The interval
 * between pulses must not exceed the amount of time needed to service this
 * pulse, else we will not be able to apply and then display events in the game
 * reliably.
 *
 * @author brownmax1
 *
 */
public class ClockThread extends Thread {

	private final int interval; // interval between pulses in us
	private final Server serverGame; // the server game main class that we will be giving the pulses to


	public ClockThread(int delay, Server serverGame) {
		this.interval = delay;
		this.serverGame = serverGame;
	}

	/**
	 * runs constantly and maintains a consistent rate of event application and "game frame" sending.
	 */
	@Override
	public void run() {
		while (true) {
			// we are looping until the program is closed
			try {
				Thread.sleep(this.interval);
				this.serverGame.clockTick(); //tell the serverGame to send out an event to all the players because enough time has passed for them to all have applied the last event they were sent

			} catch (InterruptedException e) {
				// dead code
			}
		}
	}

}
