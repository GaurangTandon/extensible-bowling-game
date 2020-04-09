package Observer;

import java.util.ArrayList;

/**
 * Class that represents control desk event
 */
public class ControlDeskEvent implements Event {

    /**
     * A representation of the wait queue, containing party names
     */
    private final ArrayList<String> partyQueue;

    /**
     * Constructor for the Observer.ControlDeskEvent
     *
     * @param partyQueue a Vector of Strings containing the names of the parties in the wait queue
     */

    public ControlDeskEvent(final ArrayList<String> partyQueue) {
        this.partyQueue = partyQueue;
    }

    /**
     * Accessor for partyQueue
     *
     * @return a Vector of Strings representing the names of the parties in the wait queue
     */

    public final ArrayList<String> getPartyQueue() {
        return partyQueue;
    }
}
