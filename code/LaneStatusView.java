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

    private boolean laneShowing;
    private boolean psShowing;

    private static final String BTN_VIEW_LANE = "View Lane";
    private static final String BTN_VIEW_PINSETTER = "Pinsetter";
    private static final String BTN_PAUSE = "Pause";
    private static final String BTN_RESUME = "Resume";
    private static final String BTN_MAINTENANCE = "     ";
    private final String saveFile;

    LaneStatusView(final Lane lane, final int laneNum) {
        this.lane = lane;
        laneShowing = false;
        psShowing = false;
        saveFile = "Datastore/SAVED_" + laneNum + ".DAT";
        pinSetterView = new PinSetterView(laneNum);
        lane.subscribePinsetter(pinSetterView);

        laneView = new LaneView(lane, laneNum);
        lane.subscribe(laneView);

        buttonPanel = new Widget.ButtonPanel("")
                .put(BTN_VIEW_LANE, this)
                .put(BTN_VIEW_PINSETTER, this)
                .put(BTN_MAINTENANCE, this)
                .put(BTN_PAUSE, this)
                .put(BTN_RESUME, this);
        buttonPanel.get(BTN_MAINTENANCE).setBackground(Color.GREEN);
        buttonPanel.get(BTN_VIEW_LANE).setEnabled(false);
        buttonPanel.get(BTN_VIEW_PINSETTER).setEnabled(false);
        buttonPanel.get(BTN_PAUSE).setEnabled(false);
        buttonPanel.get(BTN_RESUME).setEnabled(false);

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
        final Object source = e.getSource();
        if (lane.isPartyAssigned()) {
            if (source.equals(buttonPanel.get(BTN_VIEW_PINSETTER))) {
                psShowing = !psShowing;
                pinSetterView.setVisible(psShowing);
            } else if (source.equals(buttonPanel.get(BTN_VIEW_LANE))) {
                laneShowing = !laneShowing;
                laneView.setVisible(laneShowing);
            } else if (source.equals(buttonPanel.get(BTN_MAINTENANCE))) {
                lane.unPauseGame();
                buttonPanel.get(BTN_MAINTENANCE).setBackground(Color.GREEN);
            }
        }
        if (source.equals(buttonPanel.get(BTN_RESUME))) {
            loadState();
            lane.setPauseState(false);
        } else if (source.equals(buttonPanel.get(BTN_PAUSE))) {
            lane.setPauseState(true);
            buttonPanel.get(BTN_RESUME).setEnabled(true);
            saveState();
        }
    }

    private void loadState() {
        try {
            final FileReader fileReader = new FileReader(saveFile);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            lane.loadState(bufferedReader);
            bufferedReader.close();
            fileReader.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void saveState() {
        try {
            final FileWriter fileWriter = new FileWriter(saveFile);
            lane.saveState(fileWriter);
            fileWriter.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public final void receiveEvent(final Event lev) {
        final LaneEvent le = (LaneEvent) lev;
        final String bowlerNick = le.getBowlerNick();
        currentBowler.setText(bowlerNick);

        if (le.isMechanicalProblem()) {
            buttonPanel.get(BTN_MAINTENANCE).setBackground(Color.RED);
        }

        final boolean isPartyAssigned = lane.isPartyAssigned();
        buttonPanel.get(BTN_VIEW_LANE).setEnabled(isPartyAssigned);
        buttonPanel.get(BTN_VIEW_PINSETTER).setEnabled(isPartyAssigned);
        buttonPanel.get(BTN_PAUSE).setEnabled(isPartyAssigned);
        buttonPanel.get(BTN_RESUME).setEnabled(!isPartyAssigned);

        final int totalPinsDown = le.getTotalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
