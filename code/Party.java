/*
 * Party.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Party.java,v $
 *   Revision 1.3  2003/02/09 21:21:31  ???
 *   Added lots of comments
 *
 *   Revision 1.2  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding Party, Lane, Bowler, and Alley.
 *
 */

import javax.naming.PartialResultException;
import java.util.Vector;

/**
 * Container that holds bowlers
 */
class Party implements GeneralParty {

    /**
     * Vector of bowlers in this party
     */
    private final Vector<GeneralBowler> bowlers;
    private final String name;

    /**
     * Constructor for a Party
     *
     * @param bowlers Vector of bowlers that are in this party
     */

    Party(final Vector bowlers) {
        this.bowlers = new Vector(bowlers);
        name = this.bowlers.get(0).getNickName() + "'s Party";
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
