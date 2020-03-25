import Widget.ContainerPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


class EndGameReport implements ActionListener, ListSelectionListener {

    private final Widget.WindowFrame win;
    private final Widget.ButtonPanel buttonPanel;
    private final Vector<String> retVal;
    private int result;

    private String selectedMember;

    private static final String BTN_PRINT = "Print Report";
    private static final String BTN_FINISHED = "Finished";

    EndGameReport(final String partyName, final Party party) {
        result = 0;
        retVal = new Vector<>();

        final Vector<String> myVector = new Vector<>();
        for (final Bowler o : party.getMembers()) {
            myVector.add(o.getNickName());
        }
        // Member Panel
        final Widget.ScrollablePanel<String> partyPanel = new Widget.ScrollablePanel<>(
                "Party Members", myVector, 5, this);
        partyPanel.getPanel().add(partyPanel.getList()); // Can't understand why list is added again

        buttonPanel = new Widget.ButtonPanel(2, 1, "")
                .put(BTN_PRINT, this)
                .put(BTN_FINISHED, this);

        win = new Widget.WindowFrame(
                "End Game Report for " + partyName + "?",
                new ContainerPanel(1, 2, "")
                        .put(partyPanel)
                        .put(buttonPanel));
    }

    public static void main(final String[] args) {
        final Vector<Bowler> bowlers = new Vector<>();
        for (int i = 0; i < 4; i++) {
            bowlers.add(new Bowler("aaaaa", "aaaaa", "aaaaa"));
        }
        final Party party = new Party(bowlers);
        final String partyName = "wank";
        new EndGameReport(partyName, party);
    }

    public void valueChanged(final ListSelectionEvent e) {
        selectedMember =
                ((String) ((JList) e.getSource()).getSelectedValue());
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(buttonPanel.get(BTN_PRINT))) {
            retVal.add(selectedMember);
        } else if (source.equals(buttonPanel.get(BTN_FINISHED))) {
            win.setVisible(false);
            result = 1;
        }

    }

    Vector<String> getResult() {
        while (result == 0) {
            Util.busyWait(10);
        }
        return (Vector<String>) retVal.clone();
    }

    @SuppressWarnings("unused")
    public void destroy() {
        win.setVisible(false);
    }

}
