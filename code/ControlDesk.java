import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

class ControlDesk extends Publisher implements Runnable {
    private final Set<Lane> lanes;
    private final Queue partyQueue;
    final int numLanes;

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

    public void run() {
        while (true) {
            assignLane();

            Util.busyWait(250);
        }
    }

    final void assignLane() {
        for (final Lane lane : lanes) {
            if (partyQueue.hasMoreElements()) {
                if (!lane.isPartyAssigned()) {
                    lane.assignParty(partyQueue.next());
                }
            } else break;
        }

        publish();
    }

    void addPartyToQueue(final Iterable<String> partyNicks) {
        final GeneralParty newParty = new Party();

        for (final String partyNick : partyNicks) {
            final GeneralBowler newBowler = Util.getPatronDetails(partyNick);
            newParty.addBowler(newBowler);
        }

        partyQueue.add(newParty);
        publish();
    }

    Event createEvent() {
        final Vector<String> displayPartyQueue = new Vector<>();
        final Iterable<GeneralParty> pQueue = partyQueue.asVector();

        for (final GeneralParty party : pQueue) {
            final String nextParty = party.getName();
            displayPartyQueue.addElement(nextParty);
        }

        return new ControlDeskEvent(displayPartyQueue);
    }

    Iterable<Lane> getLanes() {
        return lanes;
    }
}
