package Endgame;

import Bowlers.BowlerInfo;
import Bowlers.ScorableBowler;
import Bowlers.ScorableParty;
import Utilities.ButtonNames;
import Utilities.ScoreReport;
import Utilities.Util;
import Widget.ContainerPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class EndGameReport implements ActionListener, ListSelectionListener {

    private final Widget.WindowFrame win;
    private final List<String> retVal;
    private int result;

    private String selectedMember;

    public EndGameReport(final String partyName, final ArrayList<String> partyMemberNicks) {
        result = 0;
        retVal = new Vector<>(0);

        // Member Panel
        final Widget.ScrollablePanel partyPanel = new Widget.ScrollablePanel(
                "Bowlers.Party Members", partyMemberNicks, 5, this);
        partyPanel.getPanel().add(partyPanel.getList()); // Can't understand why list is added again

        final Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel(2, 1, "")
                .put(ButtonNames.BTN_PRINT, this)
                .put(ButtonNames.BTN_FINISHED, this);

        win = new Widget.WindowFrame(
                "End Game Report for " + partyName + "?",
                new ContainerPanel(1, 2, "")
                        .put(partyPanel)
                        .put(buttonPanel));

    }

    public void printer(final ScorableParty scorer) {
        while (result == 0) {
            Util.busyWait(10);
        }

        final Iterable<ScorableBowler> partyMembers = scorer.getMembers();
        final int gameNumber = scorer.getGameNumber();

        int myIndex = 0;

        for (final BowlerInfo bowler : partyMembers) {
            final ScoreReport sr = new ScoreReport(bowler, scorer.getFinalScores(myIndex), gameNumber);
            myIndex++;

            final String nick = bowler.getNickName();
            if (shouldPrint(nick)) {
                System.out.println("Printing " + nick);
                sr.sendPrintout();
            }
        }
    }

    public void valueChanged(final ListSelectionEvent e) {
        final JList source = (JList) e.getSource();
        selectedMember = (String) source.getSelectedValue();
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = ((AbstractButton) e.getSource()).getText();

        if (source.equals(ButtonNames.BTN_PRINT)) {
            retVal.add(selectedMember);
        } else if (source.equals(ButtonNames.BTN_FINISHED)) {
            win.setVisible(false);
            result = 1;
        }
    }

    private boolean shouldPrint(final String bowlerNick) {
        return Util.containsString(retVal, bowlerNick);
    }
}
