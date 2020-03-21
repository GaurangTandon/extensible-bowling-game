/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 *
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 *
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 *
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 *
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 *
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 *
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 *
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 *
 *
 */

import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

/**
 * Class that represents control desk
 */
class ControlDesk extends Thread implements ControlDeskInterface {

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
     * The collection of subscribers
     */
    private final Vector<ControlDeskObserver> subscribers;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numLanes the numbler of lanes to be represented
     */

    ControlDesk(final int numLanes) {
        this.numLanes = numLanes;
        lanes = new HashSet<>(numLanes);
        partyQueue = new Queue();

        subscribers = new Vector<>();

        for (int i = 0; i < numLanes; i++) {
            lanes.add(new Lane());
        }

        // TODO: assert somewhere after this that the size of lanes stays constant

        start();

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
     * @return a Bowler object.
     */

    private Bowler registerPatron(final String nickName) {
        Bowler patron = null;

        try {
            // only one patron / nick.... no dupes, no checks
            patron = BowlerFile.getBowlerInfo(nickName);
        } catch (final IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     */
    public final void assignLane() {
        for (final Lane lane : lanes) {
            if (partyQueue.hasMoreElements()) {
                if (!lane.isPartyAssigned()) {
                    lane.assignParty(partyQueue.next());
                }
            } else break;
        }

        publish(new ControlDeskEvent(getPartyQueue()));
    }

    /**
     *
     */

    public void viewScores(final Lane ln) {
        // TODO: attach a LaneScoreView object to that lane
    }

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks A Vector of NickNames
     */

    public final void addPartyToQueue(final Vector<String> partyNicks) {
        final Vector<Bowler> partyBowlers = new Vector<>();
        for (final String partyNick : partyNicks) {
            final Bowler newBowler = registerPatron(partyNick);
            partyBowlers.add(newBowler);
        }
        final Party newParty = new Party(partyBowlers);
        partyQueue.add(newParty);
        publish(new ControlDeskEvent(getPartyQueue()));
    }

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
     *
     * @return a Vecotr of Strings
     */

    private Vector<String> getPartyQueue() {
        final Vector<String> displayPartyQueue = new Vector<>();
        final Vector<Party> pQueue = partyQueue.asVector();

        for (Party party : pQueue) {
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

    /**
     * Allows objects to subscribe as observers
     *
     * @param controlDeskObserver the ControlDeskObserver that will be subscribed
     */

    void subscribe(final ControlDeskObserver controlDeskObserver) {
        subscribers.add(controlDeskObserver);
    }

    /**
     * Broadcast an event to subscribing objects.
     *
     * @param event the ControlDeskEvent to broadcast
     */

    private void publish(final ControlDeskEvent event) {
        for (final ControlDeskObserver subscriber : subscribers) {
            subscriber.receiveControlDeskEvent(event);
        }
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
