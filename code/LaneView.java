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
    private JButton maintenance;
    private final LaneInterface lane;
    private Vector<Widget.GridPanel> playerLanes;

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

        playerLanes = new Vector<>();
        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            final Widget.GridPanel pin = new Widget.GridPanel(maxBalls, Lane.FRAME_COUNT, bowlerNicks.get(bowlerIdx));
            playerLanes.add(pin);
            panel.add(pin.getPanel());
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
                final JLabel ballLabel = playerLanes.get(bowlerIdx).getItemLabel(i);

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
                playerLanes.get(bowlerIdx).getBlockLabel(frameIdx).setText(Integer.toString(bowlerScores[frameIdx]));
        }
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(maintenance)) {
            lane.pauseGame();
        }
    }
}
