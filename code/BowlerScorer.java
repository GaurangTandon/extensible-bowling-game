class BowlerScorer {
    private final int[] rolls;

    private final int[] cumulScore;
    private final int[] perFramepartRes;
    private int currFrame;
    private int partIndex;
    private int rollCount;
    private int score;

    BowlerScorer() {
        // extra 2 elements for safety from index out of bounds
        rolls = new int[Lane.MAX_ROLLS + 2];
        cumulScore = new int[Lane.FRAME_COUNT];
        resetCumulScores();
        perFramepartRes = new int[Lane.MAX_ROLLS];
        for (int i = 0; i < Lane.MAX_ROLLS; i++) perFramepartRes[i] = -1;

        currFrame = 0;
        rollCount = 0;
        partIndex = 0;
        score = 0;
    }

    static final int STRIKE = 11;
    static final int SPARE = 12;

    // @pre roll - is the start of a frame
    private int getPinsDownOnThisFrame(int roll) {
        return rolls[roll] + rolls[roll + 1];
    }

    private boolean isStrike(int roll) {
        return rolls[roll] == Pinsetter.PIN_COUNT;
    }

    // roll1 = first roll of the two spares
    // must be the first roll of the frame as well
    private boolean isSpare(int roll1) {
        return getPinsDownOnThisFrame(roll1) == Pinsetter.PIN_COUNT && rolls[roll1 + 1] > 0;
    }

    // this roll is the second roll of a frame
    private boolean isSpareRoll2(int roll2) {
        return rolls[roll2] + rolls[roll2 - 1] == Pinsetter.PIN_COUNT && rolls[roll2] > 0;
    }

    private int strikeBonus(int roll) {
        return rolls[roll + 1] + rolls[roll + 2];
    }

    // roll1 = first roll of the two spares
    private int spareBonus(int roll1) {
        return rolls[roll1 + 2];
    }

    private void resetCumulScores() {
        for (int frame = 0; frame < Lane.FRAME_COUNT; frame++)
            cumulScore[frame] = -1;
    }

    private void resetCumulScores(int currFrame) {
        for (int frame = 0; frame < currFrame; frame++)
            cumulScore[frame] = 0;
    }

    void updateCumulScores() {
        int roll = 0, frame = 0;

        resetCumulScores();
        resetCumulScores(currFrame);

        while (roll < rollCount) {
            int scoreOnThisFrame;

            if (frame == Lane.LAST_FRAME) {
                scoreOnThisFrame = getPinsDownOnThisFrame(roll) + rolls[roll + 2];
                roll += 2;
            } else {
                if (isStrike(roll)) {
                    scoreOnThisFrame = Pinsetter.PIN_COUNT + strikeBonus(roll);
                } else if (isSpare(roll)) {
                    scoreOnThisFrame = Pinsetter.PIN_COUNT + spareBonus(roll);
                    roll++;
                } else {
                    scoreOnThisFrame = getPinsDownOnThisFrame(roll);
                    roll++;
                }
            }
            // += so that case when frame=9 gets handled easily
            cumulScore[frame] += scoreOnThisFrame;
            frame = Math.min(frame + 1, Lane.LAST_FRAME);
            roll++;
        }

        for (frame = 1; frame <= currFrame; frame++)
            cumulScore[frame] += cumulScore[frame - 1];

        // don't display score for this frame
        // if it hasn't started yet
        if (partIndex == 0) cumulScore[currFrame] = -1;

        score = cumulScore[currFrame];
    }

    int getScore() {
        return score;
    }

    private void updateFrameAndPartIndex() {
        boolean transgressFrame;
        transgressFrame = isStrike(rollCount);
        transgressFrame |= (partIndex == 1);
        transgressFrame &= currFrame < Lane.LAST_FRAME;

        if (transgressFrame) {
            currFrame++;
            partIndex = 0;
        } else {
            partIndex++;
        }
    }

    /**
     * Add this roll to the array
     * and also update frame and partindex accordingly
     *
     * @param pinsDown The number of pins hit in the strike
     */
    void roll(int pinsDown) {
        rolls[rollCount] = pinsDown;

        // update the display
        int updateIndex = 2 * currFrame + partIndex;

        if (partIndex == 1 && isSpareRoll2(rollCount)) {
            perFramepartRes[updateIndex] = SPARE;
        } else if ((partIndex == 0 || currFrame == Lane.LAST_FRAME) && isStrike(rollCount))
            perFramepartRes[updateIndex] = STRIKE;
        else perFramepartRes[updateIndex] = rolls[rollCount];

        updateFrameAndPartIndex();

        rollCount++;
    }

    int[] getCumulScore() {
        return cumulScore.clone();
    }

    /**
     * Used in LaneView to display the entire row of cells for a bowler
     *
     * @return integer array, result per frame part
     */
    int[] getByFramePartResult() {
        return perFramepartRes.clone();
    }

    /**
     * @param frameNumber the frame the Lane currently is in
     * @return true if this bowler can roll another ball in the same frame
     * This is true if
     * (1) frame is not the last, the previous roll didn't complete 10 pins, previous roll was the first in the frame
     * (2) frame is the last, previous roll had a strike or previous to previous roll was a strike (both must be from same frame)
     * (3) frame is last and previous roll made a spare
     */
    boolean canRollAgain(int frameNumber) {
        // remember that these property values are AFTER the bowler made his last roll
        // so i am basically checking that the bowler is still in the same frame
        // as he was in the last roll

        if (currFrame != frameNumber)
            return false;

        if (currFrame < Lane.LAST_FRAME)
            return true;

        if (partIndex == 3) return false;
        // can only roll the third one in case of a spare or a strike
        // in any previous two rolls
        if (partIndex == 2)
            return (getPinsDownOnThisFrame(rollCount - 2) >= 10);

        return true;
    }

    int getCurrFrame() {
        return currFrame;
    }

    int getRollCount() {
        return rollCount;
    }
}
