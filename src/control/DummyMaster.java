package control;

import gamelogic.ClientFrame;
import gamelogic.IDedPlayerEvent;
import gamelogic.IDedPlayerNull;

public class DummyMaster {




	private DummySlave slave; //the slave that talks to this master
	private IDedPlayerEvent currentBufferedEvent = new IDedPlayerNull(); //the latest event sent by the slave
	private final int uid; //the unique id of the player that this master is for

	public DummyMaster (int uid){
		this.uid = uid;
	}

	/// IN TO SERVER ///
	/**
	 *	 * receives an event sent from the slave to be fetched by the server on the next tick
	 * @param upEvent the event that was sent from the client to the server
	 */
	public void sendEventSlaveToMaster(IDedPlayerEvent event) {

		this.currentBufferedEvent = event;

	}

	/**
	 * give the currently held event.
	 * used by the server on each tick to fetch an event by the player whose Master
	 * this is so that the server can update the true state of the game
	 * @return IDedPlayerEvent the event that the player last requested to perform.
	 */
	public IDedPlayerEvent fetchEvent() {
		//put event in buffer in temp
		IDedPlayerEvent tempBufferedEvent = this.currentBufferedEvent;
		//set the event in this buffer to null (we don't want to reapply the same event on the next tick)
		this.currentBufferedEvent = new IDedPlayerNull();
		//return the event that was fetched (will be the null if no new event by player since last tick (likely))
		return tempBufferedEvent;
	}




	///OUT TO PLAYER ///
	/**
	 * receives an event from the server's queue of events to send out to all the clients
	 *
	 */
	public void sendClientFrameMasterToSlave(ClientFrame gameToPaint){
		this.slave.sendGameStateMasterToSlave(gameToPaint);
	}



	///HACKY FAKE NETWORKING SHIT///
/**
 * idk exactly how setup between a client wanting to connect to server will work
 * look at createMasterForSlave method in Server
 * @param slave
 */
	public void giveSlave(DummySlave slave) {
		this.slave = slave;
	}



	///UTILITY///

	public int getUid() {
		return this.uid;
	}

	/**
	 * used to determine whether the player has actually done anything since the last
	 * tick.
	 * @return bool false if the event in the buffer is a null event (i.e. we already applied the last requested event from this player) else true.
	 */
	public boolean hasEvent() {
		//sanity check//
		assert(this.currentBufferedEvent != null):"if its actual null we have serioius error here";

		if(this.currentBufferedEvent instanceof IDedPlayerNull){
			return false;
		}else{
			return true;
		}
	}


}
