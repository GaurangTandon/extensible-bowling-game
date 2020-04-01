import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

public class LaneView implements ActionListener, Observer {
    private static final String BTN_MAINTENANCE = "Maintenance Call";

    private boolean initPending;
    private Widget.ButtonPanel buttonPanel;
    private List<BowlerScoreView> bsv;
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

    private Component makeFrame(final Iterable<String> bowlerNicks) {
        initPending = true;

        final Widget.ContainerPanel panel = new Widget.ContainerPanel(0, 1, "");
        bsv = new Vector<>();

        for (final String bowlerNick : bowlerNicks) {
            final BowlerScoreView bs = new BowlerScoreView(bowlerNick);
            bsv.add(bs);
            panel.put(bs.getPanel());
        }

        initPending = false;
        return panel.getPanel();
    }

    private Component getButtonPanel() {
        buttonPanel = new Widget.ButtonPanel("").put(BTN_MAINTENANCE, this);
        return buttonPanel.getPanel();
    }

    private void setupLaneGraphics(final LaneEvent le) {
        containerPanel.getPanel().removeAll();
        containerPanel.put(makeFrame(le.getBowlerNicks()), "Center");
        containerPanel.put(getButtonPanel(), "South");
        frame.pack();
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
            bsv.get(bowlerIdx).update(leCumulativeScore[bowlerIdx], le.getScore(bowlerIdx));
        }
    }

    private void waitInitToFinish() {
        while (initPending) {
            Util.busyWait(1);
        }
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(buttonPanel.get(BTN_MAINTENANCE))) {
            lane.pauseGame();
        }
    }
}
