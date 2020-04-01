import Widget.ContainerPanel;
import Widget.WindowFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

class AddPartyView implements ActionListener, ListSelectionListener {

    private static final String ERR_MEMBER_EXISTS = "Member already in Party";
    private final int maxSize;

    private final Widget.ButtonPanel buttonPanel;
    private final WindowFrame win;
    private final Widget.ScrollablePanel<String> partyPanel;
    private final Widget.ScrollablePanel<Object> bowlerPanel;

    private final Vector<String> party;
    private final ControlDeskView controlDesk;
    private Vector<Object> bowlerDB;
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
        //noinspection ProhibitedExceptionCaught
        try {
            bowlerDB = new Vector<Object>(BowlerFile.getBowlers());
        } catch (final IOException e) {
            System.err.println("File Error, the path or permissions for the File are incorrect, check pwd.");
            bowlerDB = new Vector<>();
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.err.println("Array Index out of Bounds Error, you may have trailing whitespace in BOWLERS_DAT.");
            bowlerDB = new Vector<>();
        }
        bowlerPanel = new Widget.ScrollablePanel<>("Bowler Database", bowlerDB, 8, this);

        // Button Panel
        buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put(BTN_ADD_PATRON, this)
                .put(BTN_REM_PATRON, this)
                .put(BTN_NEW_PATRON, this)
                .put(BTN_FINISHED, this);

        // Window
        win = new WindowFrame(
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

    private void onPartyFinished() {
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

    public void valueChanged(final ListSelectionEvent e) {
        final Object source = e.getSource();
        if (source.equals(bowlerPanel.getList())) {
            selectedNick =
                    ((String) ((JList) source).getSelectedValue());
        }
        if (source.equals(partyPanel.getList())) {
            selectedMember =
                    ((String) ((JList) source).getSelectedValue());
        }
    }

    void updateNewPatron(final NewPatronView newPatron) {
        final String nickName = newPatron.getNickName();
        final Vector<Object> res = BowlerFile.putBowlerIfDidntExist(nickName, newPatron.getFull(), newPatron.getEmail());
        if (res != null) {
            bowlerDB = new Vector<Object>(res);
            bowlerPanel.setListData(bowlerDB);
            party.add(nickName);
            partyPanel.setListData(party);
        } else {
            System.err.println("A Bowler with that name already exists.");
        }
    }

    public Vector<String> getParty() {
        return (Vector<String>) party.clone();
    }
}
