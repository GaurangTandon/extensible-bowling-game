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

import java.util.HashMap;
import java.util.Vector;

class LaneEvent {

    private final Party p;
    private final int ball;
    private final Bowler bowler;
    private final int[][] cumulScore;
    private final int[][] score;
    private final int index;
    private final int frameNum;
    private final int[] curScores;
    private final boolean mechProb;

    public LaneEvent(final Party pty, final int theIndex, final Bowler theBowler, final int[][] theCumulScore, final int[][] byFramePartScores, final int theFrameNum, final int[] theCurScores, final int theBall, final boolean mechProblem) {
        p = pty;
        index = theIndex;
        bowler = theBowler;
        cumulScore = theCumulScore;
        score = byFramePartScores;
        curScores = theCurScores;
        frameNum = theFrameNum;
        ball = theBall;
        mechProb = mechProblem;
    }

    public boolean isMechanicalProblem() {
        return mechProb;
    }

    public int getFrameNum() {
        return frameNum;
    }

    public int getScore(int b, int roll) {
        return score[b][roll];
    }

    public int getIndex() {
        return index;
    }

    public int getBall() {
        return ball;
    }

    public int[][] getCumulScore() {
        return cumulScore;
    }

    public Party getParty() {
        return p;
    }

    public Bowler getBowler() {
        return bowler;
    }

    public boolean isPartyEmpty() {
        if (p == null) return true;
        Vector<Bowler> members = p.getMembers();
        return members == null || members.isEmpty();
    }
}
 
