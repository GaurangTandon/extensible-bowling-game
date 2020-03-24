import java.util.Vector;

class LaneEvent {

    private final Vector<String> bowlerNicks;
    private final int partySize;
    private final int[][] cumulativeScore;
    private final int[][] score;
    private final boolean mechanicalProblemExists;
    private final String bowlerNick;
    private final boolean shouldSetupGraphics;

    LaneEvent(final Vector<String> theBowlerNicks, final int thePartySize, final String theNick,
              final int[][] theCumulativeScore, final int[][] byFramePartScores, final boolean mechanicalProblem,
              final boolean ssGraphics) {
        bowlerNicks = theBowlerNicks;
        partySize = thePartySize;
        shouldSetupGraphics = ssGraphics;

        bowlerNick = theNick;
        cumulativeScore = theCumulativeScore;
        score = byFramePartScores;
        mechanicalProblemExists = mechanicalProblem;
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

    final int[][] getCumulativeScore() {
        return cumulativeScore.clone();
    }

    final boolean isPartyEmpty() {
        return partySize == 0;
    }
}
 
