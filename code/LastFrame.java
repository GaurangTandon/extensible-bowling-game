import java.util.ArrayList;

class LastFrame extends Frame {
    LastFrame() {
        rolls = new int[3];
        frameNumber = Frame.LAST_FRAME;
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

    void setDisplayValue(final int[] storage, final int startIndex) {
        for (int roll = 0; roll < rollCount; roll++) {
            final int rollAmount = rolls[roll];
            final int assign = isStrikePartial(roll) ? STRIKE : rollAmount;
            storage[startIndex + roll] = assign;
        }

        if (isSpare()) storage[startIndex + 1] = SPARE;
    }
}