import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

class Frame {
    private static final String DELIMITER = ",";
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

    void saveState(final FileWriter fw) throws IOException {
        for (int i = 0; i < rolls.length; i++) {
            if (i > 0) fw.write(DELIMITER);
            fw.write(rolls[i]);
        }
        fw.write("\n");
    }

    void loadState(final BufferedReader br) throws IOException {
        final String[] state = br.readLine().split(DELIMITER);
        for (int i = 0; i < rolls.length; i++) {
            rolls[i] = Integer.parseInt(state[i]);
        }
    }

    static final int STRIKE = 11;
    static final int SPARE = 12;

    private boolean isStrike() {
        return rollCount == 1 && rolls[rollCount - 1] == Pinsetter.PIN_COUNT;
    }

    boolean isSpare() {
        final int latestRoll = rolls[rollCount - 1];
        return rollCount == 2 && rolls[rollCount - 2] + latestRoll == Pinsetter.PIN_COUNT && latestRoll > 0;
    }

    // called to get display value for the latest roll
    int getDisplayValue() {
        final int latestRoll = rolls[rollCount - 1];

        if (isStrike()) {
            return STRIKE;
        } else if (isSpare()) {
            return SPARE;
        } else return latestRoll;
    }

    void roll(final int pinsDown, final int[] perFramePerPartRes) {
        rolls[rollCount] = pinsDown;
        rollCount++;

        perFramePerPartRes[2 * frameNumber + rollCount - 1] = getDisplayValue();
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

    int getIncrement(){
        return canRollAgain() ? 0 : 1;
    }
}
