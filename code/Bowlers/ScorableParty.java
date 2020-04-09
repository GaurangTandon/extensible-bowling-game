package Bowlers;

import Utilities.Util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScorableParty extends Party {
    private int bowlerIndex;
    private int frameNumber;
    private int gameNumber;

    public void saveState(final FileWriter fw) throws IOException {
        super.saveState(fw);
        for (final ScorableBowler bowlerScorer : bowlers) {
            bowlerScorer.saveState(fw);
        }
        fw.write(gameNumber + Util.DELIMITER + frameNumber + Util.DELIMITER + bowlerIndex + "\n");
    }

    public void loadState(final BufferedReader fr) throws IOException {
        try{
            super.loadState(fr);
            for (final ScorableBowler bowlerScorer : bowlers) {
                bowlerScorer.loadState(fr);
            }

            final String[] others = fr.readLine().split(Util.DELIMITER);
            gameNumber = Integer.parseInt(others[0]);
            frameNumber = Integer.parseInt(others[1]);
            bowlerIndex = Integer.parseInt(others[2]);
        } catch (final Exception err) {
            throw new IOException();
        }
    }

    public final void nextBowler() {
        bowlerIndex++;

        if (bowlerIndex == bowlers.size()) {
            frameNumber++;
            bowlerIndex = 0;
            if (frameNumber == Frame.FRAME_COUNT) {
                gameNumber++;
            }
        }
    }

    public final void resetScoresHard() {
        for (final ScorableBowler bowler : bowlers)
            bowler.resetHard();
        gameNumber = 0;
        resetScores();
    }

    private void resetScores() {
        bowlerIndex = 0;
        frameNumber = 0;
    }

    public final void roll(final int pinsDowned) {
        final ScorableBowler bowlerScorer = bowlers.get(bowlerIndex);
        bowlerScorer.roll(pinsDowned);
    }

    public final boolean canRollAgain() {
        return bowlers.get(bowlerIndex).canRollAgain(frameNumber);
    }

    public void setFinalScoresOnGameEnd() {
        if (frameNumber != Frame.LAST_FRAME) return;

        bowlers.get(bowlerIndex).setGameScoresOnGameEnd(gameNumber);
    }

    public final int[] getFinalScores(final int bowler) {
        return bowlers.get(bowler).getFinalScores();
    }

    public final int[][] getCumulativeScores() {
        final int[][] cumulativeScores = new int[bowlers.size()][Frame.FRAME_COUNT];
        for (int bowler = 0; bowler < bowlers.size(); bowler++)
            cumulativeScores[bowler] = bowlers.get(bowler).getCumulativeScore();
        return cumulativeScores;
    }

    public final int[][] getByBowlerByFramePartResult() {
        // return a bowler x 21 matrix of scores
        final int[][] result = new int[bowlers.size()][Frame.MAX_ROLLS];

        for (int bowler = 0; bowler < bowlers.size(); bowler++) {
            result[bowler] = bowlers.get(bowler).getByFramePartResult();
        }
        return result;
    }

    public final void onGameFinish() {
        for (final ScorableBowler scb : bowlers) {
            scb.resetSoft();
        }
        resetScores();
    }

    public String getCurrentThrowerNick() {
        return bowlers.get(bowlerIndex).getNickName();
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public boolean isFinished() {
        return frameNumber == Frame.FRAME_COUNT;
    }
}
