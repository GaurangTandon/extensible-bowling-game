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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for GUI components need to add a patron
 */
class NewPatronView implements ActionListener {

    private final Widget.ButtonPanel buttonPanel;
    private final TextFieldPanel patronPanel;
    private final Widget.WindowPanel window;
    private String nick, full, email;

    private final AddPartyView addParty;

    private static final String TXT_NICK_FIELD = "Nick Name";
    private static final String TXT_FULL_FIELD = "Full Name";
    private static final String TXT_EMAIL_FIELD = "E-Mail";
    private static final String BTN_FINISHED = "Add Patron";
    private static final String BTN_ABORT = "Abort";

    NewPatronView(final AddPartyView v) {
        addParty = v;

        patronPanel = new Widget.TextFieldPanel(3, 1, "Your Info")
                .put(TXT_NICK_FIELD)
                .put(TXT_FULL_FIELD)
                .put(TXT_EMAIL_FIELD);
        buttonPanel = new Widget.ButtonPanel(4, 1, "")
                .put(BTN_FINISHED, this)
                .put(BTN_ABORT, this);

        window = new Widget.WindowPanel(
                "Add Patron",
                new Widget.ContainerPanel()
                        .put(patronPanel)
                        .put(buttonPanel)
        );
    }

    // TODO: this method is duplicated across several classes with similar characteristics, investigate
    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        final boolean aborted = source.equals(buttonPanel.get(BTN_ABORT));
        final boolean finished = source.equals(buttonPanel.get(BTN_FINISHED));

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
