import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
        buttonPanel.get(BTN_RESUME).setEnabled(true);

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
                laneView.setVisible(laneShowing);
                laneShowing = !laneShowing;
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
            FileReader f = new FileReader(saveFile);
            BufferedReader bufferedReader = new BufferedReader(f);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveState() {
        try {
            FileWriter f = new FileWriter(saveFile);
            int[][] scores = lane.getScoresMatrix();
            List<String> nicks = laneView.getBowlerNicks();
            int len = scores.length;
            for (int i = 0; i < len; i++) {
                f.write(nicks.get(i) + ": ");
                for (int score : scores[i]) {
                    f.write(score + " ");
                }
                f.write("\n");
            }
            f.close();
        } catch (IOException e) {
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
        final boolean enabled = lane.isPartyAssigned();
        buttonPanel.get(BTN_VIEW_LANE).setEnabled(enabled);
        buttonPanel.get(BTN_VIEW_PINSETTER).setEnabled(enabled);
        buttonPanel.get(BTN_PAUSE).setEnabled(enabled);
        buttonPanel.get(BTN_RESUME).setEnabled(!enabled);

        final int totalPinsDown = le.getTotalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
