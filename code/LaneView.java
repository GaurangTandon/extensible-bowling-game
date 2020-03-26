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
public class LaneView implements ActionListener, Observer {
    private boolean initPending;

    private final JFrame frame;
    private final Container cPanel;
    private JLabel[][] ballLabel;
    private JLabel[][] scoreLabel;
    private JButton maintenance;
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

        final int maxBalls = Lane.MAX_ROLLS + 2;
        ballLabel = new JLabel[numBowlers][maxBalls];
        scoreLabel = new JLabel[numBowlers][Lane.FRAME_COUNT];
        final JPanel[] balls = new JPanel[maxBalls]; // is reused per bowler

        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            final JPanel pin = makeOneBowlerCellsRow(maxBalls, balls, bowlerIdx, bowlerNicks.get(bowlerIdx));
            panel.add(pin);
        }

        initPending = false;
        return panel;
    }

    private JPanel makeOneBowlerCellsRow(final int maxBalls, final JPanel[] balls, final int bowlerIdx, final String bowlerNick) {
        final JPanel pin = new JPanel();
        pin.setBorder(BorderFactory.createTitledBorder(bowlerNick));
        pin.setLayout(new GridLayout(0, 10));

        for (int j = 0; j < maxBalls; j++) {
            ballLabel[bowlerIdx][j] = new JLabel(" ");
            balls[j] = new JPanel();
            balls[j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            balls[j].add(ballLabel[bowlerIdx][j]);
        }

        for (int frameIdx = 0; frameIdx < Lane.FRAME_COUNT; frameIdx++) {
            final JPanel ballPanel = new JPanel();
            ballPanel.setLayout(new GridLayout(0, 3));
            if (frameIdx != Lane.LAST_FRAME)
                ballPanel.add(new JLabel("  "), BorderLayout.EAST);
            ballPanel.add(balls[2 * frameIdx], BorderLayout.EAST);
            ballPanel.add(balls[2 * frameIdx + 1], BorderLayout.EAST);
            if (frameIdx == Lane.LAST_FRAME)
                ballPanel.add(balls[2 * frameIdx + 2]);

            scoreLabel[bowlerIdx][frameIdx] = new JLabel("  ", SwingConstants.CENTER);

            final JPanel score = new JPanel();
            score.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            score.setLayout(new GridLayout(0, 1));
            score.add(ballPanel, BorderLayout.EAST);
            score.add(scoreLabel[bowlerIdx][frameIdx], BorderLayout.SOUTH);
            pin.add(score, BorderLayout.EAST);
        }

        return pin;
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

    private void setBoxLabels(final int[] scores, final int bowlerIdx) {
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

    public final void receiveEvent(final Event lev) {
        final LaneEvent le = (LaneEvent) lev;
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

    private void setScoreLabels(final int[] bowlerScores, final int bowlerIdx) {
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
