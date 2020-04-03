import Widget.ButtonPanel;
import Widget.WindowFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

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
        for (int laneCount = 0; laneCount < lanes.size(); laneCount++) {
            final LaneStatusView laneStat = new LaneStatusView(lanes.get(laneCount), laneCount + 1);
            lanes.get(laneCount).subscribe(laneStat);
            laneStatusPanel.put(new Widget.ContainerPanel(
                    laneStat.showLane(), "Lane " + laneCount));
        }
        return laneStatusPanel;
    }

    ControlDeskView(final ControlDesk controlDesk,
                    @SuppressWarnings("SameParameterValue") final int maxMembers) {
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.numLanes;

        final ButtonPanel controls = new ButtonPanel(3, 1, "Controls");
        controlsPanel = controls
                .put(BTN_ADD_PARTY, this)
                .put(BTN_ASSIGN, this)
                .put(BTN_FINISHED, this)
                .put(BTN_QUERIES, this);

        final Vector<Object> empty = new Vector<>();
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

    /**
     * Handler for actionEvents
     *
     * @param e the ActionEvent that triggered the handler
     */
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

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView the AddPartyView that is providing a new party
     */
    final void updateAddParty(final AddPartyView addPartyView) {
        controlDesk.addPartyToQueue(addPartyView.getParty());
    }

    /**
     * Receive a broadcast from a ControlDesk
     *
     * @param ce the ControlDeskEvent that triggered the handler
     */
    public final void receiveEvent(final Event ce) {
        partyPanel.setListData(((ControlDeskEvent) ce).getPartyQueue());
    }
}
