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
    private boolean halted;
    private boolean finished;
    private int bowlerIndex;
    private int frameNumber;
    private int gameNumber;

    /**
     * This resets the scores for the same party
     */
    private void resetScores() {
        resetScores(bowlers, false);
    }

    final void nextBowler() {
        bowlerIndex++;
        if (bowlerIndex == partySize) {
            frameNumber++;
            bowlerIndex = 0;
            if (frameNumber == Lane.FRAME_COUNT) {
                finished = true;
                gameNumber++;
            }
        }
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

        if (resetFinalScores) {
            finalScores = new int[partySize][MAX_GAMES];
            gameNumber = 0;
        }
        bowlerScorers = new BowlerScorer[partySize];

        for (int bowler = 0; bowler < partySize; bowler++) {
            bowlerScorers[bowler] = new BowlerScorer();
        }

        bowlerIndex = 0;
        frameNumber = 0;
        finished = false;
        halted = false;
    }

    final void roll(final int pinsDowned) {
        final BowlerScorer bowlerScorer = bowlerScorers[bowlerIndex];
        bowlerScorer.roll(pinsDowned);
        bowlerScorer.updateCumulativeScores();
    }

    final boolean canRollAgain() {
        return bowlerScorers[bowlerIndex].canRollAgain(frameNumber);
    }

    final int finalizeCurrentBowlersGameScore() {
        finalScores[bowlerIndex][gameNumber] = getBowlersFinalScoreForCurrentGame(bowlerIndex);
        return finalScores[bowlerIndex][gameNumber];
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

    private int getBowlersFinalScoreForCurrentGame(final int bowler) {
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

    private boolean isBowlersFirstRoll() {
        return bowlerScorers[bowlerIndex].getRollCount() == 1;
    }

    final boolean shouldResetGraphics() {
        return bowlerIndex == 0 && isBowlersFirstRoll();
    }

    final void onGameFinish() {
        resetScores();
    }

    final void pause() {
        halted = true;
    }

    final void unpause() {
        halted = false;
    }

    final boolean isLastFrame() {
        return frameNumber == Lane.LAST_FRAME;
    }

    public boolean isHalted() {
        return halted;
    }

    public int getCurrentBowler() {
        return bowlerIndex;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public boolean isFinished() {
        return finished;
    }
}
