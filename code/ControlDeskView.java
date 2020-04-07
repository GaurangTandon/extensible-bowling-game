import Widget.ButtonPanel;
import Widget.WindowFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControlDeskView implements ActionListener, Observer {
    private final WindowFrame win;
    private final Widget.ButtonPanel controlsPanel;

    private final int maxMembers;
    private final ControlDesk controlDesk;
    private final Widget.ScrollablePanel<Object> partyPanel;
    private static final String BTN_ADD_PARTY = "Add Party";
    private static final String BTN_ASSIGN = "Assign lanes";
    private static final String BTN_QUERIES = "Queries";
    private static final String BTN_FINISHED = "Finished";

    private Widget.ContainerPanel setupLaneStatusPanel(final int numLanes) {
        final Widget.ContainerPanel laneStatusPanel = new Widget.ContainerPanel(numLanes, 1, "Lane Status");
        final List<Lane> lanes = controlDesk.getLanes();
        int laneCount = 1;

        for (final Lane lane : lanes) {
            final LaneStatusView laneStat = new LaneStatusView(lane, laneCount);
            lane.subscribe(laneStat);
            laneStatusPanel.put(new Widget.ContainerPanel(
                    laneStat.showLane(), "Lane " + laneCount));
            laneCount++;
        }
        return laneStatusPanel;
    }

    ControlDeskView(final ControlDesk controlDesk,
                    @SuppressWarnings("SameParameterValue") final int maxMembers) {
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.numLanes;

        controlsPanel = new ButtonPanel(4, 1, "Controls")
                .put(BTN_ADD_PARTY, this)
                .put(BTN_ASSIGN, this)
                .put(BTN_QUERIES, this)
                .put(BTN_FINISHED, this);

        final ArrayList<Object> empty = new ArrayList<>(0);
        empty.add("(Empty)");
        partyPanel = new Widget.ScrollablePanel<>(
                "Party Queue", empty, 10);
        win = new WindowFrame(
                "Control Desk",
                new Widget.ContainerPanel()
                        .put(controlsPanel, "East")
                        .put(setupLaneStatusPanel(numLanes), "Center")
                        .put(partyPanel, "West")
        );
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(controlsPanel.get(BTN_ADD_PARTY))) {
            new AddPartyView(this, maxMembers);
        } else if (source.equals(controlsPanel.get(BTN_ASSIGN))) {
            controlDesk.assignLane();
        } else if (source.equals(controlsPanel.get(BTN_FINISHED))) {
            win.setVisible(false);
            System.exit(0);
        } else if (source.equals(controlsPanel.get(BTN_QUERIES))) {
            new AdhocView();
        }
    }

    final void updateAddParty(final AddPartyView addPartyView) {
        controlDesk.addPartyToQueue(addPartyView.getParty());
    }

    public final void receiveEvent(final Event ce) {
        partyPanel.setListData(((ControlDeskEvent) ce).getPartyQueue());
    }
}
