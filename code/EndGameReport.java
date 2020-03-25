import Widget.ContainerPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


class EndGameReport implements ActionListener, ListSelectionListener {

    private final Widget.WindowFrame win;
    private final Widget.ButtonPanel buttonPanel;
    private final Vector<String> retVal;
    private int result;

    private String selectedMember = null;

    private static final String BTN_PRINT = "Print Report";
    private static final String BTN_FINISHED = "Finished";

    EndGameReport(final String partyName, final Vector<String> partyMemberNicks) {
        result = 0;
        retVal = new Vector<>(0);

        // Member Panel
        final Widget.ScrollablePanel<String> partyPanel = new Widget.ScrollablePanel<>(
                "Party Members", partyMemberNicks, 5, this);
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
