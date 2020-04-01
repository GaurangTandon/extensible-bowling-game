import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private static final String BTN_MAINTENANCE = "     ";

    LaneStatusView(final Lane lane, final int laneNum) {
        this.lane = lane;
        laneShowing = false;
        psShowing = false;

        pinSetterView = new PinSetterView(laneNum);
        lane.subscribePinsetter(pinSetterView);

        laneView = new LaneView(lane, laneNum);
        lane.subscribe(laneView);

        buttonPanel = new Widget.ButtonPanel("")
                .put(BTN_VIEW_LANE, this)
                .put(BTN_VIEW_PINSETTER, this)
                .put(BTN_MAINTENANCE, this);
        buttonPanel.get(BTN_MAINTENANCE).setBackground(Color.GREEN);
        buttonPanel.get(BTN_VIEW_LANE).setEnabled(false);
        buttonPanel.get(BTN_VIEW_PINSETTER).setEnabled(false);

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
        if (lane.isPartyAssigned() && source.equals(buttonPanel.get(BTN_VIEW_PINSETTER))) {
            psShowing = !psShowing;
            pinSetterView.setVisible(psShowing);
        }
        if (source.equals(buttonPanel.get(BTN_VIEW_LANE)) && lane.isPartyAssigned()) {
            laneView.setVisible(laneShowing);
            laneShowing = !laneShowing;
        }
        if (source.equals(buttonPanel.get(BTN_MAINTENANCE))) {
            if (lane.isPartyAssigned()) {
                lane.unPauseGame();
                buttonPanel.get(BTN_MAINTENANCE).setBackground(Color.GREEN);
            }
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

        final int totalPinsDown = le.getTotalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
