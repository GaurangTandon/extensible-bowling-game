/*  $Id$
 *
 *  Revisions:
 *    $Log: PinsetterEvent.java,v $
 *    Revision 1.2  2003/01/26 22:34:44  ???
 *    Total rewrite of lane and pinsetter for R2's observer model
 *    Added Lane/Pinsetter Observer
 *    Rewrite of scoring algorythm in lane
 *
 *    Revision 1.1  2003/01/19 21:04:24  ???
 *    created pinsetterevent and pinsetterobserver
 *
 */

class PinsetterEvent {

    private final boolean[] pinsStillStanding;
    private final boolean foulCommited;
    private final int throwNumber;
    private final int pinsDownThisThrow;

    /**
     * PinsetterEvent()
     * <p>
     * creates a new pinsetter event
     *
     * @pre none
     * @post the object has been initialized
     */
    PinsetterEvent(final boolean[] pinsStanding, final boolean foul, final int tn, final int pinsDownOnThisThrow) {
        pinsStillStanding = new boolean[Pinsetter.PIN_COUNT];

        System.arraycopy(pinsStanding, 0, pinsStillStanding, 0, Pinsetter.PIN_COUNT);

        foulCommited = foul;
        throwNumber = tn;
        pinsDownThisThrow = pinsDownOnThisThrow;
    }

    /**
     * pinKnockedDown()
     * <p>
     * check if a pin has been knocked down
     *
     * @return true if pin [i] has been knocked down
     */
    boolean pinKnockedDown(final int pinNumber) {
        return !pinsStillStanding[pinNumber];
    }

    /**
     * pinsDownOnThisThrow()
     *
     * @return the number of pins knocked down assosicated with this event
     */
    int pinsDownOnThisThrow() {
        return pinsDownThisThrow;
    }

    /**
     * totalPinsDown()
     *
     * @return the total number of pins down for pinsetter that generated the event
     */
    int totalPinsDown() {
        int count = 0;

        for (int i = 0; i < Pinsetter.PIN_COUNT; i++) {
            if (pinKnockedDown(i)) {
                count++;
            }
        }

        return count;
    }

    /**
     * isFoulCommited()
     *
     * @return true if a foul was commited on the lane, false otherwise
     */
    boolean isFoulCommited() {
        return foulCommited;
    }

    /**
     * getThrowNumber()
     *
     * @return current number of throws taken on this lane after last reset
     */
    int getThrowNumber() {
        return throwNumber;
    }
}

