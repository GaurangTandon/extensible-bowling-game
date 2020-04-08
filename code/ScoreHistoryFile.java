import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

final class ScoreHistoryFile {
    private static final String SCORE_HISTORY_DAT = "Datastore/SCORE_HISTORY.DAT";

    private ScoreHistoryFile() {
    }

    static void addScore(final String nick, final int score)
            throws IOException {
        final String dateString = Util.getDateString();
        generateScoreHistoryString(nick, dateString, Integer.toString(score), SCORE_HISTORY_DAT);
    }

    static void generateScoreHistoryString(final String nick, final String date, final String score, final String scoreHistoryDat)
            throws IOException {
        final String data = nick + "," + date + "," + score + "\n";
        final RandomAccessFile out = new RandomAccessFile(scoreHistoryDat, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();
    }

    private static ArrayList<Score> getAllScores() {
        final ArrayList<Score> scores = new ArrayList<>(0);

        try {
            final BufferedReader in =
                    new BufferedReader(new FileReader(SCORE_HISTORY_DAT));
            String data;
            while ((data = in.readLine()) != null) {
                final String[] scoreData = data.split(Util.DELIMITER);
                //"Nick: scoreData[0] Date: scoreData[1] Score: scoreData[2]
                scores.add(new Score(scoreData[0], scoreData[1], scoreData[2]));
            }
        } catch (final Exception error) {
            error.printStackTrace();
        }
        return scores;
    }

    static ArrayList<Score> getScores(final String nick) {
        final ArrayList<Score> scores = getAllScores();
        final ArrayList<Score> retScores = new ArrayList<>(0);

        for (final Score score : scores) {
            if (nick.equals(score.getDate())) {
                retScores.add(score);
            }
        }

        return retScores;
    }

    static Score getLeastScore() {
        final ArrayList<Score> scores = getAllScores();
        Score best = new Score("", "", "10000");

        for (final Score score : scores) {
            if (best.getScore() < score.getScore()) {
                best = score;
            }
        }

        return best;
    }

    static Score getMaxCumulativeScore() {
        final ArrayList<Score> scores = getAllScores();
        final Map<String, Integer> mappedScores = new HashMap<>(0);
        for (final Score score : scores) {
            final String nick = score.getNick();
            final int oldScore = mappedScores.getOrDefault(nick, 0);
            mappedScores.put(nick, oldScore + score.getScore());
        }

        Score best = new Score();

        for (final Map.Entry<String, Integer> entry : mappedScores.entrySet()) {
            if (best.getScore() < entry.getValue()) {
                best = new Score(entry.getKey(), "", entry.getValue() + "");
            }
        }

        return best;
    }

    static Score getBestScore() {
        final ArrayList<Score> scores = getAllScores();
        Score best = new Score();

        for (final Score score : scores) {
            if (best.getScore() < score.getScore()) {
                best = score;
            }
        }

        return best;
    }
}
