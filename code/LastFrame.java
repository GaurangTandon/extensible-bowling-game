import java.util.ArrayList;

class LastFrame extends Frame {
    LastFrame() {
        rolls = new int[3];
        frameNumber = LaneScorer.LAST_FRAME;
        rolls[0] = rolls[1] = rolls[2] = UNROLLED;
    }

    boolean canRollAgain() {
        if (rollCount == rolls.length) return false;
        if (rollCount <= 1) return true;

        return rolls[rollCount - 1] + rolls[rollCount - 2] >= 10;
    }

    int getIncrement() {
        return 0;
    }

    int getContributionToScore(final ArrayList<Integer> rolls, final int rollIndex) {
        if (rollCount == 0) return -1;

        return sumRolls(rolls, rollIndex, rollCount);
    }

    // called to get display value for the latest roll
    int getDisplayValue() {
        final int latestRoll = rolls[rollCount - 1];

        if (isSpare()) {
            return SPARE;
        } else if (latestRoll == Pinsetter.PIN_COUNT) {
            return STRIKE;
        } else return latestRoll;
    }
}