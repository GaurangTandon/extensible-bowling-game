import java.util.Vector;

class LaneEvent implements Event {

    private final Vector<String> bowlerNicks;
    private final int partySize;
    private final int[][] cumulativeScore;
    private final int[][] score;
    private final boolean mechanicalProblemExists;
    private final String bowlerNick;
    private final int totalPinsDown;

    LaneEvent(final Vector<String> theBowlerNicks, final int thePartySize, final String theNick,
              final int[][] theCumulativeScore, final int[][] byFramePartScores, final boolean mechanicalProblem,
              final int pinsDown) {
        bowlerNicks = theBowlerNicks;
        partySize = thePartySize;
        totalPinsDown = pinsDown;

        bowlerNick = theNick;
        cumulativeScore = theCumulativeScore;
        score = byFramePartScores;
        mechanicalProblemExists = mechanicalProblem;
    }

    final String getBowlerNick() {
        return bowlerNick;
    }

    final Iterable<String> getBowlerNicks() {
        return bowlerNicks;
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
 
