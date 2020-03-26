/*
 * PinSetterView/.java
 *
 * Version:
 *   $Id$
 *
 * Revision:
 *   $Log$
 */


import javax.swing.*;
import java.awt.*;
import java.util.Vector;


/**
 * Constructs a prototype PinSetter GUI
 */
public class PinSetterView implements Observer {


    //This Vector will keep references to the pin labels to show
    //which ones have fallen.
    private final Vector<JLabel> pinVector = new Vector<>();
    private final JPanel secondRoll;

    /**
     * Constructs a Pin Setter GUI displaying which roll it is with
     * yellow boxes along the top (1 box for first roll, 2 boxes for second)
     * and displays the pins as numbers in this format:
     * <p>
     * 7   8   9   10
     * 4   5   6
     * 2   3
     * 1
     */


    private final JFrame frame;

    PinSetterView(final int laneNum) {
        frame = new JFrame("Lane " + laneNum + ":");

        final Container cPanel = frame.getContentPane();

        final JPanel top = new JPanel();

        final JPanel firstRoll = new JPanel();
        firstRoll.setBackground(Color.yellow);

        secondRoll = new JPanel();
        secondRoll.setBackground(Color.black);

        top.add(firstRoll, BorderLayout.WEST);
        top.add(secondRoll, BorderLayout.EAST);


        top.setBackground(Color.black);
        cPanel.add(top, BorderLayout.NORTH);

        final JPanel pins = addPins();
        cPanel.add(pins, BorderLayout.CENTER);

        frame.pack();
    }

    private void addDummyPanels(final JPanel pins, final int count) {
        for (int i = 1; i <= count; i++) {
            pins.add(new JPanel());
        }
    }

    private JPanel addPins() {
        final JPanel pins = new JPanel();

        pins.setLayout(new GridLayout(4, 7));

        final JPanel[] panels = getPanels();

        makeFourthRow(pins, panels);
        makeThirdRow(pins, panels);
        makeSecondRow(pins, panels);
        makeFirstRow(pins, panels, 1, 3);

        pins.setBackground(Color.black);
        pins.setForeground(Color.yellow);

        return pins;
    }

    private void makeFirstRow(final JPanel pins, final JPanel[] panels, final int pinNum, final int rightPad) {
        addDummyPanels(pins, 3);
        pins.add(panels[pinNum]);
        addDummyPanels(pins, rightPad);
    }

    private void makeSecondRow(final JPanel pins, final JPanel[] panels) {
        makeFirstRow(pins, panels, 2, 1);
        pins.add(panels[3]);
        addDummyPanels(pins, 2);
    }

    private void makeThirdRow(final JPanel pins, final JPanel[] panels) {
        for (int i = 4; i <= 6; i++) {
            addDummyPanels(pins, 1);
            pins.add(panels[i]);
        }
    }

    private void makeFourthRow(final JPanel pins, final JPanel[] panels) {
        for (int i = 7; i <= 10; i++) {
            pins.add(panels[i]);
            if (i != 10) addDummyPanels(pins, 1);
        }
    }

    private JPanel[] getPanels() {
        final JPanel[] panels = new JPanel[11];
        for (int pin = 1; pin <= 10; pin++) {
            final JPanel curr = new JPanel();
            final JLabel currL = new JLabel(Integer.toString(pin));
            curr.add(currL);
            pinVector.add(currL);
            panels[pin] = curr;
        }
        return panels;
    }

    /**
     * This method receives a pinsetter event.  The event is the current
     * state of the PinSetter and the method changes how the GUI looks
     * accordingly.  When pins are "knocked down" the corresponding label
     * is grayed out.  When it is the second roll, it is indicated by the
     * appearance of a second yellow box at the top.
     *
     * @param pe The state of the pinsetter is sent in this event.
     */


    public void receiveEvent(final Event pev) {
        final PinsetterEvent pe = (PinsetterEvent) pev;
        if (!(pe.isFoulCommitted())) {
            displayKnockedDownPins(pe);
        }

        if (pe.isFirstThrow()) {
            secondRoll.setBackground(Color.yellow);
        }

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        if (pinsDownOnThisThrow == -1) {
            for (int i = 0; i < 10; i++) {
                pinVector.get(i).setForeground(Color.black);
            }
            secondRoll.setBackground(Color.black);
        }
    }

    private void displayKnockedDownPins(final PinsetterEvent pe) {
        for (int c = 0; c < Pinsetter.PIN_COUNT; c++) {
            final boolean pinKnockedDown = pe.isPinKnockedDown(c);
            final JLabel tempPin = pinVector.get(c);
            if (pinKnockedDown) {
                tempPin.setForeground(Color.lightGray);
            }
        }
    }

    void setVisible(final boolean state) {
        frame.setVisible(state);
    }
}
