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
    private final int ball;
    private final Bowler bowler;
    private final int[][] cumulScore;
    private final int[][] score;
    private final int index;
    private final int frameNum;
    private final boolean mechProb;

    LaneEvent(final Vector<String> theBowlerNicks, final int thePartySize, final int theIndex, final Bowler theBowler, final int[][] theCumulScore, final int[][] byFramePartScores, final int theFrameNum, final int theBall, final boolean mechProblem) {
        bowlerNicks = theBowlerNicks;
        partySize = thePartySize;

        index = theIndex;
        bowler = theBowler;
        cumulScore = theCumulScore;
        score = byFramePartScores;
        frameNum = theFrameNum;
        ball = theBall;
        mechProb = mechProblem;
    }

    final Vector<String> getBowlerNicks() {
        return bowlerNicks;
    }


    final boolean isMechanicalProblem() {
        return mechProb;
    }

    final int getPartySize() {
        return partySize;
    }

    final int getFrameNum() {
        return frameNum;
    }

    final int getScore(final int b, final int roll) {
        return score[b][roll];
    }

    final int getIndex() {
        return index;
    }

    final int getBall() {
        return ball;
    }

    final int[][] getCumulScore() {
        return cumulScore;
    }

    public final Bowler getBowler() {
        return bowler;
    }

    final boolean isPartyEmpty() {
        return partySize == 0;
    }
}
 
