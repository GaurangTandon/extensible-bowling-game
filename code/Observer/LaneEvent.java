package Observer;

import Bowlers.ScorableParty;

import java.util.ArrayList;
import java.util.Collections;

public class LaneEvent implements Event {

    private final ArrayList<String> bowlerNicks;
    private final int partySize;
    private final int[][] cumulativeScore;
    private final int[][] score;
    private final boolean mechanicalProblemExists;
    private final String bowlerNick;
    private final int totalPinsDown;

    public LaneEvent(final ScorableParty scorer, final int pinsDown, final boolean isHalted) {
        if (scorer == null) {
            bowlerNicks = new ArrayList<>(0);
            partySize = 0;
            bowlerNick = "";
            cumulativeScore = new int[1][1];
            score = new int[1][1];
        } else {
            bowlerNicks = scorer.getMemberNicks();
            partySize = scorer.getPartySize();
            bowlerNick = scorer.getCurrentThrowerNick();
            cumulativeScore = scorer.getCumulativeScores();
            score = scorer.getByBowlerByFramePartResult();
        }
        mechanicalProblemExists = isHalted;
        totalPinsDown = pinsDown;
    }

    public final String getBowlerNick() {
        return bowlerNick;
    }

    public final Iterable<String> getBowlerNicks() {
        return Collections.unmodifiableList(bowlerNicks);
    }

    public final boolean isMechanicalProblem() {
        return mechanicalProblemExists;
    }

    public final int getPartySize() {
        return partySize;
    }

    public final int[] getScore(final int bowlerIdx) {
        return score[bowlerIdx];
    }

    public final int[] getCumulativeScore(final int bowlerIdx) {
        return cumulativeScore[bowlerIdx];
    }

    public final boolean isPartyEmpty() {
        return partySize == 0;
    }

    public int getTotalPinsDown() {
        return totalPinsDown;
    }
}
 
