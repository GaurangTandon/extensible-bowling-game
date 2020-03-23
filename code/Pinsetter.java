/*
 * Pinsetter.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Pinsetter.java,v $
 *   Revision 1.21  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.20  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.19  2003/02/06 22:28:51  ???
 *   added delay
 *
 *   Revision 1.18  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.17  2003/02/05 23:56:07  ???
 *   *** empty log message ***
 *
 *   Revision 1.16  2003/02/05 23:51:09  ???
 *   Better random numbers.
 *
 *   Revision 1.15  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.14  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.13  2003/02/02 23:21:30  ???
 *   pinsetter should give better results
 *
 *   Revision 1.12  2003/02/02 23:20:28  ???
 *   pinsetter should give better results
 *
 *   Revision 1.11  2003/02/02 23:11:41  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.10  2003/02/01 19:14:42  ???
 *   Will now set the pins back up at times other than the 10th frame.
 *
 *   Revision 1.9  2003/01/30 21:44:25  ???
 *   Fixed speling of received in may places.
 *
 *   Revision 1.8  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.7  2003/01/19 21:55:24  ???
 *   updated pinsetter to new spec
 *
 *   Revision 1.6  2003/01/16 04:59:59  ???
 *   misc fixes across the board
 *
 *   Revision 1.5  2003/01/13 22:35:21  ???
 *   Scoring works.
 *
 *   Revision 1.3  2003/01/12 22:37:20  ???
 *   Wrote a better algorythm for knocking down pins
 *
 *
 */

import java.util.Random;
import java.util.Vector;

/**
 * Class to represent the pinsetter
 * It's only job is to allow the Lane to:
 * reset the pinsetter (at start of every frame, etc.)
 * be able to throw a ball into it
 * receive an event when ball throw was completed
 * It is the duty of the BowlerScorer:
 * to decide when to reset the pinsetter and keep scoring
 */
class Pinsetter {

    private final Random rnd;
    static final int PIN_COUNT = 10;
    private final double FOUL_PROBABILITY;
    private final Vector<PinsetterObserver> subscribers;

    private final boolean[] isPinStanding;
    /* 0-9

    6   7   8   9
      3   4   5
        2   1
          0

    */
    private boolean foul;
    private int throwNumber;

    /**
     * Pinsetter()
     * <p>
     * Constructs a new pinsetter
     *
     * @return Pinsetter object
     * @pre none
     * @post a new pinsetter is created
     */
    Pinsetter() {
        FOUL_PROBABILITY = 0.04;
        isPinStanding = new boolean[PIN_COUNT];
        rnd = new Random();
        subscribers = new Vector<>();
        resetState();
    }

    /**
     * sendEvent()
     * <p>
     * Sends pinsetter events to all subscribers
     *
     * @pre none
     * @post all subscribers have recieved pinsetter event with updated state
     */
    private void sendEvent(final int pinsDownedOnThisThrow) {
        for (final PinsetterObserver subscriber : subscribers) {
            subscriber.receivePinsetterEvent(
                    new PinsetterEvent(isPinStanding, foul, throwNumber, pinsDownedOnThisThrow));
        }
    }

    /**
     * ballThrown()
     * <p>
     * Called to simulate a ball thrown comming in contact with the pinsetter
     *
     * @pre none
     * @post pins may have been knocked down and the thrownumber has been incremented
     */
    void ballThrown() {
        int pinsDownedOnThisThrow = 0;
        foul = false;
        final double skill = rnd.nextDouble();

        for (int i = 0; i < PIN_COUNT; i++) {
            if (!isPinStanding[i]) continue;

            final double pinluck = rnd.nextDouble();
            foul = pinluck <= FOUL_PROBABILITY;

            isPinStanding[i] = ((skill + pinluck) / 2.0 * 1.2) <= .5;

            if (!isPinStanding[i]) {
                pinsDownedOnThisThrow++;
            }
        }

        Util.busyWait(500);
        sendEvent(pinsDownedOnThisThrow);
        throwNumber++;
    }

    /**
     * reset()
     * <p>
     * Reset the pinsetter to its complete state
     *
     * @pre none
     * @post pinsetters state is reset
     */
    void resetState() {
        foul = false;
        throwNumber = 1;
        resetPins();
        Util.busyWait(1000);
        sendEvent(-1);
    }

    /**
     * resetPins()
     * <p>
     * Reset the pins on the pinsetter
     *
     * @pre none
     * @post pins array is reset to all pins up
     */
    private void resetPins() {
        for (int i = 0; i < PIN_COUNT; i++) {
            isPinStanding[i] = true;
        }
    }

    /**
     * subscribe()
     * <p>
     * subscribe objects to send events to
     *
     * @pre none
     * @post the subscriber object will recieve events when their generated
     */
    void subscribe(final PinsetterObserver subscriber) {
        subscribers.add(subscriber);
    }

}

