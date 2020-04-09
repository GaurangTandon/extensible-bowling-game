import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LaneStatusView implements ActionListener, Observer {

    private final Widget.ButtonPanel buttonPanel;
    private final Widget.ContainerPanel gamePanel;

    private final JLabel currentBowler;
    private final JLabel pinsDown;

    private final PinSetterView pinSetterView;
    private final LaneView laneView;
    private final Lane lane;

    private final int laneNumber;

    LaneStatusView(final Lane lane, final int laneNum) {
        this.lane = lane;
        laneNumber = laneNum;
        pinSetterView = new PinSetterView(laneNum);
        lane.subscribePinsetter(pinSetterView);

        laneView = new LaneView(lane, laneNum);
        lane.subscribe(laneView);

        buttonPanel = new Widget.ButtonPanel("")
                .put(ButtonNames.BTN_VIEW_LANE, this)
                .put(ButtonNames.BTN_VIEW_PINSETTER, this)
                .put(ButtonNames.BTN_MAINTENANCE_SPACE, this)
                .put(ButtonNames.BTN_PAUSE, this)
                .put(ButtonNames.BTN_RESUME, this);
        buttonPanel.get(ButtonNames.BTN_MAINTENANCE_SPACE).setBackground(Color.GREEN);
        buttonPanel.get(ButtonNames.BTN_VIEW_LANE).setEnabled(false);
        buttonPanel.get(ButtonNames.BTN_VIEW_PINSETTER).setEnabled(false);
        buttonPanel.get(ButtonNames.BTN_PAUSE).setEnabled(false);
        buttonPanel.get(ButtonNames.BTN_RESUME).setEnabled(true);

        currentBowler = new JLabel("(no one)");
        pinsDown = new JLabel("0");
        gamePanel = new Widget.ContainerPanel("")
                .put(new JLabel("Now Bowling: "))
                .put(currentBowler)
                .put(new JLabel("Pins Down: "))
                .put(pinsDown)
                .put(buttonPanel);
    }

    final JPanel showLane() {
        return gamePanel.getPanel();
    }

    public final void actionPerformed(final ActionEvent e) {
        final String source = ((AbstractButton) e.getSource()).getText();
        if (lane.isPartyAssigned()) {
            switch (source) {
                case ButtonNames.BTN_VIEW_PINSETTER:
                    pinSetterView.toggleVisible();
                    break;
                case ButtonNames.BTN_VIEW_LANE:
                    laneView.toggleVisible();
                    break;
                case ButtonNames.BTN_MAINTENANCE_SPACE:
                    lane.pauseGame(false);
                    buttonPanel.get(ButtonNames.BTN_MAINTENANCE_SPACE).setBackground(Color.GREEN);
                    break;
            }
        }
        if (source.equals(ButtonNames.BTN_RESUME)) {
            String fileName = Util.getFileName(gamePanel.getPanel());
            lane.loadState(fileName);
            lane.pauseManual(false);
        } else if (source.equals(ButtonNames.BTN_PAUSE)) {
            lane.pauseManual(true);
            buttonPanel.get(ButtonNames.BTN_RESUME).setEnabled(true);
            buttonPanel.get(ButtonNames.BTN_PAUSE).setEnabled(false);
            lane.saveState("Datastore/lane" + laneNumber + "_on_" + Util.getDateIdentifier() + ".dat");
        }
    }

    public final void receiveEvent(final Event lev) {
        final LaneEvent le = (LaneEvent) lev;
        final String bowlerNick = le.getBowlerNick();
        currentBowler.setText(bowlerNick);

        if (le.isMechanicalProblem()) {
            buttonPanel.get(ButtonNames.BTN_MAINTENANCE_SPACE).setBackground(Color.RED);
        }

        final boolean isPartyAssigned = lane.isPartyAssigned();
        buttonPanel.get(ButtonNames.BTN_VIEW_LANE).setEnabled(isPartyAssigned);
        buttonPanel.get(ButtonNames.BTN_VIEW_PINSETTER).setEnabled(isPartyAssigned);

        final boolean isPaused = lane.isPaused();
        buttonPanel.get(ButtonNames.BTN_PAUSE).setEnabled(!isPaused && isPartyAssigned);
        buttonPanel.get(ButtonNames.BTN_RESUME).setEnabled(isPaused || !isPartyAssigned);

        final int totalPinsDown = le.getTotalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
