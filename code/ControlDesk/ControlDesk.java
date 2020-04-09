package ControlDesk;

import Bowlers.BowlerInfo;
import Bowlers.ScorableBowler;
import Bowlers.ScorableParty;
import Lane.Lane;
import Utilities.Util;
import Widget.ContainerPanel;
import Observer.Publisher;
import Observer.Event;
import Lane.LaneStatusView;
import Observer.ControlDeskEvent;

import javax.swing.*;
import java.util.*;

public class ControlDesk extends Publisher implements Runnable {
    private final List<Lane> lanes;
    private final LinkedList<ScorableParty> partyQueue;
    final int numLanes;

    public ControlDesk(final int numLanes) {
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
            final BowlerInfo gotBowler = Util.getPatronDetails(partyNick);
            final ScorableBowler newBowler = new ScorableBowler(gotBowler);
            newParty.addBowler(newBowler);
        }

        partyQueue.add(newParty);
        publish();
    }

    public Event createEvent() {
        final ArrayList<String> displayPartyQueue = new ArrayList<>(0);

        for (final ScorableParty party : partyQueue) {
            final String nextParty = party.getName();
            displayPartyQueue.add(nextParty);
        }

        return new ControlDeskEvent(displayPartyQueue);
    }

    ContainerPanel generateLaneStatusPanel() {
        final ContainerPanel laneStatusPanel = new Widget.ContainerPanel(
                numLanes, 1, "Lane.Lane Status");
        for (int i = 0; i < numLanes; i++) {
            laneStatusPanel.put(new Widget.ContainerPanel(renderLane(i), "Lane " + (i + 1)));
        }
        return laneStatusPanel;
    }

    private JPanel renderLane(final int laneIndex) {
        final LaneStatusView laneStat = new LaneStatusView(lanes.get(laneIndex), laneIndex + 1);
        lanes.get(laneIndex).subscribe(laneStat);
        return laneStat.showLane();
    }
}
