import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

final class BowlerFile {
    private static final String BOWLER_DAT = "Datastore/BOWLERS.DAT";

    private BowlerFile() {
    }

    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName the nickName of the bowler to retrieve
     * @return a Bowler object
     */

    static Bowler getBowlerInfo(final String nickName)
            throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
        String data;
        Bowler foundBowler = null;

        while ((data = in.readLine()) != null && foundBowler == null) {
            // File format is nick,first_name,e-mail (csv)
            final String[] bowler = data.split(",");

            if (nickName.equals(bowler[0])) {
                foundBowler = new Bowler(bowler[0], bowler[1], bowler[2]);
                foundBowler.log();
            }
        }

        return foundBowler;
    }

    /**
     * Returns null if bowler exists
     * Returns entire list of new bowlers otherwise
     */
    static Vector<String> putBowlerIfDidntExist(final String nick, final String full, final String email) {
        try {
            final Bowler checkBowler = getBowlerInfo(nick);
            if (checkBowler != null) return null;

            putBowlerInfo(nick, full, email);
            return getBowlers();
        } catch (final IOException e) {
            System.err.println("File I/O Error");
            return null;
        }
    }

    /**
     * Stores a Bowler in the database
     *
     * @param nickName the NickName of the Bowler
     * @param fullName the FullName of the Bowler
     * @param email    the E-mail Address of the Bowler
     */

    private static void putBowlerInfo(
            final String nickName,
            final String fullName,
            final String email)
            throws IOException {
        ScoreHistoryFile.generateScoreHistoryString(nickName, fullName, email, BOWLER_DAT);
    }

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     */

    static Vector<String> getBowlers()
            throws IOException, ArrayIndexOutOfBoundsException {

        final Vector<String> allBowlers = new Vector<>();

        final BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            // File format is nick.first_name,e-mail
            final String[] bowler = data.split(",");
            //"Nick: bowler[0] Full: bowler[1] email: bowler[2]
            allBowlers.add(bowler[0]);
        }
        return allBowlers;
    }

}