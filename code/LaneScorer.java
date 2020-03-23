import java.util.Vector;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    private int[][] finalScores;
    private int partySize;
    private int bowlerIndex;
    private Vector<Bowler> bowlers;
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
    final void resetScores(final Vector<Bowler> bowlers) {
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

    final void roll(final int currBowlerIndex, final int pinsDowned) {
        bowlerIndex = currBowlerIndex;

        final BowlerScorer bowlerScorer = bowlerScorers[bowlerIndex];
        bowlerScorer.roll(pinsDowned);
        bowlerScorer.updateCumulScores();
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

    final int[][] getCumulScores() {
        final int[][] cumulScores = new int[partySize][Lane.FRAME_COUNT];
        for (int bowler = 0; bowler < partySize; bowler++)
            cumulScores[bowler] = bowlerScorers[bowler].getCumulScore();
        return cumulScores;
    }

    final int getBowlersFinalScoreForCurrentGame(final int bowler) {
        return bowlerScorers[bowler].getScore();
    }

    final int[][] getByBowlerByFramePartResult() {
        // return a bowlerx21 matrix of scores
        final int[][] result = new int[partySize][Lane.MAX_ROLLS];

        for (int bowler = 0; bowler < partySize; bowler++) {
            result[bowler] = bowlerScorers[bowler].getByFramePartResult();
        }
        return result;
    }

    final boolean isFirstRoll(final int bowlerIndex) {
        return bowlerScorers[bowlerIndex].getRollCount() == 1;
    }

    public final int getPartySize() {
        return partySize;
    }

    private void setPartySize(final int partySize) {
        this.partySize = partySize;
    }
}
