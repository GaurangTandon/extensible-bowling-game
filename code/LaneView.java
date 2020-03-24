/*
 *  constructs a prototype Lane View
 *
 */

import com.sun.jdi.connect.LaunchingConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * TODO:
 * LaneView probably does not need access to the entire scoresheet all the time. It can simply make do with the latest score
 * received via lane-event
 */
public class LaneView implements LaneObserver, ActionListener {
    private boolean initDone = true;

    private final JFrame frame;
    private final Container cpanel;
    private ArrayList<Bowler> bowlers;
    private JLabel[][] ballLabel;
    private JLabel[][] scoreLabel;
    private JButton maintenance;
    private final LaneInterface lane;

    public LaneView(final LaneInterface ln, final int laneNum) {
        lane = ln;
        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                frame.setVisible(false);
            }
        });

        cpanel.add(new JPanel());
    }

    void setVisible(final boolean state) {
        frame.setVisible(state);
    }

    private JPanel makeFrame(final Party party) {
        initDone = false;
        bowlers = party.getMembers();
        final int numBowlers = bowlers.size();

        final JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));

        // TODO: magic constant hell
        final JPanel[][] balls = new JPanel[numBowlers][23];
        ballLabel = new JLabel[numBowlers][23];
        final JPanel[][] scores = new JPanel[numBowlers][10];
        scoreLabel = new JLabel[numBowlers][10];
        final JPanel[][] ballGrid = new JPanel[numBowlers][10];
        final JPanel[] pins = new JPanel[numBowlers];

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
            final int j = 9;
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
                            bowlers.get(i).getNick()));
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


    private void receiveLaneEventGraphicSetup(final LaneEvent le) {
        while (!initDone) {
            Util.busyWait(1);
        }

        if (le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0) {
            cpanel.removeAll();
            cpanel.add(makeFrame(le.getParty()), "Center");

            // Button Panel
            final JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            new Insets(4, 4, 4, 4);

            maintenance = new JButton("Maintenance Call");
            final JPanel maintenancePanel = new JPanel();
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

        final int[] bowlerScores = (int[]) le.getScore().get(bowlers.get(k));

        final int bowlScore = bowlerScores[i];
        // TODO: what's this exactly?
        if (bowlScore == -1) {
            return;
        }

        final String textToSet = LaneUtil.getCharToShow(i, bowlerScores);
        final JLabel ballLabel = this.ballLabel[k][i];

        ballLabel.setText(textToSet);
    }

    public void receiveLaneEvent(final LaneEvent le) {
        if (le.isPartyEmpty()) {
            return;
        }
        final int numBowlers = le.getParty().getMembers().size();
        receiveLaneEventGraphicSetup(le);

        final int[][] lescores = le.getCumulScore();
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

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(maintenance)) {
            lane.pauseGame();
        }
    }

}
