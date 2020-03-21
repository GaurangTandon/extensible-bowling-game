import java.util.HashMap;
import java.util.Vector;

/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    private int[] curScores;
    private int[][] cumulScores;
    private int[][] finalScores;
    int partySize;
    private final HashMap<Bowler, int[]> scores;
    private int bowlIndex;
    private Vector<Bowler> bowlers;

    LaneScorer() {
        scores = new HashMap<>(0);
    }

    void resetCumulAtBowlIndex() {
        for (int i = 0; i < Pinsetter.PIN_COUNT; i++) {
            cumulScores[bowlIndex][i] = 0;
        }
    }

    /**
     * This resets the scores for the same party
     */
    void resetScores() {
        resetScores(bowlers);
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
        this.bowlers = bowlers;
        partySize = bowlers.size();
        curScores = new int[partySize];
        finalScores = new int[partySize][128]; //Hardcoding a max of 128 games, bite me.
        cumulScores = new int[partySize][10];

        for (final Bowler o : bowlers) {
            // TODO: 25 what?
            final int[] toPut = new int[25];
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
            }
            scores.put(o, toPut);
        }
    }

    void setFinalScores(int bowlerIdx, int gameNum, int value) {
        finalScores[bowlerIdx][gameNum] = value;
    }

    int[] getScores() {
        return curScores;
    }

    int[] getFinalScores(int bowler) {
        return finalScores[bowler];
    }

    int[][] getCumulScores() {
        return cumulScores;
    }

    int get9thFrameCumulScore(int bowler) {
        return cumulScores[bowler][9];
    }

    HashMap<Bowler, int[]> getScoresHashmap() {
        return scores;
    }
}
