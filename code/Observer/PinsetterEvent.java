package Observer;

import Pinsetter.Pinsetter;

public class PinsetterEvent implements Event {

    private final boolean[] pinsStillStanding;
    private final boolean foulCommitted;
    private final int throwNumber;
    private final int pinsDownThisThrow;
    private final boolean isReset;

    public PinsetterEvent(final boolean[] pinsStanding, final boolean foul, final int tn, final int pinsDownOnThisThrow) {
        pinsStillStanding = new boolean[Pinsetter.PIN_COUNT];

        System.arraycopy(pinsStanding, 0, pinsStillStanding, 0, Pinsetter.PIN_COUNT);

        foulCommitted = foul;
        throwNumber = tn;
        pinsDownThisThrow = pinsDownOnThisThrow;
        isReset = pinsDownOnThisThrow < 0;
    }

    public final boolean isPinKnockedDown(final int pinNumber) {
        return !pinsStillStanding[pinNumber];
    }

    public final int pinsDownOnThisThrow() {
        return pinsDownThisThrow;
    }

    public final boolean isFirstThrow() {
        return throwNumber == 1;
    }

    public final boolean isFoulCommitted() {
        return foulCommitted;
    }

    public final boolean isReset() {
        return isReset;
    }
}

