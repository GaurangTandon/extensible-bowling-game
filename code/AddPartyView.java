import Widget.ContainerPanel;
import Widget.WindowFrame;
import Widget.WindowView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

class AddPartyView extends WindowView implements ListSelectionListener {
    private static final String ERR_MEMBER_EXISTS = "Member already in Party";

    private final Widget.ScrollablePanel partyPanel;
    private Widget.ScrollablePanel bowlerPanel;

    private final ArrayList<String> party;
    private final ControlDeskView controlDesk;
    private String selectedNick, selectedMember;

    AddPartyView(final ControlDeskView controlDeskView) {
        party = new ArrayList<>(0);
        controlDesk = controlDeskView;
        partyPanel = drawScrollable("(Empty)", "Your Party", 5).attachListener(this);
        bowlerPanel = drawScrollable(BowlerFile.getBowlers(), "Bowler Database", 8).attachListener(this);
        String[] buttons = {ButtonNames.BTN_ADD_PATRON, ButtonNames.BTN_REM_PATRON,
                ButtonNames.BTN_NEW_PATRON, ButtonNames.BTN_FINISHED};
        win = new WindowFrame("Add Party", new ContainerPanel(1, 3, "")
                        .put(partyPanel)
                        .put(bowlerPanel)
                        .put(generateButtonPanel(buttons, "")));
    }

    private void addPatron() {
        if (selectedNick != null && party.size() < BowlingAlleyDriver.maxPatronsPerParty) {
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
        setVisible(false);
    }

    public void buttonHandler(String source) {
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
        } else if (source.equals(partyPanel.getList())) {
            selectedMember =
                    ((String) ((JList) source).getSelectedValue());
        }
    }

    void updateNewPatron(final NewPatronView newPatron) {
        final String nickName = newPatron.getNickName();
        final ArrayList<String> res = BowlerFile.putBowlerIfDidntExist(
                nickName, newPatron.getFull(), newPatron.getEmail());
        if (res != null) {
            bowlerPanel.setListData(new ArrayList<>(res));
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
