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

    private final Party p;
    private final int ball;
    private final Bowler bowler;
    private final LaneScorer scorer;
    private final int index;
    private final int frameNum;
    private final boolean mechProb;

    LaneEvent(final Party pty, final int theIndex, final Bowler theBowler, final LaneScorer theScorer,
              final int theFrameNum, final int theBall, final boolean mechProblem) {
        p = pty;
        index = theIndex;
        bowler = theBowler;
        scorer = theScorer;
        frameNum = theFrameNum;
        ball = theBall;
        mechProb = mechProblem;
    }

    boolean isMechanicalProblem() {
        return mechProb;
    }

    int getPartySize() {
        return p.getPartySize();
    }

    int getFrameNum() {
        return frameNum;
    }

    int getScore(int b, int roll) {
        return scorer.getByBowlerByFramePartResult()[b][roll];
    }

    int getIndex() {
        return index;
    }

    int getBall() {
        return ball;
    }

    int[][] getCumulScore() {
        return scorer.getCumulScores();
    }

    public Party getParty() {
        return p;
    }

    public Bowler getBowler() {
        return bowler;
    }

    boolean isPartyEmpty() {
        if (p == null) return true;
        Vector<Bowler> members = p.getMembers();
        return members == null || members.isEmpty();
    }
}
 
