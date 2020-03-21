public class LaneUtil {
    public static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    public static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    public static boolean wasSpare(final int frameChance, final int[] curScore) {
        if (frameChance <= 0) return false;

        final boolean oddRound = frameChance % 2 == 1;
        final int previousScore = curScore[frameChance - 1];
        final int currScore = curScore[frameChance];
        final int scoreSum = previousScore + currScore;

        return oddRound && scoreSum == 10;
    }

    public static boolean wasStrike(final int frameChance, final int currScore) {
        // last strike is not counted as stirke, sincie it does not give you an extra chance
        if (frameChance == 20) return false;
        final boolean evenRound = frameChance % 2 == 0;
        // 19 is the second slot of the last frame
        // at that point, bowler has another chance to get a strike
        final boolean strikeAble = evenRound || frameChance == 19;

        return strikeAble && currScore == 10;
    }

    public static boolean wasFoul(final int currScore) {
        // TODO: convert this to a constant
        return currScore == -2;
    }

    static String getCharToShow(final int frameChance, final int[] currScores) {
        final int currScore = currScores[frameChance];

        // TODO: this should show X for frameChance == 20 and currScore == 10
        final boolean wasStrike = wasStrike(frameChance, currScore);
        final boolean wasSpare = wasSpare(frameChance, currScores);
        final boolean wasFoul = wasFoul(currScore);
        final String textToSet;
        if (wasStrike)
            textToSet = "X";
        else if (wasSpare)
            textToSet = "/";
        else if (wasFoul)
            textToSet = "F";
        else
            textToSet = Integer.toString(currScore);
        return textToSet;
    }
}
