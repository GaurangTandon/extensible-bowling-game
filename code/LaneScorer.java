import java.util.HashMap;
import java.util.Vector;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    private int[][] finalScores;
    private int partySize;
    private Vector<Bowler> bowlers;
    private BowlerScorer[] bowlerScorers;

    /**
     * This resets the scores for the same party
     */
    void resetScores() {
        resetScores(bowlers, false);
    }

    /**
     * resetScores()
     * <p>
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    void resetScores(final Vector<Bowler> bowlers) {
        resetScores(bowlers, true);
    }

    private void resetScores(final Vector<Bowler> bowlers, final boolean resetFinalScores) {
        this.bowlers = bowlers;
        partySize = bowlers.size();
        if (resetFinalScores)
            finalScores = new int[partySize][128]; //Hardcoding a max of 128 games, bite me.
        bowlerScorers = new BowlerScorer[partySize];
        for (int bowler = 0; bowler < partySize; bowler++) {
            bowlerScorers[bowler] = new BowlerScorer();
       }
    }

    void roll(final int currBowlerIndex, final int pinsDowned) {

        final BowlerScorer bowlerScorer = bowlerScorers[currBowlerIndex];
        bowlerScorer.roll(pinsDowned);
        bowlerScorer.updateCumulScores();
    }

    boolean canRollAgain(final int currBowlerIndex, final int frameNumber){
        return bowlerScorers[currBowlerIndex].canRollAgain(frameNumber);
    }

    void setFinalScores(int bowlerIdx, int gameNum, int value) {
        finalScores[bowlerIdx][gameNum] = value;
    }

    int[] getFinalScores(int bowler) {
        return finalScores[bowler];
    }

    int[][] getCumulScores() {
        int[][] cumulScores = new int[partySize][Lane.FRAME_COUNT];
        for (int bowler = 0; bowler < partySize; bowler++)
            cumulScores[bowler] = bowlerScorers[bowler].getCumulScore();
        return cumulScores;
    }

    int getBowlersFinalScoreForCurrentGame(int bowler) {
        return bowlerScorers[bowler].getScore();
    }

    int[][] getByBowlerByFramePartResult() {
        // return a bowlerx21 matrix of scores
        int[][] result = new int[partySize][Lane.MAX_ROLLS];

        for (int bowler = 0; bowler < partySize; bowler++) {
            result[bowler] = bowlerScorers[bowler].getByFramePartResult();
        }
        return result;
    }
}
