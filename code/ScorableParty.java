import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class ScorableParty extends Party {
    static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    static final int LAST_FRAME = FRAME_COUNT - 1;
    private static final String DELIMITER = ",";

    private boolean halted;
    private boolean finished;
    private int bowlerIndex;
    private int frameNumber;
    private int gameNumber;

    void saveState(final FileWriter fw) throws IOException {
        super.saveState(fw);
        for (final ScorableBowler bowlerScorer : bowlers) {
            bowlerScorer.saveState(fw);
        }
        fw.write(gameNumber + DELIMITER + frameNumber + DELIMITER + bowlerIndex + "\n");
    }

    void loadState(final BufferedReader fr) throws IOException {
        super.loadState(fr);
        for (final ScorableBowler bowlerScorer : bowlers) {
            bowlerScorer.loadState(fr);
        }
        final String[] others = fr.readLine().split(DELIMITER);
        gameNumber = Integer.parseInt(others[0]);
        frameNumber = Integer.parseInt(others[1]);
        bowlerIndex = Integer.parseInt(others[2]);
    }

    final void nextBowler() {
        bowlerIndex++;

        if (bowlerIndex == bowlers.size()) {
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
        if (resetFinalScores) {
            for (final ScorableBowler bowler : bowlers)
                bowler.resetHard();
            gameNumber = 0;
        } else {
            for (final ScorableBowler scb : bowlers) {
                scb.resetSoft();
            }
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

        bowlers.get(bowlerIndex).setFinalScoresOnGameEnd(gameNumber);
    }

    final int[] getFinalScores(final int bowler) {
        return bowlers.get(bowler).getFinalScores();
    }

    final int[][] getCumulativeScores() {
        final int[][] cumulativeScores = new int[bowlers.size()][FRAME_COUNT];
        for (int bowler = 0; bowler < bowlers.size(); bowler++)
            cumulativeScores[bowler] = bowlers.get(bowler).getCumulativeScore();
        return cumulativeScores;
    }

    final int[][] getByBowlerByFramePartResult() {
        // return a bowler x 21 matrix of scores
        final int[][] result = new int[bowlers.size()][MAX_ROLLS];

        for (int bowler = 0; bowler < bowlers.size(); bowler++) {
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
