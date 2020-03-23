import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

    // FIXME: Too many Class Properties - Status OK as they are due to Swing
    private final JPanel jp;

    private final JLabel curBowler;
    private final JLabel pinsDown;
    private final JButton viewLane;
    private final JButton viewPinSetter;
    private final JButton maintenance;

    private final PinSetterView psv;
    private final LaneView lv;
    private final Lane lane;

    private boolean laneShowing;
    private boolean psShowing;

    private JButton buttonInsertUtil(final JPanel buttonPanel, final String text) {
        final JButton btn = new JButton(text);
        Util.addButtonPanel(btn, buttonPanel, this);
        return btn;
    }

    LaneStatusView(final Lane lane, final int laneNum) {
        this.lane = lane;

        laneShowing = false;
        psShowing = false;

        psv = new PinSetterView(laneNum);
        final Pinsetter ps = lane.getPinsetter();
        ps.subscribe(psv);

        lv = new LaneView(lane, laneNum);
        lane.subscribe(lv);


        jp = new JPanel();
        jp.setLayout(new FlowLayout());
        final JLabel cLabel = new JLabel("Now Bowling: ");
        curBowler = new JLabel("(no one)");
        final JLabel pdLabel = new JLabel("Pins Down: ");
        pinsDown = new JLabel("0");

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        new Insets(4, 4, 4, 4);

        viewLane = buttonInsertUtil(buttonPanel, "View Lane");
        viewPinSetter = buttonInsertUtil(buttonPanel, "Pinsetter");
        maintenance = buttonInsertUtil(buttonPanel, "     ");

        maintenance.setBackground(Color.GREEN);
        viewLane.setEnabled(false);
        viewPinSetter.setEnabled(false);

        jp.add(cLabel);
        jp.add(curBowler);
        jp.add(pdLabel);
        jp.add(pinsDown);

        jp.add(buttonPanel);
    }

    JPanel showLane() {
        return jp;
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (lane.isPartyAssigned() && source.equals(viewPinSetter)) {
            psShowing = !psShowing;
            psv.setVisible(psShowing);
        }
        if (source.equals(viewLane) && lane.isPartyAssigned()) {
            lv.setVisible(laneShowing);
            laneShowing = !laneShowing;
        }
        if (source.equals(maintenance)) {
            if (lane.isPartyAssigned()) {
                lane.unPauseGame();
                maintenance.setBackground(Color.GREEN);
            }
        }
    }

    public void receiveLaneEvent(final LaneEvent le) {
        final Bowler bowler = le.getBowler();
        curBowler.setText(bowler.getNickName());
        if (le.isMechanicalProblem()) {
            maintenance.setBackground(Color.RED);
        }
        final boolean enabled = lane.isPartyAssigned();
        viewLane.setEnabled(enabled);
        viewPinSetter.setEnabled(enabled);
    }

    public void receivePinsetterEvent(final PinsetterEvent pe) {
        final int totalPinsDown = pe.totalPinsDown();
        pinsDown.setText(Integer.valueOf(totalPinsDown).toString());
    }
}
