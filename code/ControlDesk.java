import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

class ControlDesk extends Publisher implements Runnable {
    private final HashSet<Lane> lanes;
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

    private GeneralBowler getPatronDetails(final String nickName) {
        GeneralBowler patron = null;

        try {
            patron = BowlerFile.getBowlerInfo(nickName);
        } catch (final IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }

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

    public void addPartyToQueue(final Iterable<String> partyNicks) {
        final Vector<GeneralBowler> partyBowlers = new Vector<>();
        for (final String partyNick : partyNicks) {
            final GeneralBowler newBowler = getPatronDetails(partyNick);
            partyBowlers.add(newBowler);
        }
        final GeneralParty newParty = new Party(partyBowlers);
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

    public HashSet<Lane> getLanes() {
        return lanes;
    }
}
