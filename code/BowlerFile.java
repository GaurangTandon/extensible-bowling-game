/* BowlerFile.java
 *
 *  Version:
 *  		$Id$
 *
 *  Revisions:
 * 		$Log: BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 *
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Class for interfacing with Bowler database
 */
final class BowlerFile {

    /**
     * The location of the bowler database
     */
    private static final String BOWLER_DAT = "BOWLERS.DAT";

    private BowlerFile() {
    }

    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName the nickName of the bowler to retrieve
     * @return a Bowler object
     */

    static GeneralBowler getBowlerInfo(final String nickName)
            throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
        String data;
        GeneralBowler foundBowler = null;

        while ((data = in.readLine()) != null && foundBowler == null) {
            // File format is nick \t first_name \te-mail
            final String[] bowler = data.split("\t");

            if (nickName.equals(bowler[0])) {
                foundBowler = new Bowler(bowler[0], bowler[1], bowler[2]);
                foundBowler.log();
            }
        }

        if (foundBowler == null)
            System.out.println("Nick not found...");
        return foundBowler;
    }

    static Vector putBowlerIfDidntExist(final String nick, final String full, final String email) {
        try {
            final GeneralBowler checkBowler = getBowlerInfo(nick);
            if (checkBowler == null) return null;

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

    static void putBowlerInfo(
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

    static Vector getBowlers()
            throws IOException, ArrayIndexOutOfBoundsException {

        final Vector allBowlers = new Vector();

        final BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
        String data;
        while ((data = in.readLine()) != null) {
            // File format is nick \t first_name \t e-mail
            final String[] bowler = data.split("\t");
            //"Nick: bowler[0] Full: bowler[1] email: bowler[2]
            allBowlers.add(bowler[0]);
        }
        return allBowlers;
    }

}