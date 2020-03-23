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

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for GUI components need to add a patron
 */
class NewPatronView implements ActionListener {

    private final JFrame win;
    private final JButton abort;
    private final JButton finished;
    private final JTextField nickField;
    private final JTextField fullField;
    private final JTextField emailField;
    private String nick, full, email;

    private final AddPartyView addParty;

    NewPatronView(final AddPartyView v) {

        addParty = v;

        win = new JFrame("Add Patron");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Patron Panel
        final JPanel patronPanel = new JPanel();
        patronPanel.setLayout(new GridLayout(3, 1));
        patronPanel.setBorder(new TitledBorder("Your Info"));

        nickField = Util.addFieldPanel("Nick Name", patronPanel);
        fullField = Util.addFieldPanel("Full Name", patronPanel);
        emailField = Util.addFieldPanel("E-Mail", patronPanel);

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        finished = Util.addButtonPanel("Add Patron", buttonPanel, this);
        abort = Util.addButtonPanel("Abort", buttonPanel, this);

        // Clean up main panel
        colPanel.add(patronPanel, "Center");
        colPanel.add(buttonPanel, "East");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);

    }

    // TODO: this method is duplicated across several classes with similar characteristics, investigate
    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        final boolean aborted = source.equals(abort);
        final boolean finished = source.equals(this.finished);

        if (finished) {
            nick = nickField.getText();
            full = fullField.getText();
            email = emailField.getText();
            addParty.updateNewPatron(this);
        }

        if (aborted || finished) {
            win.setVisible(false);
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
