class PinsetterEvent implements Event {

    private final boolean[] pinsStillStanding;
    private final boolean foulCommitted;
    private final int throwNumber;
    private final int pinsDownThisThrow;
    private final boolean isReset;

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

        foulCommitted = foul;
        throwNumber = tn;
        pinsDownThisThrow = pinsDownOnThisThrow;
        isReset = pinsDownOnThisThrow < 0;
    }

    /**
     * isPinKnockedDown()
     * <p>
     * check if a pin has been knocked down
     *
     * @return true if pin [i] has been knocked down
     */
    final boolean isPinKnockedDown(final int pinNumber) {
        return !pinsStillStanding[pinNumber];
    }

    /**
     * pinsDownOnThisThrow()
     *
     * @return the number of pins knocked down associated with this event
     */
    final int pinsDownOnThisThrow() {
        return pinsDownThisThrow;
    }

    final boolean isFirstThrow() {
        return throwNumber == 1;
    }

    /**
     * isFoulCommitted()
     *
     * @return true if a foul was committed on the lane, false otherwise
     */
    final boolean isFoulCommitted() {
        return foulCommitted;
    }

    final boolean isReset() {
        return isReset;
    }
}

