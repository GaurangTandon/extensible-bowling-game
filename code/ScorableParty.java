import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class ScorableParty extends Party {
    static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    static final int LAST_FRAME = FRAME_COUNT - 1;

    private static final int MAX_GAMES = 128;
    private int[][] finalScores;
    private int partySize;
    private final ArrayList<ScorableBowler> bowlers;
    private boolean halted;
    private boolean finished;
    private int bowlerIndex;
    private int frameNumber;
    private int gameNumber;

    ScorableParty() {
        bowlers = new ArrayList<>(0);
    }

    void addBowler(final ScorableBowler bowler) {
        bowlers.add(bowler);

        if (bowlers.size() == 1) {
            name += bowler.getNickName() + "'s Party";
        }
    }

    void saveState(final FileWriter fw) throws IOException {
        super.saveState(fw);
        for (final ScorableBowler bowlerScorer : bowlers) {
            bowlerScorer.saveState(fw);
        }
    }

    void loadState(final BufferedReader fr) throws IOException {
        super.loadState(fr);
        for (final ScorableBowler bowlerScorer : bowlers) {
            bowlerScorer.loadState(fr);
        }
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

    final void resetScoresHard() {
        resetScores(true);
    }

    private void resetScores() {
        resetScores(false);
    }

    private void resetScores(final boolean resetFinalScores) {
        partySize = bowlers.size();

        if (resetFinalScores) {
            finalScores = new int[partySize][MAX_GAMES];
            gameNumber = 0;
        }

        for (final ScorableBowler scb : bowlers) {
            scb.reset();
        }

        bowlerIndex = 0;
        frameNumber = 0;
        finished = false;
        halted = false;
    }

    final void roll(final int pinsDowned) {
        final ScorableBowler bowlerScorer = bowlers.get(bowlerIndex);
        bowlerScorer.roll(pinsDowned);
        bowlerScorer.updateCumulativeScores();
    }

    final boolean canRollAgain() {
        return bowlers.get(bowlerIndex).canRollAgain(frameNumber);
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
            cumulativeScores[bowler] = bowlers.get(bowler).getCumulativeScore();
        return cumulativeScores;
    }

    private int getBowlersFinalScoreForCurrentGame(final int bowler) {
        return bowlers.get(bowler).getScore();
    }

    final int[][] getByBowlerByFramePartResult() {
        // return a bowler x 21 matrix of scores
        final int[][] result = new int[partySize][MAX_ROLLS];

        for (int bowler = 0; bowler < partySize; bowler++) {
            result[bowler] = bowlers.get(bowler).getByFramePartResult();
        }
        return result;
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

    String getCurrentThrowerNick() {
        return bowlers.get(bowlerIndex).getNickName();
    }

    int getGameNumber() {
        return gameNumber;
    }

    boolean isFinished() {
        return finished;
    }
}
