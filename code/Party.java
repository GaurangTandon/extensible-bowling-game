import java.util.Vector;

/**
 * Container that holds bowlers
 */
class Party implements GeneralParty {
    private final Vector<GeneralBowler> bowlers;
    private String name;

    Party() {
        bowlers = new Vector(0);
        name = "";
    }

    public void addBowler(final GeneralBowler bowler) {
        bowlers.add(bowler);
        if (bowlers.size() == 1) {
            name += bowler.getNickName() + "'s Party";
        }
    }

    /**
     * Accessor for members in this party
     *
     * @return A vector of the bowlers in this party
     */

    public final Vector<GeneralBowler> getMembers() {
        return bowlers;
    }

    public final int getPartySize() {
        return bowlers.size();
    }

    public final Vector<String> getMemberNicks() {
        final Vector<String> nicks = new Vector<>(getPartySize());

        for (final GeneralBowler bowler : bowlers) {
            nicks.add(bowler.getNickName());
        }
        return nicks;
    }

    public final String getName() {
        return name;
    }

    public final String getMemberNick(final int index) {
        return bowlers.get(index).getNickName();
    }
}
