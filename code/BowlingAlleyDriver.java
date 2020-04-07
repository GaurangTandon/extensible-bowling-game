final class BowlingAlleyDriver {
    private BowlingAlleyDriver() {
    }

    public static void main(final String[] args) {
        final int numLanes = 3;
        final int maxPatronsPerParty = 6;

        new Alley(numLanes, maxPatronsPerParty);
    }
}
