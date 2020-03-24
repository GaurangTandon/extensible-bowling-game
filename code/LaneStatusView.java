import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

    private final Widget.ButtonPanel buttonPanel;
    private final JPanel gamePanel;

    private final JLabel currentBowler;
    private final JLabel pinsDown;

    private final PinSetterView pinSetterView;
    private final LaneView laneView;
    private final Lane lane;

    private boolean laneShowing;
    private boolean psShowing;

    LaneStatusView(final Lane lane, final int laneNum) {
        this.lane = lane;

        laneShowing = false;
        psShowing = false;

        pinSetterView = new PinSetterView(laneNum);
        final Pinsetter ps = lane.getPinsetter();
        ps.subscribe(pinSetterView);

        laneView = new LaneView(lane, laneNum);
        lane.subscribe(laneView);


        gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());
        final JLabel cLabel = new JLabel("Now Bowling: ");
        currentBowler = new JLabel("(no one)");
        final JLabel pdLabel = new JLabel("Pins Down: ");
        pinsDown = new JLabel("0");

        // Button Panel
        buttonPanel = new Widget.ButtonPanel("")
                .put("viewLane", "View Lane", this)
                .put("viewPinSetter", "Pinsetter", this)
                .put("maintenance", "     ", this);

        buttonPanel.get("maintenance").setBackground(Color.GREEN);
        buttonPanel.get("viewLane").setEnabled(false);
        buttonPanel.get("viewPinSetter").setEnabled(false);

        gamePanel.add(cLabel);
        gamePanel.add(currentBowler);
        gamePanel.add(pdLabel);
        gamePanel.add(pinsDown);

        gamePanel.add(buttonPanel.get("_panel"));
    }

    final JPanel showLane() {
        return gamePanel;
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (lane.isPartyAssigned() && source.equals(buttonPanel.get("viewPinSetter"))) {
            psShowing = !psShowing;
            pinSetterView.setVisible(psShowing);
        }
        if (source.equals(buttonPanel.get("viewLane")) && lane.isPartyAssigned()) {
            laneView.setVisible(laneShowing);
            laneShowing = !laneShowing;
        }
        if (source.equals(buttonPanel.get("maintenance"))) {
            if (lane.isPartyAssigned()) {
                lane.unPauseGame();
                buttonPanel.get("maintenance").setBackground(Color.GREEN);
            }
        }
    }

    public final void receiveLaneEvent(final LaneEvent le) {
        final String bowlerNick = le.getBowlerNick();
        currentBowler.setText(bowlerNick);
        if (le.isMechanicalProblem()) {
            buttonPanel.get("maintenance").setBackground(Color.RED);
        }
        final boolean enabled = lane.isPartyAssigned();
        buttonPanel.get("viewLane").setEnabled(enabled);
        buttonPanel.get("viewPinSetter").setEnabled(enabled);
    }

    public final void receivePinsetterEvent(final PinsetterEvent pe) {
        final int totalPinsDown = pe.totalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
