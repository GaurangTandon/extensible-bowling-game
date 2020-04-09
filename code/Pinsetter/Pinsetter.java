package Pinsetter;

import Observer.Event;
import Observer.PinsetterEvent;
import Observer.Publisher;
import Utilities.Util;

import java.util.Random;

/**
 * Class to represent the pinsetter
 * It's only job is to allow the Lane.Lane to:
 * reset the pinsetter (at start of every frame, etc.)
 * be able to throw a ball into it
 * receive an event when ball throw was completed
 * It is the duty of the BowlerScorer:
 * to decide when to reset the pinsetter and keep scoring
 */
public class Pinsetter extends Publisher {
    private final Random rnd;
    public static final int PIN_COUNT = 10;

    private final boolean[] isPinStanding;
    /* 0-9

    6   7   8   9
      3   4   5
        2   1
          0

    */
    private boolean foul;
    private int throwNumber;
    private int pinsDownedOnThisThrow;

    public Pinsetter() {
        isPinStanding = new boolean[PIN_COUNT];
        rnd = new Random();
        resetState();
    }

    public Event createEvent() {
        return new PinsetterEvent(isPinStanding, foul, throwNumber, pinsDownedOnThisThrow);
    }

    public void ballThrown() {
        pinsDownedOnThisThrow = 0;
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
        publish();
        throwNumber++;
    }

    public void resetState() {
        foul = false;
        throwNumber = 1;
        resetPins();
        Util.busyWait(1000);
        pinsDownedOnThisThrow = -1;
    }

    private void resetPins() {
        for (int i = 0; i < PIN_COUNT; i++) {
            isPinStanding[i] = true;
        }
    }

    public final int totalPinsDown() {
        int count = 0;

        for (int i = 0; i < PIN_COUNT; i++) {
            if (!isPinStanding[i]) {
                count++;
            }
        }

        return count;
    }
}

