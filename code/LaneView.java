import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class LaneView implements ActionListener, Observer {
    private final String BTN_MAINTENANCE = "Maintenance Call";

    private boolean initPending;
    private Widget.ButtonPanel buttonPanel;
    private Vector<Widget.GridPanel> playerLanes;
    private final Widget.ContainerPanel containerPanel;
    private final JFrame frame;
    private final LaneInterface lane;

    LaneView(final LaneInterface ln, final int laneNum) {
        lane = ln;
        frame = new JFrame("Lane " + laneNum + ":");
        containerPanel = new Widget.ContainerPanel((JPanel) frame.getContentPane());
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                frame.setVisible(false);
            }
        });
        containerPanel.put(new JPanel());
    }

    final void setVisible(final boolean state) {
        frame.setVisible(state);
    }

    private Component getPinPanel(final String bowlerNick) {
        final int maxBalls = Lane.MAX_ROLLS + 2;
        final Widget.GridPanel pin = new Widget.GridPanel(maxBalls, Lane.FRAME_COUNT, bowlerNick);
        playerLanes.add(pin);
        return pin.getPanel();
    }

    private Widget.ContainerPanel makeFrame(final Vector<String> bowlerNicks) {
        initPending = true;

        final Widget.ContainerPanel panel = new Widget.ContainerPanel(0, 1, "");
        playerLanes = new Vector<>();

        for (final String bowler : bowlerNicks)
            panel.put(getPinPanel(bowler));

        initPending = false;
        return panel;
    }

    private Component getButtonPanel() {
        buttonPanel = new Widget.ButtonPanel("").put(BTN_MAINTENANCE, this);
        return buttonPanel.getPanel();
    }

    private void setupLaneGraphics(final LaneEvent le) {
        containerPanel.getPanel().removeAll();
        containerPanel.put(makeFrame(le.getBowlerNicks()).getPanel(), "Center");
        containerPanel.put(getButtonPanel(), "South");
        frame.pack();
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

        waitInitToFinish();

        if (le.shouldSetupGraphics()) {
            setupLaneGraphics(le);
        }

        final int[][] leCumulativeScore = le.getCumulativeScore();
        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            setScoreLabels(leCumulativeScore[bowlerIdx], bowlerIdx);
            setBoxLabels(le.getScore(bowlerIdx), bowlerIdx);
        }
    }

    private void waitInitToFinish() {
        while (initPending) {
            Util.busyWait(1);
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
        if (source.equals(buttonPanel.get(BTN_MAINTENANCE))) {
            lane.pauseGame();
        }
    }
}
