import java.util.*;

class ControlDesk extends Publisher implements Runnable {
    private final List<Lane> lanes;
    private final LinkedList<ScorableParty> partyQueue;
    final int numLanes;

    ControlDesk(final int numLanes) {
        this.numLanes = numLanes;
        lanes = new ArrayList<>(numLanes);
        partyQueue = new LinkedList<>();

        for (int i = 0; i < numLanes; i++) {
            final Lane lane = new Lane();
            lanes.add(lane);
            new Thread(lane).start();
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            assignLane();

            Util.busyWait(250);
        }
    }

    final void assignLane() {
        for (final Lane lane : lanes) {
            if (partyQueue.isEmpty()) break;
            else if (!lane.isPartyAssigned()) {
                lane.assignParty(partyQueue.pollFirst());
            }
        }

        publish();
    }

    void addPartyToQueue(final Iterable<String> partyNicks) {
        final ScorableParty newParty = new ScorableParty();

        for (final String partyNick : partyNicks) {
            final Bowler gotBowler = Util.getPatronDetails(partyNick);
            final ScorableBowler newBowler = new ScorableBowler(gotBowler);
            newParty.addBowler(newBowler);
        }

        partyQueue.add(newParty);
        publish();
    }

    Event createEvent() {
        final ArrayList<String> displayPartyQueue = new ArrayList<>(0);

        for (final ScorableParty party : partyQueue) {
            final String nextParty = party.getName();
            displayPartyQueue.add(nextParty);
        }

        return new ControlDeskEvent(displayPartyQueue);
    }

    List<Lane> getLanes() {
        return lanes;
    }
}
