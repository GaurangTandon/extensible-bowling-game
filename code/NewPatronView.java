/* AddPartyView.java
 *
 *  Version
 *  $Id$
 *
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

import Widget.TextFieldPanel;
import Widget.WindowFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for GUI components need to add a patron
 */
class NewPatronView implements ActionListener {

    private final TextFieldPanel patronPanel;
    private final WindowFrame window;
    private String nick, full, email;

    private final AddPartyView addParty;

    private static final String TXT_NICK_FIELD = "Nick Name";
    private static final String TXT_FULL_FIELD = "Full Name";
    private static final String TXT_EMAIL_FIELD = "E-Mail";

    NewPatronView(final AddPartyView v) {
        addParty = v;

        patronPanel = new Widget.TextFieldPanel(3, 1, "Your Info")
                .put(TXT_NICK_FIELD)
                .put(TXT_FULL_FIELD)
                .put(TXT_EMAIL_FIELD);
        Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put(ButtonNames.BTN_PATRON_FINISHED, this)
                .put(ButtonNames.BTN_PATRON_ABORT, this);

        window = new WindowFrame(
                "Add Patron",
                new Widget.ContainerPanel()
                        .put(patronPanel, "Center")
                        .put(buttonPanel, "East")
        );
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = ((JButton) e.getSource()).getText();
        final boolean aborted = source.equals(ButtonNames.BTN_PATRON_ABORT);
        final boolean finished = source.equals(ButtonNames.BTN_PATRON_FINISHED);

        if (finished) {
            nick = patronPanel.getText(TXT_NICK_FIELD);
            full = patronPanel.getText(TXT_FULL_FIELD);
            email = patronPanel.getText(TXT_EMAIL_FIELD);
            addParty.updateNewPatron(this);
        }

        if (aborted || finished) {
            window.setVisible(false);
        }
    }

    String getNickName() {
        return nick;
    }

    String getFull() {
        return full;
    }

    String getEmail() {
        return email;
    }
}
