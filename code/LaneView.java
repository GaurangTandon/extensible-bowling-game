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

/**
 * TODO: LaneView probably does not need access to the entire score sheet all the time.
 * It can simply make do with the latest score received via lane-event
 */
public class LaneView implements LaneObserver, ActionListener {
    private boolean initPending = false;

    private final JFrame frame;
    private final Container cPanel;
    private JLabel[][] ballLabel = null;
    private JLabel[][] scoreLabel = null;
    private JButton maintenance = null;
    private final LaneInterface lane;

    LaneView(final LaneInterface ln, final int laneNum) {
        lane = ln;
        frame = new JFrame("Lane " + laneNum + ":");
        cPanel = frame.getContentPane();
        cPanel.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                frame.setVisible(false);
            }
        });

        cPanel.add(new JPanel());
    }

    final void setVisible(final boolean state) {
        frame.setVisible(state);
    }

    private JPanel makeFrame(final Vector<String> bowlerNicks, final int numBowlers) {
        initPending = true;

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
                    BorderFactory.createTitledBorder(bowlerNicks.get(i)));
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

        initPending = false;
        return panel;
    }


    private void setupLaneGraphics(final LaneEvent le) {
        while (initPending) {
            Util.busyWait(1);
        }

        if (le.shouldSetupGraphics()) {
            cPanel.removeAll();
            cPanel.add(makeFrame(le.getBowlerNicks(), le.getPartySize()), "Center");

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
            cPanel.add(buttonPanel, "South");
            frame.pack();
        }
    }

    private static String getCharToShow(final int currScore) {
        final String textToSet;
        switch (currScore) {
            case BowlerScorer.STRIKE:
                textToSet = "X";
                break;
            case BowlerScorer.SPARE:
                textToSet = "/";
                break;
            default:
                textToSet = Integer.toString(currScore);
        }
        return textToSet;
    }

    private void setBoxLabels(final int[] scores, int bowlerIdx) {
        for (int i = 0; i < Lane.MAX_ROLLS; i++) {
            final int bowlScore = scores[i];

            // it means that the particular roll was skipped due to a strike
            if (bowlScore != -1) {
                final String textToSet = getCharToShow(bowlScore);
                final JLabel ballLabel = this.ballLabel[bowlerIdx][i];

                ballLabel.setText(textToSet);
            }
        }
    }

    public final void receiveLaneEvent(final LaneEvent le) {
        if (le.isPartyEmpty())
            return;

        final int numBowlers = le.getPartySize();
        setupLaneGraphics(le);

        final int[][] leCumulativeScore = le.getCumulativeScore();
        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            setScoreLabels(leCumulativeScore[bowlerIdx], bowlerIdx);
            setBoxLabels(le.getScore(bowlerIdx), bowlerIdx);
        }
    }

    private void setScoreLabels(int[] bowlerScores, int bowlerIdx) {
        for (int frameIdx = 0; frameIdx < Lane.FRAME_COUNT; frameIdx++) {
            if (bowlerScores[frameIdx] != -1)
                scoreLabel[bowlerIdx][frameIdx].setText(Integer.toString(bowlerScores[frameIdx]));
        }
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(maintenance)) {
            lane.pauseGame();
        }
    }
}
