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

import Widget.ContainerPanel;
import Widget.WindowPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
    private final Widget.WindowPanel win;
    private final Widget.ScrollablePanel<String> partyPanel;
    private final Widget.ScrollablePanel<Object> bowlerPanel;

    private final Vector<String> party;
    private final ControlDeskView controlDesk;
    private Vector<Object> bowlerdb;
    private String selectedNick, selectedMember;

    private static final String BTN_ADD_PATRON = "Add to Party";
    private static final String BTN_REM_PATRON = "Remove Member";
    private static final String BTN_NEW_PATRON = "New Patron";
    private static final String BTN_FINISHED = "Finished";

    AddPartyView(final ControlDeskView controlDesk, final int max) {
        this.controlDesk = controlDesk;
        maxSize = max;

        // Party Panel
        final Vector<String> empty = new Vector<>();
        empty.add("(Empty)");
        party = new Vector<>();
        partyPanel = new Widget.ScrollablePanel<>("Your Party", empty, 5, this);

        // Bowlers Panel
        try {
            bowlerdb = new Vector<Object>(BowlerFile.getBowlers());
        } catch (final IOException e) {
            System.err.println("File Error, the path or permissions for the File are incorrect, check pwd.");
            bowlerdb = new Vector<>();
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.err.println("Array Index out of Bounds Error, you may have trailing whitespace in BOWLERS_DAT.");
            bowlerdb = new Vector<>();
        }
        bowlerPanel = new Widget.ScrollablePanel<>("Bowler Database", bowlerdb, 8, this);

        // Button Panel
        buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put(BTN_ADD_PATRON, this)
                .put(BTN_REM_PATRON, this)
                .put(BTN_NEW_PATRON, this)
                .put(BTN_FINISHED, this);

        // Window
        win = new WindowPanel(
                "Add Party",
                new ContainerPanel(1, 3, "")
                        .put(partyPanel)
                        .put(bowlerPanel)
                        .put(buttonPanel)
        );
    }

    private void addPatron() {
        if (selectedNick != null && party.size() < maxSize) {
            if (party.contains(selectedNick)) {
                System.err.println(ERR_MEMBER_EXISTS);
            } else {
                party.add(selectedNick);
                partyPanel.setListData(party);
            }
        }
    }

    private void removePatron() {
        if (selectedMember != null) {
            party.removeElement(selectedMember);
            partyPanel.setListData(party);
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
        if (source.equals(buttonPanel.get(BTN_ADD_PATRON))) {
            addPatron();
        }
        if (source.equals(buttonPanel.get(BTN_REM_PATRON))) {
            removePatron();
        }
        if (source.equals(buttonPanel.get(BTN_NEW_PATRON))) {
            new NewPatronView(this);
        }
        if (source.equals(buttonPanel.get(BTN_FINISHED))) {
            onPartyFinished();
        }
    }

    /**
     * Handler for List actions
     * @param e the ListActionEvent that triggered the handler
     */

    public void valueChanged(final ListSelectionEvent e) {
        if (e.getSource().equals(bowlerPanel.getList())) {
            selectedNick =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
        if (e.getSource().equals(partyPanel.getList())) {
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
                bowlerPanel.setListData(bowlerdb);
                party.add(newPatron.getNickName());
                partyPanel.setListData(party);
            } else {
                System.err.println("A Bowler with that name already exists.");
            }
        } catch (final Exception e) {
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
