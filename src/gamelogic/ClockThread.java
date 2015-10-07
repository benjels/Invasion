package gamelogic;

/**
 * maintains a consistent "pulse". On this pulse, we call the receiveClockPulse
 * method on the our instance of ServerTrueGameState which will send out the
 * event at the head of the queue to all of the connected players. The interval
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

	@Override
	public void run() {
		while (true) {
			// we are looping until the program is closed
			try {
				Thread.sleep(this.interval);
				this.serverGame.clockTick(); //tell the serverGame to send out an event to all the players because enough time has passed for them to all have applied the last event they were sent
//TODO: right after it tells the server to tick, it should tell all the ai to generate their next events so that they
//will be ready for next time the clock ticks
			} catch (InterruptedException e) {
				// dead code imo
			}
		}
	}

}
