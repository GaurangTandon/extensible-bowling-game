/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

import Widget.ButtonPanel;
import Widget.WindowFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Vector;

/**
 * Class for representation of the control desk
 */
public class ControlDeskView implements ActionListener, Observer {

    private final WindowFrame win;
    private final Widget.ButtonPanel controlsPanel;
    private JList<?> partyList;

    /**
     * The maximum  number of members in a party
     */
    private final int maxMembers;
    private final ControlDeskInterface controlDesk;

    private static final String BTN_ADD_PARTY = "Add Party";
    private static final String BTN_ASSIGN = "Assign lanes";
    private static final String BTN_FINISHED = "Finished";

    private JPanel setupLaneStatusPanel(final int numLanes) {
        final JPanel laneStatusPanel = new JPanel();
        laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        final HashSet<? extends Lane> lanes = controlDesk.getLanes();
        int laneCount = 1;
        for (final Lane curLane : lanes) {
            final LaneStatusView laneStat = new LaneStatusView(curLane, (laneCount + 1));
            curLane.subscribe(laneStat);
            final JPanel lanePanel = laneStat.showLane();
            lanePanel.setBorder(new TitledBorder("Lane " + laneCount));
            laneCount++;
            laneStatusPanel.add(lanePanel);
        }

        return laneStatusPanel;
    }

    private JPanel setupPartyPanel() {
        final JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Queue"));

        final Vector<String> empty = new Vector<>();
        empty.add("(Empty)");

        partyList = new JList<Object>(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(10);
        final JScrollPane partyPane = new JScrollPane(partyList);
        partyPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        return partyPanel;
    }

    /**
     * Displays a GUI representation of the ControlDesk
     */

    ControlDeskView(final ControlDeskInterface controlDesk, @SuppressWarnings("SameParameterValue") final int maxMembers) {
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.getNumLanes();

        final ButtonPanel controls = new ButtonPanel(3, 1, "Controls");
        controlsPanel = controls
                .put(BTN_ADD_PARTY, this)
                .put(BTN_ASSIGN, this)
                .put(BTN_FINISHED, this);

        win = new WindowFrame(
                "Control Desk",
                new Widget.ContainerPanel()
                        .put(controlsPanel, "East")
                        .put(setupLaneStatusPanel(numLanes), "Center")
                        .put(setupPartyPanel(), "West")
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
        partyList.setListData(((ControlDeskEvent) ce).getPartyQueue());
    }
}
