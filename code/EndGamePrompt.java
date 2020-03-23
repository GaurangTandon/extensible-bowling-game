import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EndGamePrompt implements ActionListener {

    private final JFrame win;
    private final JButton yesButton;
    private final JButton noButton;

    private int result;

    EndGamePrompt(final String partyName) {
        result = 0;

        win = new JFrame("Another Game for " + partyName + "?");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(2, 1));

        // Label Panel
        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());

        final JLabel message = new JLabel("Party " + partyName
                + " has finished bowling.\nWould they like to bowl another game?");

        labelPanel.add(message);

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        new Insets(4, 4, 4, 4);

        yesButton = new JButton("Yes");
        final JPanel yesButtonPanel = new JPanel();
        yesButtonPanel.setLayout(new FlowLayout());
        yesButton.addActionListener(this);
        yesButtonPanel.add(yesButton);

        noButton = new JButton("No");
        final JPanel noButtonPanel = new JPanel();
        noButtonPanel.setLayout(new FlowLayout());
        noButton.addActionListener(this);
        noButtonPanel.add(noButton);

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Clean up main panel
        colPanel.add(labelPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);

    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

		if (source.equals(yesButton)) {
            result = 1;
        }
        if (source.equals(noButton)) {
            result = 2;
        }

    }

    int getResult() {
        while (result == 0) {
            Util.busyWait(10);
        }
        return result;
    }

    void destroy() {
        win.setVisible(false);
    }

}

