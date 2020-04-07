import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

class BowlerScorer {
    private int[] rolls;
    private int[] cumulativeScore;
    private int[] perFramePartRes;

    private int currFrame;
    private int partIndex;
    private int rollCount;
    private int score;

    BowlerScorer() {
        resetState();
    }

    private void resetState() {
        // extra 2 elements for safety from index out of bounds
        rolls = new int[LaneScorer.MAX_ROLLS + 2];
        cumulativeScore = new int[LaneScorer.FRAME_COUNT];
        resetCumulativeScores();
        perFramePartRes = new int[LaneScorer.MAX_ROLLS];
        for (int i = 0; i < LaneScorer.MAX_ROLLS; i++) perFramePartRes[i] = -1;

        currFrame = 0;
        rollCount = 0;
        partIndex = 0;
        score = 0;
    }

    void saveState(final FileWriter fw) throws IOException {
        for (int i = 0; i < rollCount; i++)
            fw.write(rolls[i] + " ");
        fw.write("\n");
    }

    void loadState(final BufferedReader fr) throws IOException {
        resetState();
        final String[] state = fr.readLine().split(" ");
        for (final String s : state) {
            roll(Integer.parseInt(s));
        }
    }

    static final int STRIKE = 11;
    static final int SPARE = 12;

    // @pre roll - is the start of a frame
    private int getPinsDownOnThisFrame(final int roll) {
        return rolls[roll] + rolls[roll + 1];
    }

    private boolean isStrike(final int roll) {
        return rolls[roll] == Pinsetter.PIN_COUNT;
    }

    // roll1 = first roll of the two spares
    // must be the first roll of the frame as well
    private boolean isSpare(final int roll1) {
        return getPinsDownOnThisFrame(roll1) == Pinsetter.PIN_COUNT && rolls[roll1 + 1] > 0;
    }

    // this roll is the second roll of a frame
    private boolean isSpareRoll2(final int roll2) {
        return rolls[roll2] + rolls[roll2 - 1] == Pinsetter.PIN_COUNT && rolls[roll2] > 0;
    }

    private int strikeBonus(final int roll) {
        return rolls[roll + 1] + rolls[roll + 2];
    }

    // roll1 = first roll of the two spares
    private int spareBonus(final int roll1) {
        return rolls[roll1 + 2];
    }

    private void resetCumulativeScores() {
        for (int frame = 0; frame < LaneScorer.FRAME_COUNT; frame++)
            cumulativeScore[frame] = -1;
    }

    private void resetCumulativeScores(final int currFrame) {
        for (int frame = 0; frame < currFrame; frame++)
            cumulativeScore[frame] = 0;
    }

    void updateCumulativeScores() {
        int roll = 0, frame = 0;

        resetCumulativeScores();
        resetCumulativeScores(currFrame);

        while (roll < rollCount) {
            final int scoreOnThisFrame;
            final int oldFrame = frame;

            if (frame == LaneScorer.LAST_FRAME) {
                scoreOnThisFrame = getPinsDownOnThisFrame(roll) + rolls[roll + 2];
                roll += 3;
            } else {
                if (isStrike(roll)) {
                    scoreOnThisFrame = Pinsetter.PIN_COUNT + strikeBonus(roll);
                    roll++;
                } else if (isSpare(roll)) {
                    scoreOnThisFrame = Pinsetter.PIN_COUNT + spareBonus(roll);
                    roll += 2;
                } else {
                    scoreOnThisFrame = getPinsDownOnThisFrame(roll);
                    roll += 2;
                }
                frame++;
            }

            cumulativeScore[oldFrame] += scoreOnThisFrame;
            if (oldFrame > 0)
                cumulativeScore[oldFrame] += cumulativeScore[oldFrame - 1];
        }

        // don't display score for this frame
        // if it hasn't started yet
        if (partIndex == 0) cumulativeScore[currFrame] = -1;

        score = cumulativeScore[currFrame];
    }

    int getScore() {
        return score;
    }

    private void updateFrameAndPartIndex() {
        boolean transgressFrame;
        transgressFrame = isStrike(rollCount);
        transgressFrame |= (partIndex == 1);
        transgressFrame &= currFrame < LaneScorer.LAST_FRAME;

        if (transgressFrame) {
            currFrame++;
            partIndex = 0;
        } else {
            partIndex++;
        }
    }

    /**
     * Add this roll to the array
     * and also update frame and part index accordingly
     *
     * @param pinsDown The number of pins hit in the strike
     */
    void roll(final int pinsDown) {
        rolls[rollCount] = pinsDown;

        // update the display
        final int updateIndex = 2 * currFrame + partIndex;

        if (shouldDisplaySpare())
            perFramePartRes[updateIndex] = SPARE;
        else if (shouldDisplayStrike())
            perFramePartRes[updateIndex] = STRIKE;
        else
            perFramePartRes[updateIndex] = rolls[rollCount];

        updateFrameAndPartIndex();

        rollCount++;
    }

    private boolean shouldDisplayStrike() {
        return (partIndex == 0 || currFrame == LaneScorer.LAST_FRAME) && isStrike(rollCount);
    }

    private boolean shouldDisplaySpare() {
        return partIndex == 1 && isSpareRoll2(rollCount);
    }

    int[] getCumulativeScore() {
        return cumulativeScore.clone();
    }

    /**
     * Used in LaneView to display the entire row of cells for a bowler
     *
     * @return integer array, result per frame part
     */
    int[] getByFramePartResult() {
        return perFramePartRes.clone();
    }

    /**
     * @param frameNumber the frame the Lane currently is in
     * @return true if this bowler can roll another ball in the same frame
     * This is true if
     * (1) frame is not the last, the previous roll didn't complete 10 pins, previous roll was the first in the frame
     * (2) frame is the last, previous roll had a strike or previous to previous roll was a strike (both must be from same frame)
     * (3) frame is last and previous roll made a spare
     */
    boolean canRollAgain(final int frameNumber) {
        // remember that these property values are AFTER the bowler made his last roll
        // so i am basically checking that the bowler is still in the same frame
        // as he was in the last roll

        final Boolean frameValidation = validateCurrFrame(frameNumber);
        if (frameValidation != null) return frameValidation;

        final Boolean partIndexValidation = validatePartIndex();
        if (partIndexValidation != null) return partIndexValidation;

        return true;
    }

    private Boolean validatePartIndex() {
        if (partIndex == 3) return false;
        // can only roll the third one in case of a spare or a strike
        // in any previous two rolls
        if (partIndex == 2)
            return getPinsDownOnThisFrame(rollCount - 2) >= 10;
        return null;
    }

    private Boolean validateCurrFrame(final int frameNumber) {
        if (currFrame != frameNumber)
            return false;

        if (currFrame < LaneScorer.LAST_FRAME)
            return true;

        return null;
    }

    int getCurrFrame() {
        return currFrame;
    }

    int getRollCount() {
        return rollCount;
    }
}
