import Widget.ContainerPanel;
import Widget.WindowFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

class AddPartyView implements ActionListener, ListSelectionListener {

    private static final String ERR_MEMBER_EXISTS = "Member already in Party";
    private final int maxSize;

    private final WindowFrame win;
    private final Widget.ScrollablePanel<String> partyPanel;
    private Widget.ScrollablePanel<Object> bowlerPanel;

    private final ArrayList<String> party;
    private final ControlDeskView controlDesk;
    private ArrayList<Object> bowlerDB;
    private String selectedNick, selectedMember;

    private void buildBowlerPanel() {
        //noinspection ProhibitedExceptionCaught
        try {
            bowlerDB = new ArrayList<>(BowlerFile.getBowlers());
        } catch (final IOException e) {
            System.err.println("File Error, the path or permissions for the File are incorrect, check pwd.");
            bowlerDB = new ArrayList<>(0);
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.err.println("Array Index out of Bounds Error, you may have trailing whitespace in BOWLERS_DAT.");
            bowlerDB = new ArrayList<>(0);
        }

        bowlerPanel = new Widget.ScrollablePanel<>("Bowler Database", bowlerDB, 8, this);
    }

    AddPartyView(final ControlDeskView controlDesk, final int max) {
        this.controlDesk = controlDesk;
        maxSize = max;

        final ArrayList<String> empty = new ArrayList<>(0);
        empty.add("(Empty)");
        party = new ArrayList<>(0);
        partyPanel = new Widget.ScrollablePanel<>("Your Party", empty, 5, this);

        buildBowlerPanel();

        final Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put(ButtonNames.BTN_ADD_PATRON, this)
                .put(ButtonNames.BTN_REM_PATRON, this)
                .put(ButtonNames.BTN_NEW_PATRON, this)
                .put(ButtonNames.BTN_FINISHED, this);

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
            party.remove(selectedMember);
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
        final String source = ((AbstractButton) e.getSource()).getText();
        switch (source) {
            case ButtonNames.BTN_ADD_PATRON:
                addPatron();
                break;
            case ButtonNames.BTN_REM_PATRON:
                removePatron();
                break;
            case ButtonNames.BTN_NEW_PATRON:
                new NewPatronView(this);
                break;
            case ButtonNames.BTN_FINISHED:
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
        final Vector<String> res = BowlerFile.putBowlerIfDidntExist(nickName, newPatron.getFull(), newPatron.getEmail());
        if (res != null) {
            bowlerDB = new ArrayList<>(res);
            bowlerPanel.setListData(bowlerDB);
            party.add(nickName);
            partyPanel.setListData(party);
        } else {
            System.err.println("A Bowler with that name already exists.");
        }
    }

    public Iterable<String> getParty() {
        return (Iterable<String>) party.clone();
    }
}
