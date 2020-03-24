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
 */
class ControlDeskEvent {

    /**
     * A representation of the wait queue, containing party names
     */
    private final Vector partyQueue;

    /**
     * Constructor for the ControlDeskEvent
     *
     * @param partyQueue a Vector of Strings containing the names of the parties in the wait queue
     */

    ControlDeskEvent(final Vector partyQueue) {
        this.partyQueue = partyQueue;
    }

    /**
     * Accessor for partyQueue
     *
     * @return a Vector of Strings representing the names of the parties in the wait queue
     */

    final Vector getPartyQueue() {
        return partyQueue;
    }

}
