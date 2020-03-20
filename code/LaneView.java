/*
 *  constructs a prototype Lane View
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class LaneView implements LaneObserver, ActionListener {

    private boolean initDone = true;

    private final JFrame frame;
    private final Container cpanel;
    private final Lane lane;
    private Vector bowlers;
    private JLabel[][] ballLabel;
    private JLabel[][] scoreLabel;
    private JButton maintenance;

    public LaneView(Lane lane, int laneNum) {

        this.lane = lane;

        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });

        cpanel.add(new JPanel());

    }

    void show() {
        frame.setVisible(true);
    }

    void hide() {
        frame.setVisible(false);
    }

    void setVisible(boolean state) {
        frame.setVisible(state);
    }

    private JPanel makeFrame(Party party) {

        initDone = false;
        bowlers = party.getMembers();
        int numBowlers = bowlers.size();

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));

        // TODO: magic constant hell
        JPanel[][] balls = new JPanel[numBowlers][23];
        ballLabel = new JLabel[numBowlers][23];
        JPanel[][] scores = new JPanel[numBowlers][10];
        scoreLabel = new JLabel[numBowlers][10];
        JPanel[][] ballGrid = new JPanel[numBowlers][10];
        JPanel[] pins = new JPanel[numBowlers];

        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 23; j++) {
                ballLabel[i][j] = new JLabel(" ");
                balls[i][j] = new JPanel();
                balls[i][j].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                balls[i][j].add(ballLabel[i][j]);
            }
        }

        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 9; j++) {
                ballGrid[i][j] = new JPanel();
                ballGrid[i][j].setLayout(new GridLayout(0, 3));
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
            }
            int j = 9;
            ballGrid[i][j] = new JPanel();
            ballGrid[i][j].setLayout(new GridLayout(0, 3));
            ballGrid[i][j].add(balls[i][2 * j]);
            ballGrid[i][j].add(balls[i][2 * j + 1]);
            ballGrid[i][j].add(balls[i][2 * j + 2]);
        }

        for (int i = 0; i != numBowlers; i++) {
            pins[i] = new JPanel();
            pins[i].setBorder(
                    BorderFactory.createTitledBorder(
                            ((Bowler) bowlers.get(i)).getNick()));
            pins[i].setLayout(new GridLayout(0, 10));
            for (int k = 0; k != 10; k++) {
                scores[i][k] = new JPanel();
                scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
                scores[i][k].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                scores[i][k].setLayout(new GridLayout(0, 1));
                scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
                scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
                pins[i].add(scores[i][k], BorderLayout.EAST);
            }
            panel.add(pins[i]);
        }

        initDone = true;
        return panel;
    }


    private void receiveLaneEventGraphicSetup(LaneEvent le) {
        while (!initDone) {
            Util.busyWait(1);
        }

        if (le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0) {
            System.out.println("Making the frame.");
            cpanel.removeAll();
            cpanel.add(makeFrame(le.getParty()), "Center");

            // Button Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            new Insets(4, 4, 4, 4);

            maintenance = new JButton("Maintenance Call");
            JPanel maintenancePanel = new JPanel();
            maintenancePanel.setLayout(new FlowLayout());
            maintenance.addActionListener(this);
            maintenancePanel.add(maintenance);

            buttonPanel.add(maintenancePanel);
            cpanel.add(buttonPanel, "South");
            frame.pack();
        }
    }

    private void receiveLaneEventScoringSegment(final LaneEvent le, final int k, final int i) {
        assert i >= 0;

        final boolean evenRound = (i % 19) % 2 == 0, oddRound = i % 2 == 1;
        final int[] bowlerScore = (int[]) le.getScore().get(bowlers.get(k));

        if (bowlerScore[i] == -1) {
            return;
        }

        final JLabel ballLabel = this.ballLabel[k][i];
        if (bowlerScore[i] == 10 && evenRound)
            ballLabel.setText("X");
        else if (bowlerScore[i] + bowlerScore[i - 1] == 10 && oddRound)
            ballLabel.setText("/");
        else if (bowlerScore[i] == -2)
            ballLabel.setText("F");
        else
            ballLabel.setText(Integer.toString(bowlerScore[i]));

    }

    public void receiveLaneEvent(LaneEvent le) {
        if (!lane.isPartyAssigned()) {
            return;
        }
        int numBowlers = le.getParty().getMembers().size();
        receiveLaneEventGraphicSetup(le);

        int[][] lescores = le.getCumulScore();
        for (int k = 0; k < numBowlers; k++) {
            for (int i = 0; i <= le.getFrameNum() - 1; i++) {
                if (lescores[k][i] != 0)
                    scoreLabel[k][i].setText(
                            (Integer.valueOf(lescores[k][i])).toString());
            }
            for (int i = 0; i < 21; i++) {
                receiveLaneEventScoringSegment(le, k, i);
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(maintenance)) {
            lane.pauseGame();
        }
    }

}
