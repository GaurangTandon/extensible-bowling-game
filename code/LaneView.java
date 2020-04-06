import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

public class LaneView implements ActionListener, Observer {
    // TODO: to increase of cohesion, we could move this to a util class
    private static final String BTN_MAINTENANCE = "Maintenance Call";

    private Widget.ButtonPanel buttonPanel;
    private List<BowlerScoreView> bsv;
    private List<String> bowlerNicks;
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
        this.bowlerNicks = (List) bowlerNicks;
        final Widget.ContainerPanel panel = new Widget.ContainerPanel(0, 1, "");
        bsv = new Vector<>();

        for (final String bowlerNick : bowlerNicks) {
            final BowlerScoreView bs = new BowlerScoreView(bowlerNick);
            bsv.add(bs);
            panel.put(bs.getPanel());
        }

        return panel.getPanel();
    }

    private Component getButtonPanel() {
        buttonPanel = new Widget.ButtonPanel("").put(BTN_MAINTENANCE, this);
        return buttonPanel.getPanel();
    }

    private void setupLaneGraphics(final Iterable<String> bowlerNicks) {
        containerPanel
                .clear()
                .put(makeFrame(bowlerNicks), "Center")
                .put(getButtonPanel(), "South");
        frame.pack();
    }

    public final void receiveEvent(final Event lev) {
        final LaneEvent le = (LaneEvent) lev;
        if (le.isPartyEmpty())
            return;

        final int numBowlers = le.getPartySize();

        if (le.shouldSetupGraphics()) {
            setupLaneGraphics(le.getBowlerNicks());
        }

        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            bsv.get(bowlerIdx).update(le.getCumulativeScore(bowlerIdx), le.getScore(bowlerIdx));
        }
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source.equals(buttonPanel.get(BTN_MAINTENANCE))) {
            lane.pauseGame();
        }
    }
}
