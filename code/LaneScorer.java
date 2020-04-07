import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    static final int LAST_FRAME = FRAME_COUNT - 1;

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

    void saveState(final FileWriter fw) throws IOException {
        for (final BowlerScorer bowlerScorer : bowlerScorers) {
            bowlerScorer.saveState(fw);
        }
    }

    void loadState(final BufferedReader fr) throws IOException {
        for (final BowlerScorer bowlerScorer : bowlerScorers) {
            bowlerScorer.loadState(fr);
        }
    }

    private void resetScores() {
        resetScores(bowlers, false);
    }

    final void nextBowler() {
        bowlerIndex++;
        if (bowlerIndex == partySize) {
            frameNumber++;
            bowlerIndex = 0;
            if (frameNumber == FRAME_COUNT) {
                finished = true;
                gameNumber++;
            }
        }
    }

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

    void setFinalScoresOnGameEnd() {
        if (!isLastFrame()) return;

        finalScores[bowlerIndex][gameNumber] = getBowlersFinalScoreForCurrentGame(bowlerIndex);
        final String finalScore = Integer.toString(finalScores[bowlerIndex][gameNumber]);

        try {
            final String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(bowlers.get(bowlerIndex).getNickName(), dateString, finalScore);
        } catch (final IOException e) {
            System.err.println("Exception in addScore. " + e);
        }
    }

    final int[] getFinalScores(final int bowler) {
        return finalScores[bowler];
    }

    final int[][] getCumulativeScores() {
        final int[][] cumulativeScores = new int[partySize][FRAME_COUNT];
        for (int bowler = 0; bowler < partySize; bowler++)
            cumulativeScores[bowler] = bowlerScorers[bowler].getCumulativeScore();
        return cumulativeScores;
    }

    private int getBowlersFinalScoreForCurrentGame(final int bowler) {
        return bowlerScorers[bowler].getScore();
    }

    final int[][] getByBowlerByFramePartResult() {
        // return a bowler x 21 matrix of scores
        final int[][] result = new int[partySize][MAX_ROLLS];

        for (int bowler = 0; bowler < partySize; bowler++) {
            result[bowler] = bowlerScorers[bowler].getByFramePartResult();
        }
        return result;
    }

    private boolean isBowlersFirstRoll() {
        return bowlerScorers[bowlerIndex].getRollCount() == 1;
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

    private boolean isLastFrame() {
        return frameNumber == LAST_FRAME;
    }

    boolean isHalted() {
        return halted;
    }

    int getCurrentBowler() {
        return bowlerIndex;
    }

    int getGameNumber() {
        return gameNumber;
    }

    boolean isFinished() {
        return finished;
    }
}
