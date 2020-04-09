import java.util.ArrayList;
import java.util.Collections;

class LaneEvent implements Event {

    private final ArrayList<String> bowlerNicks;
    private final int partySize;
    private final int[][] cumulativeScore;
    private final int[][] score;
    private final boolean mechanicalProblemExists;
    private final String bowlerNick;
    private final int totalPinsDown;

    LaneEvent(final ScorableParty scorer, final int pinsDown, final boolean isHalted) {
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

    final String getBowlerNick() {
        return bowlerNick;
    }

    final Iterable<String> getBowlerNicks() {
        return Collections.unmodifiableList(bowlerNicks);
    }

    final boolean isMechanicalProblem() {
        return mechanicalProblemExists;
    }

    final int getPartySize() {
        return partySize;
    }

    final int[] getScore(final int bowlerIdx) {
        return score[bowlerIdx];
    }

    final int[] getCumulativeScore(final int bowlerIdx) {
        return cumulativeScore[bowlerIdx];
    }

    final boolean isPartyEmpty() {
        return partySize == 0;
    }

    int getTotalPinsDown() {
        return totalPinsDown;
    }
}
 
