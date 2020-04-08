import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ScorableBowler extends Bowler {
    private Frame[] frames;
    private int currFrame;
    private static final int MAX_GAMES = 128;
    private int[] finalScores;

    ScorableBowler(final String nick, final String full, final String mail) {
        super(nick, full, mail);
        resetHard();
    }

    ScorableBowler(final Bowler bowler) {
        this(bowler.getNickName(), bowler.getFullName(), bowler.getEmail());
    }

    ScorableBowler() {
        this("", "", "");
    }

    void resetHard() {
        resetSoft();
        finalScores = new int[MAX_GAMES];
    }

    void resetSoft() {
        frames = new Frame[Frame.FRAME_COUNT];
        for (int i = 0; i < Frame.LAST_FRAME; i++)
            frames[i] = new Frame(i);
        frames[Frame.LAST_FRAME] = new LastFrame();

        currFrame = 0;
    }

    void saveState(final FileWriter fw) throws IOException {
        final List<Integer> rolls = getRolls();

        for (int i = 0; i < rolls.size(); i++) {
            if (i > 0) fw.write(Util.DELIMITER);
            fw.write(String.valueOf(rolls.get(i)));
        }
        fw.write("\n");
        for (int i = 0; i < MAX_GAMES; i++) {
            if (i > 0) fw.write(Util.DELIMITER);
            fw.write(String.valueOf(finalScores[i]));
        }
        fw.write("\n");
    }

    // assumes the global LaneScorer reset has been called
    void loadState(final BufferedReader fr) throws IOException {
        final String[] rolls = fr.readLine().split(Util.DELIMITER);
        for (final String rollAmount : rolls) roll(Integer.parseInt(rollAmount));

        final String[] scores = fr.readLine().split(Util.DELIMITER);
        for (int i = 0; i < MAX_GAMES; i++) finalScores[i] = Integer.parseInt(scores[i]);
    }

    private ArrayList<Integer> getRolls() {
        final ArrayList<Integer> rollList = new ArrayList<>(0);
        for (final Frame frame : frames) {
            frame.addRolls(rollList);
        }

        return rollList;
    }

    /**
     * Add this roll to the array
     * and also update frame and part index accordingly
     *
     * @param pinsDown The number of pins hit in the strike
     */
    void roll(final int pinsDown) {
        final Frame frame = frames[currFrame];
        frame.roll(pinsDown);

        currFrame += frame.getIncrement();
    }

    int[] getCumulativeScore() {
        final int[] cumulativeScore = new int[Frame.FRAME_COUNT];

        for (int frame = 0; frame < Frame.FRAME_COUNT; frame++)
            cumulativeScore[frame] = -1;

        final ArrayList<Integer> rolls = getRolls();
        int rollIndex = 0;

        for (int i = 0; i <= currFrame; i++) {
            final int contrib = frames[i].getContributionToScore(rolls, rollIndex);
            if (contrib == -1) break;

            cumulativeScore[i] = contrib;
            if (i > 0) cumulativeScore[i] += cumulativeScore[i - 1];

            rollIndex += frames[i].rollCount;
        }

        return cumulativeScore;
    }

    /**
     * Used in LaneView to display the entire row of cells for a bowler
     *
     * @return integer array, result per frame part
     */
    int[] getByFramePartResult() {
        final int[] perFramePartRes = new int[Frame.MAX_ROLLS];
        for (int i = 0; i < Frame.MAX_ROLLS; i++) perFramePartRes[i] = -1;

        for (int frame = 0; frame < Frame.LAST_FRAME; frame++) {
            frames[frame].setDisplayValue(perFramePartRes, 2 * frame);
        }

        return perFramePartRes;
    }

    boolean canRollAgain(final int lanesFrameNumber) {
        return currFrame == lanesFrameNumber && frames[currFrame].canRollAgain();
    }

    int getCurrFrame() {
        return currFrame;
    }

    int[] getFinalScores() {
        return finalScores;
    }

    void setFinalScoresOnGameEnd(final int gameNumber) {
        finalScores[gameNumber] = getCumulativeScore()[Frame.LAST_FRAME];
        final String finalScore = Integer.toString(finalScores[gameNumber]);

        try {
            final String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(getNickName(), dateString, finalScore);
        } catch (final IOException e) {
            System.err.println("Exception in addScore. " + e);
        }
    }
}
