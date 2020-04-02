import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

final class ScoreHistoryFile {
    private static final String SCORE_HISTORY_DAT = "SCORE_HISTORY.DAT";

    private ScoreHistoryFile() {
    }

    static void addScore(final String nick, final String date, final String score)
            throws IOException {
        generateScoreHistoryString(nick, date, score, SCORE_HISTORY_DAT);
    }

    static void generateScoreHistoryString(final String nick, final String date, final String score, final String scoreHistoryDat)
            throws IOException {
        final String data = nick + "," + date + "," + score + "\n";
        final RandomAccessFile out = new RandomAccessFile(scoreHistoryDat, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();
    }

    static Vector<Score> getScores(final String nick)
            throws IOException {
        final Vector<Score> scores = new Vector<>();

        final BufferedReader in =
                new BufferedReader(new FileReader(SCORE_HISTORY_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            final String[] scoreData = data.split(",");
            //"Nick: scoreData[0] Date: scoreData[1] Score: scoreData[2]
            if (nick.equals(scoreData[0])) {
                scores.add(new Score(scoreData[0], scoreData[1], scoreData[2]));
            }
        }
        return scores;
    }

    static Score getLeastScore() throws IOException {
        Score best = new Score("", "", "10000");

        final BufferedReader in =
                new BufferedReader(new FileReader(SCORE_HISTORY_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            final String[] scoreData = data.split(",");
            //"Nick: scoreData[0] Date: scoreData[1] Score: scoreData[2]
            if (Integer.parseInt(scoreData[2]) < best.getScore()) {
                best = new Score(scoreData[0], scoreData[1], scoreData[2]);
            }
        }
        return best;
    }

    static Score getMaxCumulativeScore() throws IOException {
        Map<String, Integer> scores = new HashMap<>(0);

        final BufferedReader in =
                new BufferedReader(new FileReader(SCORE_HISTORY_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            final String[] scoreData = data.split(",");
            //"Nick: scoreData[0] Date: scoreData[1] Score: scoreData[2]
            scores.put(scoreData[0], scores.getOrDefault(scoreData[0], 0) + Integer.parseInt(scoreData[2]));
        }

        Score best = new Score();

        for (final String sc : scores.keySet()) {
            if (best.getScore() < scores.get(sc)) {
                best = new Score(sc, "", scores.get(sc) + "");
            }
        }

        return best;
    }

    static Score getBestScore() throws IOException {
        Score best = new Score();

        final BufferedReader in =
                new BufferedReader(new FileReader(SCORE_HISTORY_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            final String[] scoreData = data.split(",");
            //"Nick: scoreData[0] Date: scoreData[1] Score: scoreData[2]
            if (Integer.parseInt(scoreData[2]) > best.getScore()) {
                best = new Score(scoreData[0], scoreData[1], scoreData[2]);
            }
        }
        return best;
    }
}
