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
class Pinsetter implements GeneralPinsetter {
    private final Random rnd;
    static final int PIN_COUNT = 10;
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

    Pinsetter() {
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
     * @post all subscribers have received pinsetter event with updated state
     */
    private void sendEvent(final int pinsDownedOnThisThrow) {
        final PinsetterEvent pe = new PinsetterEvent(isPinStanding, foul, throwNumber, pinsDownedOnThisThrow);
        for (final PinsetterObserver subscriber : subscribers) {
            subscriber.receivePinsetterEvent(pe);
        }
    }

    /**
     * ballThrown()
     * <p>
     * Called to simulate a ball thrown coming in contact with the pinsetter
     *
     * @pre none
     * @post pins may have been knocked down and the throwNumber has been incremented
     */
    public void ballThrown() {
        int pinsDownedOnThisThrow = 0;
        foul = false;
        final double skill = rnd.nextDouble();

        for (int i = 0; i < PIN_COUNT; i++) {
            if (!isPinStanding[i]) continue;

            final double pinLuck = rnd.nextDouble();
            final double FOUL_PROBABILITY = 0.04;
            foul = pinLuck <= FOUL_PROBABILITY;

            isPinStanding[i] = ((skill + pinLuck) / 2.0 * 1.2) <= .5;

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
    public void resetState() {
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
     * @post the subscriber object will receive events when their generated
     */
    public void subscribe(final PinsetterObserver subscriber) {
        subscribers.add(subscriber);
    }
}

