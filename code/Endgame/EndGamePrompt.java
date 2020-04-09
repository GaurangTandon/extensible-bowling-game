package Endgame;

import Utilities.ButtonNames;
import Utilities.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGamePrompt implements ActionListener {

    private final Widget.WindowFrame win;
    private int result;

    public EndGamePrompt(final String partyName) {
        result = 0;

        final Widget.ContainerPanel labelPanel = new Widget.ContainerPanel("")
                .put(new JLabel("Bowlers.Party " + partyName
                        + " has finished bowling.\nWould they like to bowl another game?"));
        final Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel(1, 2, "")
                .put(ButtonNames.BTN_YES, this)
                .put(ButtonNames.BTN_NO, this);

        win = new Widget.WindowFrame(
                "Another Game for " + partyName + "?",
                new Widget.ContainerPanel(2, 1, "")
                        .put(labelPanel)
                        .put(buttonPanel)
        );
    }

    public final void actionPerformed(final ActionEvent e) {
        final String source = ((AbstractButton) e.getSource()).getText();

        if (source.equals(ButtonNames.BTN_YES)) {
            result = 1;
        } else if (source.equals(ButtonNames.BTN_NO)) {
            result = 2;
        }

    }

    public final int getResult() {
        while (result == 0) {
            Util.busyWait(10);
        }
        win.setVisible(false);
        return result;
    }

}

