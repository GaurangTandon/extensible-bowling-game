import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

/**
 * Class that represents control desk
 */
class ControlDesk extends Publisher implements ControlDeskInterface, Runnable {

    private final HashSet<Lane> lanes;

    /**
     * The party wait queue
     */
    private final Queue partyQueue;

    /**
     * The number of lanes represented
     */
    private final int numLanes;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numLanes the number of lanes to be represented
     */

    ControlDesk(final int numLanes) {
        this.numLanes = numLanes;
        lanes = new HashSet<>(numLanes);
        partyQueue = new Queue();

        for (int i = 0; i < numLanes; i++) {
            final Lane lane = new Lane();
            lanes.add(lane);
            new Thread(lane).start();
        }
    }

    /**
     * Main loop for ControlDesk's thread
     */
    public void run() {
        while (true) {
            assignLane();

            Util.busyWait(250);
        }
    }


    /**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName The NickName of the Bowler
     * @return a GeneralBowler object.
     */

    private GeneralBowler registerPatron(final String nickName) {
        GeneralBowler patron = null;

        try {
            patron = BowlerFile.getBowlerInfo(nickName);
        } catch (final IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }

    /**
     * Iterate through the available lanes and assign the parties in the wait queue if lanes are available.
     */
    public final void assignLane() {
        for (final Lane lane : lanes) {
            if (partyQueue.hasMoreElements()) {
                if (!lane.isPartyAssigned()) {
                    lane.assignParty(partyQueue.next());
                }
            } else break;
        }

        publish();
    }

// --Commented out by Inspection START (25/03/20, 2:37 PM):
//    /**
//     *
//     */
//
//    public void viewScores(final Lane ln) {
//        // TODO: attach a LaneScoreView object to that lane
//    }
// --Commented out by Inspection STOP (25/03/20, 2:37 PM)

    /**
     * Creates a party from a Vector of nicknames and adds them to the wait queue.
     *
     * @param partyNicks A Vector of NickNames
     */

    public void addPartyToQueue(final Iterable<String> partyNicks) {
        final Vector<GeneralBowler> partyBowlers = new Vector<>();
        for (final String partyNick : partyNicks) {
            final GeneralBowler newBowler = registerPatron(partyNick);
            partyBowlers.add(newBowler);
        }
        final GeneralParty newParty = new Party(partyBowlers);
        partyQueue.add(newParty);
        publish();
    }

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
     *
     * @return a Vector of Strings
     */

    private Vector<String> getPartyQueue() {
        final Vector<String> displayPartyQueue = new Vector<>();
        final Iterable<GeneralParty> pQueue = partyQueue.asVector();

        for (final GeneralParty party : pQueue) {
            final String nextParty = party.getName();
            displayPartyQueue.addElement(nextParty);
        }
        return displayPartyQueue;
    }

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     *
     * @return an int containing the number of lanes represented
     */

    public final int getNumLanes() {
        return numLanes;
    }

    Event createEvent() {
        return new ControlDeskEvent(getPartyQueue());
    }

    /**
     * Accessor method for lanes
     *
     * @return a HashSet of Lanes
     */

    public HashSet<Lane> getLanes() {
        return lanes;
    }
}
