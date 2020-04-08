import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LaneView implements ActionListener, Observer {

    private List<BowlerScoreView> bsv;
    private final Widget.ContainerPanel containerPanel;
    private final JFrame frame;
    private final LaneInterface lane;
    private List<String> bowlerNicks;

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
        bowlerNicks = new ArrayList<>(0);
    }

    final void setVisible(final boolean state) {
        frame.setVisible(state);
    }

    private Component makeFrame() {
        final Widget.ContainerPanel panel = new Widget.ContainerPanel(0, 1, "");
        bsv = new Vector<>(0);

        for (final String bowlerNick : bowlerNicks) {
            final BowlerScoreView bs = new BowlerScoreView(bowlerNick);
            bsv.add(bs);
            panel.put(bs.getPanel());
        }

        return panel.getPanel();
    }

    private Component getButtonPanel() {
        final Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel("").put(ButtonNames.BTN_MAINTENANCE, this);
        return buttonPanel.getPanel();
    }

    private void setupLaneGraphics() {
        containerPanel
                .clear()
                .put(makeFrame(), "Center")
                .put(getButtonPanel(), "South");
        frame.pack();
    }

    public final void receiveEvent(final Event lev) {
        final LaneEvent le = (LaneEvent) lev;
        if (le.isPartyEmpty())
            return;

        final int numBowlers = le.getPartySize();

        final List<String> givenNicks = (List<String>) le.getBowlerNicks();
        if (bsv == null || !bowlerNicks.equals(givenNicks)) {
            bowlerNicks = givenNicks;
            setupLaneGraphics();
        }

        for (int bowlerIdx = 0; bowlerIdx < numBowlers; bowlerIdx++) {
            bsv.get(bowlerIdx).update(le.getCumulativeScore(bowlerIdx), le.getScore(bowlerIdx));
        }
    }

    public final void actionPerformed(final ActionEvent e) {
        final String source = ((AbstractButton) e.getSource()).getText();
        if (ButtonNames.BTN_MAINTENANCE.equals(source)) {
            lane.pauseGame(true);
        }
    }
}
