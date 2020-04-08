import java.util.ArrayList;
import java.util.Collection;

class Frame {
    static final int FRAME_COUNT = 10;
    static final int LAST_FRAME = FRAME_COUNT - 1;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;


    static final int UNROLLED = -1;
    int[] rolls;
    int frameNumber;
    int rollCount;

    Frame(final int frameNumber) {
        this.frameNumber = frameNumber;
        reset();
    }

    Frame() {
        reset();
    }

    private void reset() {
        rolls = new int[2];
        rolls[0] = rolls[1] = UNROLLED;
        rollCount = 0;
    }

    static final int STRIKE = 11;
    static final int SPARE = 12;

    boolean isStrikePartial(final int roll) {
        return rolls[roll] == Pinsetter.PIN_COUNT;
    }

    boolean isSpare() {
        if (rollCount <= 1) return false;
        final int latestRoll = rolls[1];
        return rolls[0] + latestRoll == Pinsetter.PIN_COUNT && latestRoll > 0;
    }

    private boolean isStrike() {
        return rollCount == 1 && isStrikePartial(rollCount - 1);
    }

    // called to get display value for the latest roll
    void setDisplayValue(final int[] storage, final int startIndex) {
        if (isStrike()) {
            storage[startIndex] = STRIKE;
            return;
        }

        storage[startIndex] = rolls[0];
        storage[startIndex + 1] = isSpare() ? SPARE : rolls[1];
    }

    void roll(final int pinsDown) {
        rolls[rollCount] = pinsDown;
        rollCount++;
    }

    void addRolls(final Collection<Integer> rollList) {
        for (int i = 0; i < rollCount; i++) rollList.add(rolls[i]);
    }

    static int sumRolls(final ArrayList<Integer> rolls, final int startIndex, final int count) {
        int sum = 0;
        for (int i = startIndex, lim = Math.min(rolls.size(), startIndex + count); i < lim; i++)
            sum += rolls.get(i);
        return sum;
    }

    int getContributionToScore(final ArrayList<Integer> rolls, final int rollIndex) {
        if (rollCount == 0) return -1;

        return isStrike() || isSpare() ? sumRolls(rolls, rollIndex, 3) : sumRolls(rolls, rollIndex, rollCount);
    }

    boolean canRollAgain() {
        if (rollCount == rolls.length) return false;
        if (rollCount == 0) return true;

        return rolls[rollCount - 1] != Pinsetter.PIN_COUNT;
    }

    int getIncrement() {
        return canRollAgain() ? 0 : 1;
    }
}
