/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 *
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 *
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 *
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 *
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Class for GUI components need to add a party
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */

class AddPartyView implements ActionListener, ListSelectionListener {

    private static final String ERR_MEMBER_EXISTS = "Member already in Party";
    private final int maxSize;

    private final Widget.ButtonPanel buttonPanel;

    private final JFrame win;
    private final JList<String> partyList;
    private final JList<Object> allBowlers;
    private final Vector<String> party;
    private final ControlDeskView controlDesk;
    private Vector<Object> bowlerdb;

    private String selectedNick, selectedMember;

    AddPartyView(final ControlDeskView controlDesk, final int max) {
        this.controlDesk = controlDesk;
        maxSize = max;

        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        final JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        party = new Vector<>();
        final Vector<String> empty = new Vector<>();
        empty.add("(Empty)");

        partyList = new JList<>(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(5);
        partyList.addListSelectionListener(this);
        final JScrollPane partyPane = new JScrollPane(partyList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Bowler Database
        final JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerdb = new Vector<Object>(BowlerFile.getBowlers());
        } catch (final Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector<>();
        }
        allBowlers = new JList<>(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        final JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put("addPatron", "Add to Party", this)
                .put("remPatron", "Remove Member", this)
                .put("newPatron", "New Patron", this)
                .put("finished", "Finished", this);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel.get("_panel"));

        win.getContentPane().add("Center", colPanel);
        win.pack();
        // Center WindowPanel on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    private void addPatron() {
        if (selectedNick != null && party.size() < maxSize) {
            if (party.contains(selectedNick)) {
                System.err.println(ERR_MEMBER_EXISTS);
            } else {
                party.add(selectedNick);
                partyList.setListData(party);
            }
        }
    }

    private void removePatron() {
        if (selectedMember != null) {
            party.removeElement(selectedMember);
            partyList.setListData(party);
        }
    }

    private void onPartyFinished(){
        if (party != null && !party.isEmpty()) {
            controlDesk.updateAddParty(this);
        }
        win.setVisible(false);
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(buttonPanel.get("addPatron"))) {
            addPatron();
        }

        if (source.equals(buttonPanel.get("remPatron"))) {
            removePatron();
        }

        if (source.equals(buttonPanel.get("newPatron"))) {
            new NewPatronView(this);
        }

        if (source.equals(buttonPanel.get("finished"))) {
            onPartyFinished();
        }
    }

    /**
     * Handler for List actions
     * @param e the ListActionEvent that triggered the handler
     */

    public void valueChanged(final ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
        if (e.getSource().equals(partyList)) {
            selectedMember =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }

    /**
     * Called by NewPatronView to notify AddPartyView to update
     *
     * @param newPatron the NewPatronView that called this method
     */

    void updateNewPatron(final NewPatronView newPatron) {
        try {
            final Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNickName());
            if (checkBowler == null) {
                BowlerFile.putBowlerInfo(
                        newPatron.getNickName(),
                        newPatron.getFull(),
                        newPatron.getEmail());
                bowlerdb = new Vector<Object>(BowlerFile.getBowlers());
                allBowlers.setListData(bowlerdb);
                party.add(newPatron.getNickName());
                partyList.setListData(party);
            } else {
                System.err.println("A Bowler with that name already exists.");
            }
        } catch (final Exception e2) {
            System.err.println("File I/O Error");
        }
    }

    /**
     * Accessor for Party
     */

    public Vector<String> getParty() {
        return (Vector<String>) party.clone();
    }

}
