public class BowlerScorer {
    private int[] rolls;

    private int[] cumulScore;
    private int[] finalScore;
    private int numOfFramesBowled;
    private int partIndex;
    private int rollCount;
    private int score;

    BowlerScorer() {
        rolls = new int[LaneUtil.MAX_ROLLS];
        cumulScore = new int[LaneUtil.FRAME_COUNT];
        finalScore = new int[LaneUtil.FRAME_COUNT];

        numOfFramesBowled = 0;
        partIndex = 0;
        score = 0;
    }

    // @pre roll - is the start of a frame
    int getPinsDownOnThisFrame(int roll) {
        return rolls[roll] + rolls[roll + 1];
    }

    boolean isStrike(int roll) {
        return rolls[roll] == Pinsetter.PIN_COUNT;
    }

    // roll1 = first roll of the two spares
    boolean isSpare(int roll1) {
        return getPinsDownOnThisFrame(roll1) == Pinsetter.PIN_COUNT;
    }

    int strikeBonus(int roll) {
        return rolls[roll + 1] + rolls[roll + 2];
    }

    // roll1 = first roll of the two spares
    int spareBonus(int roll1) {
        return rolls[roll1 + 1];
    }

    void updateCumulScores() {
        int frame = 0, roll = 0;

        // first set frame wise score
        while (roll < rollCount) {
            if (isStrike(roll)) {
                cumulScore[frame] = Pinsetter.PIN_COUNT + strikeBonus(roll);
            } else if (isSpare(roll)) {
                cumulScore[frame] = Pinsetter.PIN_COUNT + spareBonus(roll);
                roll++;
            } else if (partIndex == 0) {
                // basically, this condition ensures that this frame
                // for which we are getting the score has completed
                // if it has completed, then partIndex will be 0 (since it's pointing to the next frame)
                cumulScore[frame] = getPinsDownOnThisFrame(roll);
                roll++;
            }
            roll++;
            frame++;
        }

        // then make all scores cumulative
        for (frame = 1; frame < numOfFramesBowled; frame++) {
            cumulScore[frame] += cumulScore[frame - 1];
        }
        score = cumulScore[numOfFramesBowled - 1];
    }

    void roll(int pinsDown) {
        rolls[rollCount] = pinsDown;
        if (isStrike(rollCount) || partIndex == 1) {
            numOfFramesBowled++;
            // numFrames might exceed when you're in the last frame, but it should
            numOfFramesBowled = Math.min(numOfFramesBowled, LaneUtil.FRAME_COUNT);
        } else {
            partIndex++;
        }
        rollCount++;
    }

    int getScore() {
        return score;
    }

    int[] getCumulScore() {
        return cumulScore;
    }

    /**
     * Used in LaneView to display the entire row of cells for a bowler
     * @return
     */
    int[] getByFramePartResult() {
        int[] res = new int[LaneUtil.MAX_ROLLS];
        for (int i = 0; i < LaneUtil.MAX_ROLLS; i++) res[i] = -1;
        int framePart = 0;

        for (int roll = 0; roll < rollCount; roll++) {
            res[framePart] = rolls[roll];

            if(isStrike(roll)){
                framePart++;
            }

            framePart++;
        }

        return res;
    }
}
