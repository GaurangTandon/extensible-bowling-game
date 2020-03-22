public class BowlerScorer {
    private int[] rolls;

    private int[] cumulScore;
    private int[] finalScore;
    private int currFrame;
    private int partIndex;
    private int rollCount;
    private int score;

    BowlerScorer() {
        // extra 2 elements for safety from index out of bounds
        rolls = new int[Lane.MAX_ROLLS + 2];
        cumulScore = new int[Lane.FRAME_COUNT];
        finalScore = new int[Lane.FRAME_COUNT];

        currFrame = 0;
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
        return rolls[roll1 + 2];
    }

    void updateCumulScores() {
        int roll = 0, frame = 0;

        for (frame = 0; frame <= currFrame; frame++)
            cumulScore[frame] = 0;

        frame = 0;
        while (roll < rollCount) {
            int scoreOnThisFrame = 0;
            if (frame == Lane.FRAME_COUNT - 1) {
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
            frame++;
            frame = Math.min(frame, Lane.FRAME_COUNT - 1);
            roll++;
        }

        for (frame = 1; frame <= currFrame; frame++)
            cumulScore[frame] += cumulScore[frame - 1];

        score = cumulScore[currFrame];
    }

    void roll(int pinsDown) {
        rolls[rollCount] = pinsDown;

        if (isStrike(rollCount) || partIndex == 1) {
            currFrame++;
            // numFrames might exceed when you're in the last frame, but it should
            currFrame = Math.min(currFrame, Lane.FRAME_COUNT - 1);
            partIndex = 0;
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

    public static final int STRIKE = 11;
    public static final int SPARE = 12;

    /**
     * Used in LaneView to display the entire row of cells for a bowler
     *
     * @return
     */
    int[] getByFramePartResult() {
        int[] res = new int[Lane.MAX_ROLLS];
        for (int i = 0; i < Lane.MAX_ROLLS; i++) res[i] = -1;
        int frame = 0, frameIndex = 0;

        for (int roll = 0; roll < rollCount; roll++) {
            int index = frame * 2 + frameIndex;

            if (isStrike(roll)) {
                res[index] = STRIKE;
                if(frame < Lane.FRAME_COUNT - 1) {
                    frame++;
                    frameIndex = 0;
                }else{
                    frameIndex++;
                }
            } else if (isSpare(roll)) {
                res[index] = rolls[roll];
                res[index + 1] = SPARE;
                frame++;
                frameIndex = 0;
            } else {
                res[index] = rolls[roll];
                if (frame < Lane.FRAME_COUNT - 1 && frameIndex == 1) {
                    frameIndex = 0;
                    frame++;
                } else {
                    frameIndex++;
                }
            }

            frame = Math.min(frame, Lane.FRAME_COUNT - 1);
        }

        return res;
    }

    int getCurrFrame() {
        return currFrame;
    }
}
