import Widget.WindowFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LaneView implements ActionListener, Observer {
    private List<BowlerScoreView> bsv;
    private final Widget.ContainerPanel containerPanel;
    private final WindowFrame win;
    private final LaneWithPinsetter lane;
    private List<String> bowlerNicks;

    LaneView(final LaneWithPinsetter ln, final int laneNum) {
        lane = ln;
        win = new WindowFrame("Lane " + laneNum + ":");
        containerPanel = win.getContainer();
        containerPanel.put(new JPanel());
        bowlerNicks = new ArrayList<>(0);
    }

    final void setVisible(final boolean state) {
        win.setVisible(state);
    }

    void toggleVisible() {
        win.toggleVisible();
    }

    private void setupLaneGraphics() {
        final Widget.ButtonPanel buttonPanel = new Widget.ButtonPanel("")
                .put(ButtonNames.BTN_MAINTENANCE, this);
        final Widget.ContainerPanel panel = new Widget.ContainerPanel(0, 1, "");
        bsv = new ArrayList<>(0);

        for (final String bowlerNick : bowlerNicks) {
            final BowlerScoreView bs = new BowlerScoreView(bowlerNick);
            bsv.add(bs);
            panel.put(bs.getPanel());
        }

        containerPanel
                .clear()
                .put(panel.getPanel(), "Center")
                .put(buttonPanel.getPanel(), "South");
        win.pack();
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
