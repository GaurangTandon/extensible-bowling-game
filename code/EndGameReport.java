import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


class EndGameReport implements ActionListener, ListSelectionListener {

    private final JFrame win;
    private final JButton printButton;
    private final JButton finished;
    private final Vector<String> retVal;

    private int result;

    private String selectedMember;

    public EndGameReport(final String partyName, final Party party) {

        result = 0;
        retVal = new Vector<>();
        win = new JFrame("End Game Report for " + partyName + "?");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 2));

        // Member Panel
        final JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Members"));

        final Vector<String> myVector = new Vector<>();
        for (final Bowler o : party.getMembers()) {
            myVector.add(o.getNick());
        }
        final JList<String> memberList = new JList<>(myVector);
        memberList.setFixedCellWidth(120);
        memberList.setVisibleRowCount(5);
        memberList.addListSelectionListener(this);
        final JScrollPane partyPane = new JScrollPane(memberList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        partyPanel.add(memberList);

        // Button Panel
        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        new Insets(4, 4, 4, 4);

        printButton = new JButton("Print Report");
        final JPanel printButtonPanel = new JPanel();
        printButtonPanel.setLayout(new FlowLayout());
        printButton.addActionListener(this);
        printButtonPanel.add(printButton);

        finished = new JButton("Finished");
        final JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        buttonPanel.add(printButton);
        buttonPanel.add(finished);

        // Clean up main panel
        colPanel.add(partyPanel);
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

        if (source.equals(printButton)) {
            retVal.add(selectedMember);
        } else if (source.equals(finished)) {
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
