/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Vector;

/**
 * Class for representation of the control desk
 */
public class ControlDeskView implements ActionListener, ControlDeskObserver {

    private JButton addParty;
    private JButton finished;
    private JButton assign;
    private final JFrame win;
    private JList<?> partyList;

    /**
     * The maximum  number of members in a party
     */
    private final int maxMembers;

    private final ControlDeskInterface controlDesk;

    private JButton buttonUtil(final String text, final JPanel controlPanel) {
        final JButton button = new JButton(text);
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        button.addActionListener(this);
        panel.add(button);
        controlPanel.add(panel);

        return button;
    }

    private void setupControlsPanel(final JPanel colPanel) {
        final JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));
        controlsPanel.setBorder(new TitledBorder("Controls"));

        addParty = buttonUtil("Add Party", controlsPanel);
        assign = buttonUtil("Assign Lanes", controlsPanel);
        finished = buttonUtil("Finished", controlsPanel);
        controlsPanel.remove(assign);

        colPanel.add(controlsPanel, "East");
    }

    private void setupLaneStatusPanel(final JPanel colPanel, final int numLanes) {
        final JPanel laneStatusPanel = new JPanel();
        laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        final HashSet<? extends Lane> lanes = controlDesk.getLanes();
        int laneCount = 1;
        for (final Lane curLane : lanes) {
            final LaneStatusView laneStat = new LaneStatusView(curLane, (laneCount + 1));
            curLane.subscribe(laneStat);
            curLane.getPinsetter().subscribe(laneStat);
            final JPanel lanePanel = laneStat.showLane();
            lanePanel.setBorder(new TitledBorder("Lane " + laneCount));
            laneCount++;
            laneStatusPanel.add(lanePanel);
        }

        colPanel.add(laneStatusPanel, "Center");
    }

    private void setupPartyPanel(final JPanel colPanel) {
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

        colPanel.add(partyPanel, "West");
    }

    private void showWindowInScreenCenter() {
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    /**
     * Displays a GUI representation of the ControlDesk
     */

    ControlDeskView(final ControlDeskInterface controlDesk, final int maxMembers) {
        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.getNumLanes();

        win = new JFrame("Control Desk");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        setupControlsPanel(colPanel);
        setupLaneStatusPanel(colPanel, numLanes);
        setupPartyPanel(colPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        /* Close program when this window closes */
        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });

        showWindowInScreenCenter();
    }

    /**
     * Handler for actionEvents
     *
     * @param e the ActionEvent that triggered the handler
     */

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(addParty)) {
            new AddPartyView(this, maxMembers);
        } else if (source.equals(assign)) {
            controlDesk.assignLane();
        } else if (source.equals(finished)) {
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

    public final void receiveControlDeskEvent(final ControlDeskEvent ce) {
        partyList.setListData(ce.getPartyQueue());
    }
}
