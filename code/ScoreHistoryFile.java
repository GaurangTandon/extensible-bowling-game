import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

class ScoreHistoryFile {

    private static final String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

    public static void addScore(final String nick, final String date, final String score)
            throws IOException {

        final String data = nick + "\t" + date + "\t" + score + "\n";

        final RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        out.close();
    }

    public static Vector<Score> getScores(final String nick)
            throws IOException {
        final Vector<Score> scores = new Vector<>();

        final BufferedReader in =
                new BufferedReader(new FileReader(SCOREHISTORY_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            // File format is nick\tfname\te-mail
            final String[] scoredata = data.split("\t");
            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
            if (nick.equals(scoredata[0])) {
                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
            }
        }
        return scores;
    }

}
