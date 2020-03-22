public class BowlerScorerTest {
    private static BowlerScorer bs;

    private static void rollMany(final int val, final int count) {
        for (int i = 0; i < count; i++) {
            bs.roll(val);
        }
    }

    private void testSpares(){
        // TODO
    }

    private static void rollAllOnes() {
        bs = new BowlerScorer();
        rollMany(1, LaneUtil.FRAME_COUNT * 2);
        final int[] expScore = new int[LaneUtil.FRAME_COUNT];
        final int[] gotScore = bs.getCumulScore();
        for (int i = 0; i < LaneUtil.FRAME_COUNT; i++) {
            expScore[i] = 2 * (i + 1);
            assert gotScore[i] == expScore[i];
        }
    }


    private static void testGutters() {
        bs = new BowlerScorer();
        rollMany(0, LaneUtil.FRAME_COUNT * 2);
        final int[] expScore = new int[LaneUtil.FRAME_COUNT];
        final int[] gotScore = bs.getCumulScore();
        for (int i = 0; i < LaneUtil.FRAME_COUNT; i++) {
            expScore[i] = 0;
            assert gotScore[i] == expScore[i];
        }
    }


    private static void testBest() {
        bs = new BowlerScorer();
        rollMany(Pinsetter.PIN_COUNT, LaneUtil.FRAME_COUNT + 2);
        final int[] expScore = new int[LaneUtil.FRAME_COUNT];
        final int[] gotScore = bs.getCumulScore();
        for (int i = 0; i < LaneUtil.FRAME_COUNT; i++) {
            expScore[i] = 30 * (i + 1);
            assert gotScore[i] == expScore[i];
        }
    }

    public static void main(String[] args){
        testBest();
        testGutters();
        rollAllOnes();
        System.out.println("All tests passed");
    }
}
