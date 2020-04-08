import Widget.ButtonPanel;
import Widget.WindowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ControlDeskView extends WindowView implements ActionListener, Observer {

    private final int maxMembers;
    private final ControlDesk controlDesk;
    private final Widget.ScrollablePanel<Object> partyPanel;

    ControlDeskView(final ControlDesk controlDesk,
                    @SuppressWarnings("SameParameterValue") final int maxMembers) {
        super("Control Desk");
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        // Button Panel
        final Widget.ButtonPanel controlsPanel = new ButtonPanel(4, 1, "Controls")
                .put(ButtonNames.BTN_ADD_PARTY, this)
                .put(ButtonNames.BTN_ASSIGN, this)
                .put(ButtonNames.BTN_QUERIES, this)
                .put(ButtonNames.BTN_FINISHED, this);
        // Container Panel
        final Widget.ContainerPanel laneStatusPanel = new Widget.ContainerPanel(
                controlDesk.numLanes, 1, "Lane Status");
        for (int i = 1; i <= controlDesk.numLanes; i++) {
            final LaneStatusView laneStat = new LaneStatusView(controlDesk.getLane(i - 1), i);
            controlDesk.getLane(i - 1).subscribe(laneStat);
            laneStatusPanel.put(new Widget.ContainerPanel(
                    laneStat.showLane(), "Lane " + i));
        }
        // Party Queue
        final ArrayList<Object> empty = new ArrayList<>(0);
        empty.add("(Empty)");
        partyPanel = new Widget.ScrollablePanel<>(
                "Party Queue", empty, 10);
        // Put all together
        container
                .put(controlsPanel, "East")
                .put(laneStatusPanel, "Center")
                .put(partyPanel, "West");
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
