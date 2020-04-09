package Lane;

import Observer.Publisher;
import Observer.Observer;
import Pinsetter.Pinsetter;
import Pinsetter.PinSetterView;

abstract class LaneWithPinsetter extends Publisher implements Observer {
    private final Pinsetter pinsetter;
    abstract void pauseGame(boolean state);

    LaneWithPinsetter() {
        pinsetter = new Pinsetter();
        pinsetter.subscribe(this);
    }

    final void subscribePinsetter(final PinSetterView psv) {
        pinsetter.subscribe(psv);
    }

    final int getPinsDown(){
        return pinsetter == null ? 0 : pinsetter.totalPinsDown();
    }

    final void rollBall(){
        pinsetter.ballThrown();
    }

    final void resetPinsetter(){
        pinsetter.resetState();
    }
}
