import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class ScorableBowler extends Bowler {
    private int[] cumulativeScore;
    private int[] perFramePartRes;
    private Frame[] frames;

    private int currFrame;
    private int score;

    ScorableBowler() {
        super("", "", "");
        reset();
    }

    void reset() {
        frames = new Frame[ScorableParty.FRAME_COUNT];
        for (int i = 0; i < ScorableParty.FRAME_COUNT - 1; i++)
            frames[i] = new Frame(i);
        frames[ScorableParty.FRAME_COUNT - 1] = new LastFrame();

        cumulativeScore = new int[ScorableParty.FRAME_COUNT];
        resetCumulativeScores();
        perFramePartRes = new int[ScorableParty.MAX_ROLLS];
        for (int i = 0; i < ScorableParty.MAX_ROLLS; i++) perFramePartRes[i] = -1;

        currFrame = 0;
        score = 0;
    }

    void saveState(final FileWriter fw) throws IOException {
        for (final Frame frame : frames) frame.saveState(fw);
    }

    // assumes the global LaneScorer reset has been called
    void loadState(final BufferedReader fr) throws IOException {
        for (final Frame frame : frames) frame.loadState(fr);
    }

    private void resetCumulativeScores() {
        for (int frame = 0; frame < ScorableParty.FRAME_COUNT; frame++)
            cumulativeScore[frame] = -1;
    }

    private ArrayList<Integer> getRolls() {
        final ArrayList<Integer> rollList = new ArrayList<>(0);
        for (final Frame frame : frames) {
            frame.addRolls(rollList);
        }

        return rollList;
    }

    void updateCumulativeScores() {
        final ArrayList<Integer> rolls = getRolls();
        int rollIndex = 0;

        resetCumulativeScores();

        for (int i = 0; i < currFrame; i++) {
            final int contrib = frames[i].getContributionToScore(rolls, rollIndex);
            if (contrib == -1) break;

            cumulativeScore[i] = contrib;
            if (i > 0) cumulativeScore[i] += cumulativeScore[i - 1];

            rollIndex += frames[i].rollCount;
        }

        score = cumulativeScore[currFrame];
    }

    int getScore() {
        return score;
    }

    /**
     * Add this roll to the array
     * and also update frame and part index accordingly
     *
     * @param pinsDown The number of pins hit in the strike
     */
    void roll(final int pinsDown) {
        final Frame frame = frames[currFrame];
        frame.roll(pinsDown, perFramePartRes);

        currFrame += frame.getIncrement();
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

    boolean canRollAgain(final int lanesFrameNumber) {
        if (currFrame != lanesFrameNumber) return false;
        return frames[currFrame].canRollAgain();
    }

    int getCurrFrame() {
        return currFrame;
    }
}
