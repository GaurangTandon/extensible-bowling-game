final class BowlingAlleyDriver {
    final static int maxPatronsPerParty = 6;

    private BowlingAlleyDriver() {
    }

    public static void main(final String[] args) {
        final int numLanes = 3;

        new Alley(numLanes);
    }
}
