/* ControlDeskEvent.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */


import java.util.ArrayList;

/**
 * Class that represents control desk event
 *
 */
class ControlDeskEvent {

	/** A representation of the wait queue, containing party names */
	private final ArrayList<String> partyQueue;

    /**
     * Contstructor for the ControlDeskEvent
     *
     * @param partyQueue	a ArrayList of Strings containing the names of the parties in the wait queue
     *
     */

	public ControlDeskEvent(final ArrayList<String> partyQueue ) {
		this.partyQueue = partyQueue;
	}

    /**
     * Accessor for partyQueue
     *
     * @return a ArrayList of Strings representing the names of the parties in the wait queue
     *
     */

	public ArrayList<String> getPartyQueue() {
		return partyQueue;
	}

}
