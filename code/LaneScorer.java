import java.util.Vector;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    private static final int MAX_GAMES = 128;
    private int[][] finalScores;
    private int partySize;
    private Vector<GeneralBowler> bowlers;
    private BowlerScorer[] bowlerScorers;

    /**
     * This resets the scores for the same party
     */
    final void resetScores() {
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
    final void resetScores(final Vector<GeneralBowler> bowlers) {
        resetScores(bowlers, true);
    }

    private void resetScores(final Vector<GeneralBowler> bowlers, final boolean resetFinalScores) {
        this.bowlers = bowlers;
        partySize = bowlers.size();

        if (resetFinalScores)
            finalScores = new int[partySize][MAX_GAMES];
        bowlerScorers = new BowlerScorer[partySize];

        for (int bowler = 0; bowler < partySize; bowler++) {
            bowlerScorers[bowler] = new BowlerScorer();
        }
    }

    final void roll(final int currBowlerIndex, final int pinsDowned) {
        final BowlerScorer bowlerScorer = bowlerScorers[currBowlerIndex];
        bowlerScorer.roll(pinsDowned);
        bowlerScorer.updateCumulativeScores();
    }

    final boolean canRollAgain(final int currBowlerIndex, final int frameNumber) {
        return bowlerScorers[currBowlerIndex].canRollAgain(frameNumber);
    }

    final void setFinalScores(final int bowlerIdx, final int gameNum, final int value) {
        finalScores[bowlerIdx][gameNum] = value;
    }

    final int[] getFinalScores(final int bowler) {
        return finalScores[bowler];
    }

    final int[][] getCumulativeScores() {
        final int[][] cumulativeScores = new int[partySize][Lane.FRAME_COUNT];
        for (int bowler = 0; bowler < partySize; bowler++)
            cumulativeScores[bowler] = bowlerScorers[bowler].getCumulativeScore();
        return cumulativeScores;
    }

    final int getBowlersFinalScoreForCurrentGame(final int bowler) {
        return bowlerScorers[bowler].getScore();
    }

    final int[][] getByBowlerByFramePartResult() {
        // return a bowler x 21 matrix of scores
        final int[][] result = new int[partySize][Lane.MAX_ROLLS];

        for (int bowler = 0; bowler < partySize; bowler++) {
            result[bowler] = bowlerScorers[bowler].getByFramePartResult();
        }
        return result;
    }

    final boolean isFirstRoll(final int bowlerIndex) {
        return bowlerScorers[bowlerIndex].getRollCount() == 1;
    }
}
