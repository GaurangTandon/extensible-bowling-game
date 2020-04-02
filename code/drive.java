final class drive {
    private drive() {
    }

    public static void main(final String[] args) {

        final int numLanes = 3;
        final int maxPatronsPerParty = 6;

        final Alley a = new Alley(numLanes);
        final ControlDesk controlDesk = a.getControlDesk();

        final Observer cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
        controlDesk.subscribe(cdv);

    }
}
