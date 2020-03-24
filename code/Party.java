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

import java.util.ArrayList;

/**
 * Container that holds bowlers
 */
public class Party {

    /**
     * ArrayList of bowlers in this party
     */
    private final ArrayList<Bowler> myBowlers;
    private final String name;

    /**
     * Constructor for a Party
     *
     * @param bowlers ArrayList of bowlers that are in this party
     */

    public Party(final ArrayList bowlers) {
        myBowlers = new ArrayList(bowlers);
        name = myBowlers.get(0).getNickName() + "'s Party";
    }

    /**
     * Accessor for members in this party
     *
     * @return A ArrayList of the bowlers in this party
     */

    ArrayList<Bowler> getMembers() {
        return myBowlers;
    }

    String getName() {
        return name;
    }
}
