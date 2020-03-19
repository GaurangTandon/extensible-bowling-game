/* ControlDeskEvent.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */


import java.util.Vector;

/**
 * Class that represents control desk event
 *
 */
class ControlDeskEvent {

	/** A representation of the wait queue, containing party names */
	private final Vector partyQueue;

    /**
     * Contstructor for the ControlDeskEvent
     *
     * @param partyQueue	a Vector of Strings containing the names of the parties in the wait queue
     *
     */

	public ControlDeskEvent( Vector partyQueue ) {
		this.partyQueue = partyQueue;
	}

    /**
     * Accessor for partyQueue
     *
     * @return a Vector of Strings representing the names of the parties in the wait queue
     *
     */

	public Vector getPartyQueue() {
		return partyQueue;
	}

}
