import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class ScorableBowler extends Bowler {
    private int[] gameScores;
    private static final int MAX_GAMES = 128;

    ScorableBowler(final String nick, final String full, final String mail) {
        super(nick, full, mail);
        resetHard();
    }

    ScorableBowler(final BowlerInfo bowler) {
        this(bowler.getNickName(), bowler.getFullName(), bowler.getEmail());
    }

    int[] getFinalScores() {
        return gameScores.clone();
    }

    void resetHard() {
        resetSoft();
        gameScores = new int[MAX_GAMES];
    }

    void saveState(final FileWriter fw) throws IOException {
        super.saveState();
        for (int i = 0; i < MAX_GAMES; i++) {
            if (i > 0) fw.write(Util.DELIMITER);
            fw.write(String.valueOf(gameScores[i]));
        }
        fw.write("\n");
    }

    // assumes the global LaneScorer reset has been called
    void loadState(final BufferedReader fr) throws IOException {
        try{
            super.loadState();

            final String[] scores = fr.readLine().split(Util.DELIMITER);
            for (int i = 0; i < MAX_GAMES; i++) gameScores[i] = Integer.parseInt(scores[i]);
        }catch(final Exception e){
            throw new IOException();
        }
    }

    void setGameScoresOnGameEnd(final int gameNumber) {
        gameScores[gameNumber] = getCumulativeScore()[Frame.LAST_FRAME];

        try {
            ScoreHistoryFile.addScore(getNickName(), gameScores[gameNumber]);
        } catch (final IOException e) {
            System.err.println("Exception in addScore. " + e);
        }
    }
}
