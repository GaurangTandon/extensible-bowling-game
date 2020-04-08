@SuppressWarnings("StaticVariableMayNotBeInitialized")
final class BowlerScorerTest {
    private static ScorableBowler bs;

    private BowlerScorerTest() {
    }

    private static void rollMany(final int val, final int count) {
        for (int i = 0; i < count; i++) {
            bs.roll(val);
        }
    }

    @SuppressWarnings("StaticVariableUsedBeforeInitialization")
    private static void rollSpare() {
        bs.roll(Pinsetter.PIN_COUNT - 1);
        bs.roll(1);
    }

    // useful in case you forget to pass -enableassertions flag
    private static void asserter(final boolean toCheck) {
        if (!toCheck) {
            System.out.println("failed!");
            System.exit(1);
        }
    }

    private static void checkEquality(final int[] expScore) {
        bs.updateCumulativeScores();
        final int[] gotScore = bs.getCumulativeScore();
        for (int i = 0; i < bs.getCurrFrame(); i++)
            asserter(gotScore[i] == expScore[i]);
    }

    private static void testSpares() {
        bs = new ScorableBowler();
        rollSpare();
        rollSpare();
        bs.roll(5);
        final int[] expScore = new int[ScorableParty.FRAME_COUNT];
        expScore[0] = Pinsetter.PIN_COUNT + Pinsetter.PIN_COUNT - 1;
        expScore[1] = expScore[0] + Pinsetter.PIN_COUNT + 5;
        expScore[2] = expScore[1] + 5;

        checkEquality(expScore);
    }

    private static void testAllOnes() {
        bs = new ScorableBowler();
        rollMany(1, ScorableParty.FRAME_COUNT * 2);
        final int[] expScore = new int[ScorableParty.FRAME_COUNT];
        for (int i = 0; i < ScorableParty.FRAME_COUNT; i++) {
            expScore[i] = 2 * (i + 1);
        }
        checkEquality(expScore);
    }


    private static void testGutters() {
        bs = new ScorableBowler();
        rollMany(0, ScorableParty.FRAME_COUNT * 2);
        final int[] expScore = new int[ScorableParty.FRAME_COUNT];
        for (int i = 0; i < ScorableParty.FRAME_COUNT; i++) {
            expScore[i] = 0;
        }
        checkEquality(expScore);
    }


    private static void testBest() {
        bs = new ScorableBowler();
        rollMany(Pinsetter.PIN_COUNT, ScorableParty.FRAME_COUNT + 2);
        final int[] expScore = new int[ScorableParty.FRAME_COUNT];
        final int perFrameScore = 30;
        for (int i = 0; i < ScorableParty.FRAME_COUNT; i++) {
            expScore[i] = perFrameScore * (i + 1);
        }
        checkEquality(expScore);
    }

    public static void main(final String[] args) {
        testBest();
        testGutters();
        testAllOnes();
        testSpares();
        System.out.println("All tests passed");
    }
}
