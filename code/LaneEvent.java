/*  $Id$
 *
 *  Revisions:
 *    $Log: LaneEvent.java,v $
 *    Revision 1.6  2003/02/16 22:59:34  ???
 *    added mechnanical problem flag
 *
 *    Revision 1.5  2003/02/02 23:55:31  ???
 *    Many many changes.
 *
 *    Revision 1.4  2003/02/02 22:44:26  ???
 *    More data.
 *
 *    Revision 1.3  2003/02/02 17:49:31  ???
 *    Modified.
 *
 *    Revision 1.2  2003/01/30 21:21:07  ???
 *    *** empty log message ***
 *
 *    Revision 1.1  2003/01/19 22:12:40  ???
 *    created laneevent and laneobserver
 *
 *
 */

import java.util.Vector;

class LaneEvent {

    private final Vector<String> bowlerNicks;
    private final int partySize;
    private final int[][] cumulScore;
    private final int[][] score;
    private final boolean mechanicalProblemExists;
    private final String bowlerNick;
    private final boolean shouldSetupGraphics;

    LaneEvent(final Vector<String> theBowlerNicks, final int thePartySize, final String theNick, final int[][] theCumulScore, final int[][] byFramePartScores, final boolean mechProblem, final boolean ssGraphics) {
        bowlerNicks = theBowlerNicks;
        partySize = thePartySize;
        shouldSetupGraphics = ssGraphics;

        bowlerNick = theNick;
        cumulScore = theCumulScore;
        score = byFramePartScores;
        mechanicalProblemExists = mechProblem;
    }

    final String getBowlerNick() {
        return bowlerNick;
    }

    final boolean shouldSetupGraphics() {
        return shouldSetupGraphics;
    }

    final Vector<String> getBowlerNicks() {
        return bowlerNicks;
    }

    final boolean isMechanicalProblem() {
        return mechanicalProblemExists;
    }

    final int getPartySize() {
        return partySize;
    }

    final int getScore(final int bowlerIdx, final int roll) {
        return score[bowlerIdx][roll];
    }

    final int[][] getCumulScore() {
        return cumulScore;
    }

    final boolean isPartyEmpty() {
        return partySize == 0;
    }
}
 
