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
public class PinSetterView implements PinsetterObserver {


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

    public PinSetterView(final int laneNum) {
        frame = new JFrame("Lane " + laneNum + ":");

        final Container cpanel = frame.getContentPane();

        final JPanel top = new JPanel();

        JPanel firstRoll = new JPanel();
        firstRoll.setBackground(Color.yellow);

        secondRoll = new JPanel();
        secondRoll.setBackground(Color.black);

        top.add(firstRoll, BorderLayout.WEST);
        top.add(secondRoll, BorderLayout.EAST);


        top.setBackground(Color.black);
        cpanel.add(top, BorderLayout.NORTH);

        JPanel pins = addPins();
        cpanel.add(pins, BorderLayout.CENTER);

        frame.pack();
    }

    public static void main(final String[] args) {
        new PinSetterView(1);
    }

    private void addDummyPanels(JPanel pins, int count) {
        while(count-- > 0){
            pins.add(new JPanel());
        }
    }

    private JPanel addPins() {
        final JPanel pins = new JPanel();

        pins.setLayout(new GridLayout(4, 7));

        //**********************Grid of the pins**************************

        final JPanel[] panels = new JPanel[11];
        for (int pin = 1; pin <= 10; pin++) {
            final JPanel curr = new JPanel();
            final JLabel currL = new JLabel(Integer.toString(pin));
            curr.add(currL);
            pinVector.add(currL);
            panels[pin] = curr;
        }
        //******************************Fourth Row**************

        for (int i = 7; i <= 10; i++) {
            pins.add(panels[i]);
            if (i != 10) addDummyPanels(pins, 1);
        }

        //*****************************Third Row***********

        for (int i = 4; i <= 6; i++) {
            addDummyPanels(pins, 1);
            pins.add(panels[i]);
        }

        //*****************************Second Row**************

        addDummyPanels(pins, 3);
        pins.add(panels[2]);
        addDummyPanels(pins, 1);
        pins.add(panels[3]);
        addDummyPanels(pins, 2);

        //******************************First Row*****************

        addDummyPanels(pins, 3);
        pins.add(panels[1]);
        addDummyPanels(pins, 3);

        pins.setBackground(Color.black);
        pins.setForeground(Color.yellow);

        return pins;
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


    public void receivePinsetterEvent(final PinsetterEvent pe) {
        if (!(pe.isFoulCommited())) {
            new JLabel();
            JLabel tempPin;

            for (int c = 0; c < 10; c++) {
                final boolean pinKnockedDown = pe.pinKnockedDown(c);
                tempPin = pinVector.get(c);
                if (pinKnockedDown) {
                    tempPin.setForeground(Color.lightGray);
                }
            }
        }
        if (pe.getThrowNumber() == 1) {
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

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

}
