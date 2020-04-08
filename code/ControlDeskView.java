import Widget.ButtonPanel;
import Widget.WindowFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControlDeskView implements ActionListener, Observer {
    private final WindowFrame win;
    private Widget.ButtonPanel controlsPanel;

    private final int maxMembers;
    private final ControlDesk controlDesk;
    private final Widget.ScrollablePanel<Object> partyPanel;

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

    private void setupControlsPanel() {
        controlsPanel = new ButtonPanel(4, 1, "Controls")
                .put(ButtonNames.BTN_ADD_PARTY, this)
                .put(ButtonNames.BTN_ASSIGN, this)
                .put(ButtonNames.BTN_QUERIES, this)
                .put(ButtonNames.BTN_FINISHED, this);
    }

    ControlDeskView(final ControlDesk controlDesk,
                    @SuppressWarnings("SameParameterValue") final int maxMembers) {
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.numLanes;

        setupControlsPanel();

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
        final String source = ((AbstractButton) e.getSource()).getText();
        switch (source) {
            case ButtonNames.BTN_ADD_PARTY:
                new AddPartyView(this, maxMembers);
                break;
            case ButtonNames.BTN_ASSIGN:
                controlDesk.assignLane();
                break;
            case ButtonNames.BTN_FINISHED:
                win.setVisible(false);
                System.exit(0);
            case ButtonNames.BTN_QUERIES:
                new AdhocView();
                break;
        }
    }

    final void updateAddParty(final AddPartyView addPartyView) {
        controlDesk.addPartyToQueue(addPartyView.getParty());
    }

    public final void receiveEvent(final Event ce) {
        partyPanel.setListData(((ControlDeskEvent) ce).getPartyQueue());
    }
}
